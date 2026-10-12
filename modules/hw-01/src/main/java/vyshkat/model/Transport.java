package vyshkat.model;

import vyshkat.exception.DomainValidationException;

// Base transport type
public abstract class Transport implements InventoryItem {

    private static final int MIN_BEGINNER_EASE = 1;
    private static final int MAX_BEGINNER_EASE = 10;

    private final String name;
    private final String inventoryNumber;
    private final int beginnerEase;
    private final boolean serviceable;

    // Creates transport
    protected Transport(
            final String name,
            final String inventoryNumber,
            final int beginnerEase,
            final boolean serviceable
    ) {
        validateText(name, "Name required");
        validateText(inventoryNumber, "Inventory number required");
        if (beginnerEase < MIN_BEGINNER_EASE
                || beginnerEase > MAX_BEGINNER_EASE) {
            throw new DomainValidationException(
                    "Level must be from 1 to 10"
            );
        }
        this.name = name;
        this.inventoryNumber = inventoryNumber;
        this.beginnerEase = beginnerEase;
        this.serviceable = serviceable;
    }

    @Override
    public final String name() {
        return name;
    }

    @Override
    public final String inventoryNumber() {
        return inventoryNumber;
    }

    // Returns the beginner level
    public final int beginnerEase() {
        return beginnerEase;
    }

    // Returns the service state
    public final boolean serviceable() {
        return serviceable;
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
