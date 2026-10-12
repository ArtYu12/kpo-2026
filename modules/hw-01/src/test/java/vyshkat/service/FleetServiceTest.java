package vyshkat.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vyshkat.exception.DuplicateInventoryNumberException;
import vyshkat.model.Bicycle;
import vyshkat.model.ChargingStation;
import vyshkat.model.ElectricBicycle;
import vyshkat.model.Helmet;
import vyshkat.model.Scooter;

@ExtendWith(MockitoExtension.class)
@DisplayName("Fleet service")
class FleetServiceTest {

    @Mock
    private ServiceCenter serviceCenter;

    private FleetService fleetService;

    @BeforeEach
    void setUp() {
        fleetService = new FleetService(serviceCenter);
    }

    @Test
    @DisplayName("Transport is added after inspection")
    void acceptsTransportAfterSuccessfulInspection() {
        var scooter = new Scooter("TR-001", 8, true, 1.4);
        when(serviceCenter.passesInspection(scooter)).thenReturn(true);

        var accepted = fleetService.acceptTransport(scooter);

        assertTrue(accepted);
        assertEquals(1, fleetService.transportCount());
        verify(serviceCenter).passesInspection(scooter);
    }

    @Test
    @DisplayName("Transport is not added after failed inspection")
    void rejectsTransportAfterFailedInspection() {
        var scooter = new Scooter("TR-001", 8, false, 1.4);
        when(serviceCenter.passesInspection(scooter)).thenReturn(false);

        var accepted = fleetService.acceptTransport(scooter);

        assertFalse(accepted);
        assertEquals(0, fleetService.transportCount());
        assertEquals(0, fleetService.totalInventoryCount());
        verify(serviceCenter).passesInspection(scooter);
    }

    @Test
    @DisplayName("Energy is summed only for consumers")
    void calculatesTotalEnergyConsumption() {
        var scooter = new Scooter("TR-001", 8, true, 1.4);
        var electricBicycle = new ElectricBicycle(
                "TR-002",
                5,
                true,
                0.9
        );
        var bicycle = new Bicycle("TR-003", 9, true);
        when(serviceCenter.passesInspection(scooter)).thenReturn(true);
        when(serviceCenter.passesInspection(electricBicycle))
                .thenReturn(true);
        when(serviceCenter.passesInspection(bicycle)).thenReturn(true);

        fleetService.acceptTransport(scooter);
        fleetService.acceptTransport(electricBicycle);
        fleetService.acceptTransport(bicycle);
        fleetService.addEquipment(new Helmet("EQ-001"));
        fleetService.addEquipment(new ChargingStation("EQ-002", 4.5));

        assertEquals(
                6.8,
                fleetService.totalDailyEnergyConsumption(),
                0.0001
        );
    }

    @Test
    @DisplayName("Beginner list uses level six")
    void selectsBeginnerFriendlyTransport() {
        var easy = new Bicycle("TR-001", 6, true);
        var difficult = new Bicycle("TR-002", 5, true);
        var veryEasy = new Scooter("TR-003", 10, true, 1.0);
        when(serviceCenter.passesInspection(easy)).thenReturn(true);
        when(serviceCenter.passesInspection(difficult)).thenReturn(true);
        when(serviceCenter.passesInspection(veryEasy)).thenReturn(true);

        fleetService.acceptTransport(easy);
        fleetService.acceptTransport(difficult);
        fleetService.acceptTransport(veryEasy);

        var result = fleetService.beginnerFriendlyTransport();

        assertEquals(2, result.size());
        assertTrue(result.contains(easy));
        assertTrue(result.contains(veryEasy));
    }

    @Test
    @DisplayName("Inventory contains transport and equipment")
    void inventoryContainsTransportAndEquipment() {
        var bicycle = new Bicycle("TR-001", 7, true);
        var helmet = new Helmet("EQ-001");
        when(serviceCenter.passesInspection(bicycle)).thenReturn(true);

        fleetService.acceptTransport(bicycle);
        fleetService.addEquipment(helmet);

        assertEquals(1, fleetService.transportCount());
        assertEquals(1, fleetService.equipmentCount());
        assertEquals(2, fleetService.totalInventoryCount());
        assertEquals(
                "TR-001",
                fleetService.inventoryItems().get(0).inventoryNumber()
        );
        assertEquals(
                "EQ-001",
                fleetService.inventoryItems().get(1).inventoryNumber()
        );
    }

    @Test
    @DisplayName("Duplicate inventory number is rejected")
    void rejectsDuplicateInventoryNumber() {
        var helmet = new Helmet("ITEM-001");
        var station = new ChargingStation("ITEM-001", 2.0);
        fleetService.addEquipment(helmet);

        assertThrows(
                DuplicateInventoryNumberException.class,
                () -> fleetService.addEquipment(station)
        );
    }
}
