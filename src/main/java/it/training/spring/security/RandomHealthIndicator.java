package it.training.spring.security;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomHealthIndicator implements HealthIndicator {
    @Override
    public Health getHealth(boolean includeDetails) {
        return health();
    }

    @Override
    public Health health() {
        double rnd = new Random().nextDouble();
        Health.Builder status = Health.up();
        if (rnd > 0.5) {
            status = Health.down();
        }
        return status.build();
    }
}
