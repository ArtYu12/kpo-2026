package vyshkat.model;

import vyshkat.exception.DomainValidationException;

public class CustomTransport extends Transport {
    public CustomTransport (
        final String transportName,
        final String inventoryNumber,
        final int beginnerEase,
        final boolean serviceable
    ) {
        validateText(transportName, "Transport name is required");

        super(
                transportName,
                inventoryNumber,
                beginnerEase,
                serviceable
        );
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
