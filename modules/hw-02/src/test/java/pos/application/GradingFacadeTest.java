package pos.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Clock;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pos.domain.exception.DomainValidationException;
import pos.domain.model.EvaluationFactory;
import pos.domain.service.CompletedCriteriaStrategy;
import pos.domain.service.FinalGradeCalculator;
import pos.domain.service.WeightedSumStrategy;
import pos.infrastructure.export.CsvResultExporter;
import pos.infrastructure.persistence.InMemoryEvaluationRepository;
import pos.infrastructure.persistence.InMemoryRubricRepository;
import pos.infrastructure.persistence.InMemoryStudentRepository;

@DisplayName("Grading facade")
class GradingFacadeTest {

    private GradingFacade facade;
    private int studentId;
    private int lecturesId;
    private int homeworkId;

    @BeforeEach
    void setUp() {
        facade = new GradingFacade(
                new InMemoryStudentRepository(),
                new InMemoryRubricRepository(),
                new InMemoryEvaluationRepository(),
                new EvaluationFactory(Clock.systemUTC()),
                new FinalGradeCalculator(),
                List.of(
                        new WeightedSumStrategy(),
                        new CompletedCriteriaStrategy()
                ),
                List.of(new CsvResultExporter())
        );
        studentId = facade.addStudent("Ivan Petrov", "BSE245").id();
        lecturesId = facade.addCriterion("Lectures", 10, 0.5).id();
        homeworkId = facade.addCriterion("Homework", 10, 0.5).id();
    }

    @Test
    @DisplayName("Result follows the current evaluations")
    void resultFollowsEvaluations() {
        var evaluation = facade.evaluate(studentId, lecturesId, 10, "");
        facade.evaluate(studentId, homeworkId, 6, "");
        assertEquals(8.0, facade.results().get(0).grade(), 0.0001);

        facade.correctEvaluation(evaluation.id(), 4, "appeal");
        assertEquals(5.0, facade.results().get(0).grade(), 0.0001);

        facade.cancelEvaluation(evaluation.id());
        assertEquals(3.0, facade.results().get(0).grade(), 0.0001);
    }

    @Test
    @DisplayName("Second evaluation for the same criterion is rejected")
    void rejectsSecondEvaluation() {
        facade.evaluate(studentId, lecturesId, 10, "");

        assertThrows(
                DomainValidationException.class,
                () -> facade.evaluate(studentId, lecturesId, 5, "")
        );
        assertEquals(1, facade.evaluations().size());
    }

    @Test
    @DisplayName("Unknown student can not be evaluated")
    void rejectsUnknownStudent() {
        assertThrows(
                DomainValidationException.class,
                () -> facade.evaluate(42, lecturesId, 5, "")
        );
    }

    @Test
    @DisplayName("Criterion with evaluations can not be removed")
    void keepsEvaluatedCriterion() {
        facade.evaluate(studentId, lecturesId, 10, "");

        assertThrows(
                DomainValidationException.class,
                () -> facade.removeCriterion(lecturesId)
        );
        facade.removeCriterion(homeworkId);
        assertEquals(1, facade.criteria().size());
    }

    @Test
    @DisplayName("Removing a student removes the student's evaluations")
    void removesStudentWithEvaluations() {
        facade.evaluate(studentId, lecturesId, 10, "");

        facade.removeStudent(studentId);

        assertTrue(facade.students().isEmpty());
        assertTrue(facade.evaluations().isEmpty());
    }

    @Test
    @DisplayName("Grading rule can be changed")
    void changesGradingRule() {
        facade.evaluate(studentId, lecturesId, 6, "");
        assertEquals(3.0, facade.results().get(0).grade(), 0.0001);

        facade.selectStrategy(2);

        assertEquals(6.0, facade.results().get(0).grade(), 0.0001);
        assertEquals(
                new CompletedCriteriaStrategy().name(),
                facade.currentStrategyName()
        );
        assertThrows(
                DomainValidationException.class,
                () -> facade.selectStrategy(3)
        );
    }

    @Test
    @DisplayName("Unknown export format is rejected")
    void rejectsUnknownFormat() {
        assertEquals(List.of("csv"), facade.exportFormats());
        assertThrows(
                DomainValidationException.class,
                () -> facade.exportResults("xml", "results.xml")
        );
    }
}
