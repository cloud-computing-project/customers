package si.fri.rso.samples.customers.api.v1.resources;

import com.kumuluz.ee.common.runtime.EeRuntime;
import org.json.JSONArray;
import org.json.JSONObject;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import si.fri.rso.samples.customers.api.v1.configuration.RestProperties;

@Path("demo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class DemoResource {

    @Inject
    private RestProperties restProperties;

    @GET
    @Path("instanceid")
    public Response getInstanceId() {

        String instanceId =
                "{\"instanceId\" : \"" + EeRuntime.getInstance().getInstanceId() + "\"}";

        return Response.ok(instanceId).build();
    }

    @POST
    @Path("healthy")
    public Response setHealth(Boolean healthy) {
        restProperties.setHealthy(healthy);
        return Response.ok().build();
    }

    @GET
    @Path("healthy")
    public Boolean getHealth() {
        return restProperties.isHealthy();
    }

    @GET
    @Path("info")
    @Produces("application/json")
    public Response info() {

        JSONObject json = new JSONObject();

        JSONArray members = new JSONArray();
        members.put("as7849");
        members.put("ao2282");
        members.put("bj9914");

        JSONArray microservices = new JSONArray();
        microservices.put("http://169.51.20.134:31568/v1/customers");
        microservices.put("http://169.51.20.134:32564/v1/orders");
        microservices.put("http://169.51.20.63:31567/v1/products");

        JSONArray github = new JSONArray();
        github.put("https://github.com/cloud-computing-project/customers");
        github.put("https://github.com/cloud-computing-project/orders");
        github.put("https://github.com/cloud-computing-project/products");

        JSONArray travis = new JSONArray();
        travis.put("https://travis-ci.org/cloud-computing-project/customers");
        travis.put("https://travis-ci.org/cloud-computing-project/orders");
        travis.put("https://travis-ci.org/cloud-computing-project/products");

        JSONArray dockerhub = new JSONArray();
        dockerhub.put("https://hub.docker.com/r/amela/customers/");
        dockerhub.put("https://hub.docker.com/r/ejmric/orders/");
        dockerhub.put("https://hub.docker.com/r/bozen/products/");

        json.put("members", members);
        json.put("project_description", "Our project includes an application for shopping online(on the example of e-bay).");
        json.put("microservices", microservices);
        json.put("github", github);
        json.put("travis", travis);
        json.put("dockerhub", dockerhub);

        return Response.ok(json.toString()).build();
    }
}
