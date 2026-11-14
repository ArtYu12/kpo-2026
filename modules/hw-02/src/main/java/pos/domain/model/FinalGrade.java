package pos.domain.model;

import pos.domain.exception.DomainValidationException;

// Final grade of a student on the ten point scale
public record FinalGrade(double value) {

    // Maximum grade of the scale
    public static final double MAX_GRADE = 10.0;

    private static final double MIN_GRADE = 0.0;
    private static final double EPSILON = 1e-9;

    // Checks that the grade is on the scale
    public FinalGrade {
        if (value < MIN_GRADE - EPSILON || value > MAX_GRADE + EPSILON) {
            throw new DomainValidationException(
                    "Grade must be from 0 to 10"
            );
        }
    }

    // Returns rounded grade, 5.5 becomes 6
    public int rounded() {
        return (int) Math.round(value);
    }
}
