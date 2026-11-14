package pos.domain.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import pos.domain.exception.DomainValidationException;

// Rubric of the course, aggregate root for criteria
public final class Rubric {

    private static final double MAX_TOTAL_WEIGHT = 1.0;
    private static final double EPSILON = 1e-9;

    private final Map<Integer, Criterion> criteria = new LinkedHashMap<>();
    private int lastCriterionId = 0;

    // Adds a new criterion to the rubric
    public Criterion addCriterion(
            final String name,
            final double maxScore,
            final double weight
    ) {
        Weight newWeight = new Weight(weight);
        if (totalWeight() + newWeight.value()
                > MAX_TOTAL_WEIGHT + EPSILON) {
            throw new DomainValidationException(
                    "Total weight of the rubric can not be greater than 1"
            );
        }
        Criterion criterion = new Criterion(
                lastCriterionId + 1,
                name,
                maxScore,
                newWeight
        );
        ensureUniqueName(criterion.name());

        lastCriterionId++;
        criteria.put(criterion.id(), criterion);
        return criterion;
    }

    // Removes a criterion from the rubric
    public void removeCriterion(final int id) {
        criterion(id);
        criteria.remove(id);
    }

    // Finds a criterion of the rubric
    public Criterion criterion(final int id) {
        Criterion found = criteria.get(id);
        if (found == null) {
            throw new DomainValidationException(
                    "Criterion " + id + " is not in the rubric"
            );
        }
        return found;
    }

    // Checks if the rubric has a criterion
    public boolean contains(final int id) {
        return criteria.containsKey(id);
    }

    // Returns all criteria in order of adding
    public List<Criterion> criteria() {
        return new ArrayList<>(criteria.values());
    }

    // Returns sum of weights of all criteria
    public double totalWeight() {
        double total = 0;
        for (Criterion criterion : criteria.values()) {
            total += criterion.weight().value();
        }
        return total;
    }

    private void ensureUniqueName(final String name) {
        for (Criterion criterion : criteria.values()) {
            if (criterion.name().equalsIgnoreCase(name)) {
                throw new DomainValidationException(
                        "Criterion already exists: " + name
                );
            }
        }
    }
}
