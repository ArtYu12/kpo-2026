package pos.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.Clock;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pos.domain.exception.DomainValidationException;
import pos.domain.model.Criterion;
import pos.domain.model.Evaluation;
import pos.domain.model.EvaluationFactory;
import pos.domain.model.FinalGrade;
import pos.domain.model.Rubric;
import pos.domain.model.Student;

@DisplayName("Final grade calculator")
class FinalGradeCalculatorTest {

    private final FinalGradeCalculator calculator = new FinalGradeCalculator();
    private final EvaluationFactory factory =
            new EvaluationFactory(Clock.systemUTC());

    private Rubric rubric;
    private Criterion lectures;
    private Criterion homework;
    private Student student;
    private int lastId;

    @BeforeEach
    void setUp() {
        rubric = new Rubric();
        lectures = rubric.addCriterion("Lectures", 10, 0.4);
        homework = rubric.addCriterion("Homework", 5, 0.6);
        student = new Student(1, "Ivan Petrov", "BSE245");
        lastId = 0;
    }

    @Test
    @DisplayName("Weighted sum uses weights of all criteria")
    void calculatesWeightedSum() {
        var evaluations = List.of(
                evaluate(student, lectures, 5),
                evaluate(student, homework, 5)
        );

        var grade = calculator.calculate(
                student,
                rubric,
                evaluations,
                new WeightedSumStrategy()
        );

        // 0.4 * 0.5 + 0.6 * 1.0 = 0.8
        assertEquals(8.0, grade.value(), 0.0001);
        assertEquals(8, grade.rounded());
    }

    @Test
    @DisplayName("Missing criterion gives 0 in weighted sum")
    void missingCriterionGivesZero() {
        var evaluations = List.of(evaluate(student, homework, 5));

        var grade = calculator.calculate(
                student,
                rubric,
                evaluations,
                new WeightedSumStrategy()
        );

        assertEquals(6.0, grade.value(), 0.0001);
    }

    @Test
    @DisplayName("Current grade uses only evaluated criteria")
    void calculatesCurrentGrade() {
        var evaluations = List.of(evaluate(student, lectures, 7));

        var grade = calculator.calculate(
                student,
                rubric,
                evaluations,
                new CompletedCriteriaStrategy()
        );

        assertEquals(7.0, grade.value(), 0.0001);
    }

    @Test
    @DisplayName("Student without evaluations gets 0")
    void noEvaluationsGiveZero() {
        var grade = calculator.calculate(
                student,
                rubric,
                List.of(),
                new CompletedCriteriaStrategy()
        );

        assertEquals(0.0, grade.value(), 0.0001);
    }

    @Test
    @DisplayName("Evaluation of another student is rejected")
    void rejectsEvaluationOfAnotherStudent() {
        var other = new Student(2, "Anna", "BSE245");
        var evaluations = List.of(evaluate(other, lectures, 7));

        assertThrows(
                DomainValidationException.class,
                () -> calculator.calculate(
                        student,
                        rubric,
                        evaluations,
                        new WeightedSumStrategy()
                )
        );
    }

    @Test
    @DisplayName("Evaluation of a removed criterion is rejected")
    void rejectsCriterionNotInRubric() {
        var evaluations = List.of(evaluate(student, lectures, 7));
        rubric.removeCriterion(lectures.id());

        assertThrows(
                DomainValidationException.class,
                () -> calculator.calculate(
                        student,
                        rubric,
                        evaluations,
                        new WeightedSumStrategy()
                )
        );
    }

    @Test
    @DisplayName("Two evaluations for one criterion are rejected")
    void rejectsDuplicateCriterion() {
        var evaluations = List.of(
                evaluate(student, lectures, 7),
                evaluate(student, lectures, 9)
        );

        assertThrows(
                DomainValidationException.class,
                () -> calculator.calculate(
                        student,
                        rubric,
                        evaluations,
                        new WeightedSumStrategy()
                )
        );
    }

    @Test
    @DisplayName("Grade 5.5 is rounded to 6")
    void roundsHalfUp() {
        assertEquals(6, new FinalGrade(5.5).rounded());
        assertEquals(5, new FinalGrade(5.49).rounded());
    }

    private Evaluation evaluate(
            final Student owner,
            final Criterion criterion,
            final double value
    ) {
        lastId++;
        return factory.create(
                lastId,
                owner.id(),
                rubric,
                criterion.id(),
                value,
                ""
        );
    }
}
