package vyshkat.model;

import vyshkat.exception.DomainValidationException;

// Base equipment type
public abstract class Equipment implements InventoryItem {

    private final String name;
    private final String inventoryNumber;

    // Creates equipment
    protected Equipment(
            final String name,
            final String inventoryNumber
    ) {
        validateText(name, "Name is required");
        validateText(inventoryNumber, "Inventory number is required");
        this.name = name;
        this.inventoryNumber = inventoryNumber;
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public final String inventoryNumber() {
        return inventoryNumber;
    }

    private static void validateText(
            final String value,
            final String message
    ) {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException(message);
        }
    }
}
