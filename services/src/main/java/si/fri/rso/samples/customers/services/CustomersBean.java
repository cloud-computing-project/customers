package si.fri.rso.samples.customers.services;


import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import si.fri.rso.samples.customers.models.Comment;
import si.fri.rso.samples.customers.models.Customer;
import si.fri.rso.samples.customers.models.Order;
import si.fri.rso.samples.customers.services.config.RestProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import javax.persistence.TypedQuery;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.ProcessingException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@RequestScoped
public class CustomersBean {

    private Logger log = LogManager.getLogger(CustomersBean.class.getName());

    @Inject
    private RestProperties restProperties;

    @Inject
    private EntityManager em;

    @Inject
    private CustomersBean customersBean;

    private Client httpClient;

    @Inject
    @DiscoverService("rso-orders")
    private Optional<String> baseUrl;

    @Inject
    @DiscoverService("rso-comments")
    private Optional<String> baseUrlComments;

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
    }


    public List<Customer> getCustomers() {

        TypedQuery<Customer> query = em.createNamedQuery("Customer.getAll", Customer.class);

        return query.getResultList();

    }

    public List<Customer> getCustomersFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, Customer.class, queryParameters);
    }

    public Customer getCustomer(String customerId) {

        Customer customer = em.find(Customer.class, customerId);

        if (customer == null) {
            throw new NotFoundException();
        }

        if (restProperties.isOrderServiceEnabled()) {
            List<Order> orders = customersBean.getOrders(customerId);
            customer.setOrders(orders);
        }

        if (restProperties.isCommentServiceEnabled()) {
            List<Comment> comments = customersBean.getComments(customerId);
            customer.setComments(comments);
        }
        log.info("comment service is "+ restProperties.isCommentServiceEnabled());


        return customer;
    }

    public Customer createCustomer(Customer customer) {

        try {
            beginTx();
            em.persist(customer);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return customer;
    }

    public Customer putCustomer(String customerId, Customer customer) {

        Customer c = em.find(Customer.class, customerId);

        if (c == null) {
            return null;
        }

        try {
            beginTx();
            customer.setId(c.getId());
            customer = em.merge(customer);
            commitTx();
        } catch (Exception e) {
            rollbackTx();
        }

        return customer;
    }

    public boolean deleteCustomer(String customerId) {

        Customer customer = em.find(Customer.class, customerId);

        if (customer != null) {
            try {
                beginTx();
                em.remove(customer);
                commitTx();
            } catch (Exception e) {
                rollbackTx();
            }
        } else
            return false;

        return true;
    }


    public List<Order> getOrders(String customerId) {
        log.info("base url orders " + baseUrl);

        if (baseUrl.isPresent()) {
            try {
                return httpClient
                        .target(baseUrl.get() + "/v1/orders?where=customerId:EQ:" + customerId)
                        .request().get(new GenericType<List<Order>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.error(e);
                throw new InternalServerErrorException(e);
            }
        }

        return new ArrayList<>();
    }

    public List<Order> getOrdersFallback(String customerId) {
        return new ArrayList<>();
    }

    public List<Comment> getComments(String customerId) {
        log.info("base url comments " + baseUrlComments);
        if (baseUrlComments.isPresent()) {
            try {
                return httpClient
                        .target(baseUrlComments.get() + "/v1/comments?where=customerId:EQ:" + customerId)
                        .request().get(new GenericType<List<Comment>>() {
                        });
            } catch (WebApplicationException | ProcessingException e) {
                log.error(e);
                throw new InternalServerErrorException(e);
            }
        }

        return new ArrayList<>();
    }

    public List<Comment> getCommentsFallback(String customerId) {
        return new ArrayList<>();
    }


    private void beginTx() {
        if (!em.getTransaction().isActive())
            em.getTransaction().begin();
    }

    private void commitTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().commit();
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive())
            em.getTransaction().rollback();
    }
}
