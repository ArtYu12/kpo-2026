package vyshkat.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import vyshkat.exception.DomainValidationException;

@DisplayName("Model validation")
class ModelValidationTest {

    @Test
    @DisplayName("Scooter keeps all values")
    void scooterKeepsProperties() {
        var scooter = new Scooter("TR-001", 8, true, 1.4);

        assertAll(
                () -> assertEquals("Scooter", scooter.name()),
                () -> assertEquals("TR-001", scooter.inventoryNumber()),
                () -> assertEquals(8, scooter.beginnerEase()),
                () -> assertEquals(1.4,
                        scooter.dailyEnergyConsumption()),
                () -> assertEquals(true, scooter.serviceable())
        );
    }

    @ParameterizedTest(name = "Value {0} is rejected")
    @ValueSource(ints = {-1, 0, 11, 20})
    @DisplayName("Beginner level must be from 1 to 10")
    void beginnerEaseMustBeInRange(final int beginnerEase) {
        assertThrows(
                DomainValidationException.class,
                () -> new Bicycle("TR-001", beginnerEase, true)
        );
    }

    @Test
    @DisplayName("Negative transport energy is rejected")
    void transportEnergyMustNotBeNegative() {
        assertThrows(
                DomainValidationException.class,
                () -> new Scooter("TR-001", 8, true, -0.1)
        );
    }

    @Test
    @DisplayName("Negative station energy is rejected")
    void stationEnergyMustNotBeNegative() {
        assertThrows(
                DomainValidationException.class,
                () -> new ChargingStation("EQ-001", -0.1)
        );
    }

    @Test
    @DisplayName("Blank inventory number is rejected")
    void inventoryNumberMustNotBeBlank() {
        assertThrows(
                DomainValidationException.class,
                () -> new Helmet("   ")
        );
    }
}
