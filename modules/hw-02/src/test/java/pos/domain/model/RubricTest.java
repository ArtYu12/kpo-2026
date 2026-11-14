package pos.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pos.domain.exception.DomainValidationException;

@DisplayName("Rubric")
class RubricTest {

    @Test
    @DisplayName("Criterion is added with a new id")
    void addsCriterion() {
        var rubric = new Rubric();

        var first = rubric.addCriterion("Lectures", 10, 0.3);
        var second = rubric.addCriterion("Seminars", 5, 0.2);

        assertEquals(1, first.id());
        assertEquals(2, second.id());
        assertEquals(2, rubric.criteria().size());
        assertEquals(0.5, rubric.totalWeight(), 0.0001);
    }

    @Test
    @DisplayName("Criterion names are unique")
    void rejectsDuplicateName() {
        var rubric = new Rubric();
        rubric.addCriterion("Lectures", 10, 0.3);

        assertThrows(
                DomainValidationException.class,
                () -> rubric.addCriterion("lectures", 5, 0.2)
        );
        assertEquals(1, rubric.criteria().size());
    }

    @Test
    @DisplayName("Total weight can not be greater than 1")
    void rejectsTooBigTotalWeight() {
        var rubric = new Rubric();
        rubric.addCriterion("Lectures", 10, 0.7);

        assertThrows(
                DomainValidationException.class,
                () -> rubric.addCriterion("Seminars", 10, 0.4)
        );
    }

    @Test
    @DisplayName("Weights 0.1 * 10 give exactly 1")
    void acceptsTotalWeightOne() {
        var rubric = new Rubric();
        for (int i = 0; i < 10; i++) {
            rubric.addCriterion("Criterion " + i, 10, 0.1);
        }

        assertEquals(10, rubric.criteria().size());
    }

    @ParameterizedTest(name = "Weight {0} is rejected")
    @ValueSource(doubles = {-0.5, 0, 1.01, 5})
    @DisplayName("Weight must be from 0 (exclusive) to 1")
    void weightMustBeInRange(final double weight) {
        var rubric = new Rubric();

        assertThrows(
                DomainValidationException.class,
                () -> rubric.addCriterion("Lectures", 10, weight)
        );
    }

    @ParameterizedTest(name = "Max score {0} is rejected")
    @ValueSource(doubles = {-1, 0})
    @DisplayName("Max score must be positive")
    void maxScoreMustBePositive(final double maxScore) {
        var rubric = new Rubric();

        assertThrows(
                DomainValidationException.class,
                () -> rubric.addCriterion("Lectures", maxScore, 0.5)
        );
    }

    @Test
    @DisplayName("Unknown criterion is not found")
    void unknownCriterionIsNotFound() {
        var rubric = new Rubric();

        assertFalse(rubric.contains(1));
        assertThrows(
                DomainValidationException.class,
                () -> rubric.criterion(1)
        );
    }

    @Test
    @DisplayName("Removed criterion is not in the rubric")
    void removesCriterion() {
        var rubric = new Rubric();
        var criterion = rubric.addCriterion("Lectures", 10, 0.3);
        assertTrue(rubric.contains(criterion.id()));

        rubric.removeCriterion(criterion.id());

        assertFalse(rubric.contains(criterion.id()));
        assertEquals(0, rubric.totalWeight(), 0.0001);
    }
}
