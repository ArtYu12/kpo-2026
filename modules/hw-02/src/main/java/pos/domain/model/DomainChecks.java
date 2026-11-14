package pos.domain.model;

import pos.domain.exception.DomainValidationException;

// Common checks used by domain objects
final class DomainChecks {

    private DomainChecks() {
    }

    static void requireText(final String value, final String message) {
        if (value == null || value.isBlank()) {
            throw new DomainValidationException(message);
        }
    }

    static void requireNumber(final double value, final String message) {
        if (Double.isNaN(value) || Double.isInfinite(value)) {
            throw new DomainValidationException(message);
        }
    }
}
