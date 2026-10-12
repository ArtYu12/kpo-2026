package vyshkat.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vyshkat.model.Scooter;

@DisplayName("Default service center")
class DefaultServiceCenterTest {

    private final ServiceCenter serviceCenter =
            new DefaultServiceCenter();

    @Test
    @DisplayName("Serviceable transport passes inspection")
    void serviceableTransportPassesInspection() {
        var scooter = new Scooter("TR-001", 8, true, 1.4);

        assertTrue(serviceCenter.passesInspection(scooter));
    }

    @Test
    @DisplayName("Broken transport fails inspection")
    void brokenTransportFailsInspection() {
        var scooter = new Scooter("TR-001", 8, false, 1.4);

        assertFalse(serviceCenter.passesInspection(scooter));
    }
}
