package pos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Starts the POS application
@SpringBootApplication(proxyBeanMethods = false)
public final class PosApplication {

    private PosApplication() {
    }

    // Starts Spring Boot
    public static void main(final String[] args) {
        SpringApplication.run(PosApplication.class, args);
    }
}
