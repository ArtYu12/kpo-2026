package vyshkat.configuration;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import vyshkat.VyshkatApplication;
import vyshkat.service.FleetService;
import vyshkat.service.ServiceCenter;

@SpringBootTest(classes = VyshkatApplication.class)
@ActiveProfiles("test")
@DisplayName("Spring DI container")
class SpringContextTest {

    @Autowired
    private FleetService fleetService;

    @Autowired
    private ServiceCenter serviceCenter;

    @Test
    @DisplayName("Context creates fleet and inspection services")
    void contextCreatesApplicationServices() {
        assertNotNull(fleetService);
        assertNotNull(serviceCenter);
    }
}
