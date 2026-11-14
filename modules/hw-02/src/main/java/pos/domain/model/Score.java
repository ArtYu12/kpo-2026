package pos.domain.model;

import pos.domain.exception.DomainValidationException;

// Score for a criterion
public record Score(double value, double maxScore) {

    private static final double MIN_SCORE = 0;

    // Checks that the score is from 0 to the maximum
    public Score {
        DomainChecks.requireNumber(value, "Score must be a number");
        if (value < MIN_SCORE || value > maxScore) {
            throw new DomainValidationException(
                    "Score must be from 0 to " + maxScore
            );
        }
    }

    // Returns part of the maximum score from 0 to 1
    public double ratio() {
        return value / maxScore;
    }
}
