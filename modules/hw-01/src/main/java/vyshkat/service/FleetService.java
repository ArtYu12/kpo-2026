package vyshkat.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import vyshkat.exception.DomainValidationException;
import vyshkat.exception.DuplicateInventoryNumberException;
import vyshkat.model.EnergyConsumer;
import vyshkat.model.Equipment;
import vyshkat.model.InventoryItem;
import vyshkat.model.Transport;

// Manages the fleet
@Service
public final class FleetService {
    private static final int BEGINNER_EASE_THRESHOLD = 6;

    private final ServiceCenter serviceCenter;
    private final List<InventoryItem> inventoryItems = new ArrayList<>();

    // Creates the fleet service
    public FleetService(final ServiceCenter serviceCenter) {
        if (serviceCenter == null) {
            throw new DomainValidationException(
                    "Inspection service required"
            );
        }
        this.serviceCenter = serviceCenter;
    }

    // Adds transport after inspection
    public boolean acceptTransport(final Transport transport) {
        if (transport == null) {
            throw new DomainValidationException(
                    "Transport required"
            );
        }
        ensureUniqueInventoryNumber(transport.inventoryNumber());
        if (!serviceCenter.passesInspection(transport)) {
            return false;
        }
        inventoryItems.add(transport);
        return true;
    }

    // Adds equipment to inventory
    public void addEquipment(final Equipment equipment) {
        if (equipment == null) {
            throw new DomainValidationException("Equipment required");
        }
        ensureUniqueInventoryNumber(equipment.inventoryNumber());
        inventoryItems.add(equipment);
    }

    // Returns the transport count
    public int transportCount() {
        return transports().size();
    }

    // Returns the equipment count
    public int equipmentCount() {
        return inventoryItems.size() - transports().size();
    }

    // Returns the full item count
    public int totalInventoryCount() {
        return inventoryItems.size();
    }

    // Returns total daily energy use
    public double totalDailyEnergyConsumption() {
        return inventoryItems.stream()
                .filter(EnergyConsumer.class::isInstance)
                .map(EnergyConsumer.class::cast)
                .mapToDouble(EnergyConsumer::dailyEnergyConsumption)
                .sum();
    }

    // Returns transport for beginners
    public List<Transport> beginnerFriendlyTransport() {
        return transports().stream()
                .filter(transport -> transport.beginnerEase()
                        >= BEGINNER_EASE_THRESHOLD)
                .toList();
    }

    // Returns all inventory items
    public List<InventoryItem> inventoryItems() {
        return List.copyOf(inventoryItems);
    }

    private List<Transport> transports() {
        return inventoryItems.stream()
                .filter(Transport.class::isInstance)
                .map(Transport.class::cast)
                .toList();
    }

    private void ensureUniqueInventoryNumber(
            final String inventoryNumber
    ) {
        var numberExists = inventoryItems.stream()
                .anyMatch(item -> item.inventoryNumber()
                        .equals(inventoryNumber));
        if (numberExists) {
            throw new DuplicateInventoryNumberException(inventoryNumber);
        }
    }
}
