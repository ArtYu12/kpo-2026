package pos.domain.model;

import pos.domain.exception.DomainValidationException;

// Contribution of a criterion to the final grade
public record Weight(double value) {

    private static final double MAX_WEIGHT = 1.0;

    // Checks that the weight is in the allowed range
    public Weight {
        DomainChecks.requireNumber(value, "Weight must be a number");
        if (value <= 0 || value > MAX_WEIGHT) {
            throw new DomainValidationException(
                    "Weight must be greater than 0 and not greater than 1"
            );
        }
    }
}
