package si.fri.rso.samples.customers.api.v1.health;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import si.fri.rso.samples.customers.api.v1.configuration.RestProperties;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Health
@ApplicationScoped
public class CustomerServiceHealthCheck implements HealthCheck{
    @Inject
    private RestProperties restProperties;

    @Override
    public HealthCheckResponse call() {

        if (restProperties.isHealthy()) {
            return HealthCheckResponse.named(CustomerServiceHealthCheck.class.getSimpleName()).up().build();
        } else {
            return HealthCheckResponse.named(CustomerServiceHealthCheck.class.getSimpleName()).down().build();
        }

    }
}
