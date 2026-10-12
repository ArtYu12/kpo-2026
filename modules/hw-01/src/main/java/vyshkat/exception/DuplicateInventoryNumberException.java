package vyshkat.exception;

// Reports a duplicate number
public class DuplicateInventoryNumberException extends RuntimeException {

    // Creates the exception
    public DuplicateInventoryNumberException(final String inventoryNumber) {
        super("Inventory number already exists: " + inventoryNumber);
    }
}
