package pos.domain.model;

import pos.domain.exception.DomainValidationException;

// A criterion of the rubric with its maximum score and weight
public final class Criterion {

    private final int id;
    private final String name;
    private final double maxScore;
    private final Weight weight;

    // Creates a criterion
    Criterion(
            final int id,
            final String name,
            final double maxScore,
            final Weight weight
    ) {
        DomainChecks.requireText(name, "Criterion name required");
        DomainChecks.requireNumber(maxScore, "Max score must be a number");
        if (maxScore <= 0) {
            throw new DomainValidationException(
                    "Max score must be positive"
            );
        }
        this.id = id;
        this.name = name.trim();
        this.maxScore = maxScore;
        this.weight = weight;
    }

    // Returns criterion identifier
    public int id() {
        return id;
    }

    // Returns criterion name
    public String name() {
        return name;
    }

    // Returns maximum score for the criterion
    public double maxScore() {
        return maxScore;
    }

    // Returns contribution to the final grade
    public Weight weight() {
        return weight;
    }

    // Criteria are equal when they have the same id
    @Override
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Criterion criterion)) {
            return false;
        }
        return id == criterion.id;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }
}
