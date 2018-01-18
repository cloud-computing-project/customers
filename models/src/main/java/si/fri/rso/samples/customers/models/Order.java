package si.fri.rso.samples.customers.models;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "orders")
@NamedQueries(value =
        {
                @NamedQuery(name = "Order.getAll", query = "SELECT o FROM orders o"),
                @NamedQuery(name = "Order.findByCustomer", query = "SELECT o FROM orders o WHERE o.customerId = " +
                        ":customerId")
        })
@UuidGenerator(name = "idGenerator")
public class Order {

    @Id
    @GeneratedValue(generator = "idGenerator")
    private String id;

    //private String description;
    @Column(name = "product_id")
    private String productId;

    private Date submitted;

    @Column(name = "customer_id")
    private String customerId;

    @Column(name = "payment_id")
    private String paymentId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Date getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Date submitted) {
        this.submitted = submitted;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
}
