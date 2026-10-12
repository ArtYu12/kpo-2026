package vyshkat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Starts the application
@SpringBootApplication(proxyBeanMethods = false)
public final class VyshkatApplication {

    private VyshkatApplication() {
    }

    // Starts Spring Boot
    public static void main(final String[] args) {
        SpringApplication.run(VyshkatApplication.class, args);
    }
}
