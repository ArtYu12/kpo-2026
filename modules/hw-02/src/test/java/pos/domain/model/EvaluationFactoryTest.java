package pos.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pos.domain.exception.DomainValidationException;

@DisplayName("Evaluation factory")
class EvaluationFactoryTest {

    private static final Instant NOW = Instant.parse("2026-11-07T10:00:00Z");

    private EvaluationFactory factory;
    private Rubric rubric;
    private Criterion lectures;
    private Criterion seminars;

    @BeforeEach
    void setUp() {
        factory = new EvaluationFactory(Clock.fixed(NOW, ZoneOffset.UTC));
        rubric = new Rubric();
        lectures = rubric.addCriterion("Lectures", 10, 0.5);
        seminars = rubric.addCriterion("Seminars", 5, 0.5);
    }

    @Test
    @DisplayName("Evaluation keeps all values")
    void createsEvaluation() {
        var evaluation = create(lectures.id(), 8);

        assertEquals(1, evaluation.id());
        assertEquals(1, evaluation.studentId());
        assertEquals(lectures.id(), evaluation.criterionId());
        assertEquals(8, evaluation.score().value(), 0.0001);
        assertEquals(10, evaluation.score().maxScore(), 0.0001);
        assertEquals(LocalDate.of(2026, 11, 7), evaluation.date());
        assertEquals("good", evaluation.comment());
    }

    @Test
    @DisplayName("12 of 10 can not be given")
    void rejectsScoreGreaterThanMax() {
        assertThrows(
                DomainValidationException.class,
                () -> create(lectures.id(), 12)
        );
    }

    @Test
    @DisplayName("Criterion must be in the rubric")
    void rejectsUnknownCriterion() {
        assertThrows(
                DomainValidationException.class,
                () -> create(99, 5)
        );
    }

    @Test
    @DisplayName("Correction is checked by the criterion range")
    void correctionKeepsRange() {
        var evaluation = create(seminars.id(), 3);

        assertThrows(
                DomainValidationException.class,
                () -> evaluation.correct(seminars, 6, "")
        );
        assertEquals(3, evaluation.score().value(), 0.0001);

        evaluation.correct(seminars, 5, "after appeal");
        assertEquals(5, evaluation.score().value(), 0.0001);
        assertEquals("after appeal", evaluation.comment());
    }

    @Test
    @DisplayName("Correction with another criterion is rejected")
    void correctionNeedsSameCriterion() {
        var evaluation = create(seminars.id(), 3);

        assertThrows(
                DomainValidationException.class,
                () -> evaluation.correct(lectures, 7, "")
        );
    }

    private Evaluation create(
            final int criterionId,
            final double value
    ) {
        return factory.create(
                1,
                1,
                rubric,
                criterionId,
                value,
                "good"
        );
    }
}
