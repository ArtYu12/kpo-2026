package pos.infrastructure.console;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pos.application.GradingFacade;
import pos.domain.model.EvaluationFactory;
import pos.domain.service.CompletedCriteriaStrategy;
import pos.domain.service.FinalGradeCalculator;
import pos.domain.service.WeightedSumStrategy;
import pos.infrastructure.export.CsvResultExporter;
import pos.infrastructure.persistence.InMemoryEvaluationRepository;
import pos.infrastructure.persistence.InMemoryRubricRepository;
import pos.infrastructure.persistence.InMemoryStudentRepository;

@DisplayName("Console menu")
class ConsoleRunnerTest {

    @Test
    @DisplayName("User adds data and sees the final grade")
    void showsFinalGrade() {
        var output = run(String.join(
                "\n",
                "1", "Ivan Petrov", "BSE245",
                "4", "Lectures", "10", "0,5",
                "7", "1", "1", "9", "",
                "11",
                "0"
        ));

        assertTrue(output.contains("Student added with id 1"));
        assertTrue(output.contains("Criterion added with id 1"));
        assertTrue(output.contains("Evaluation saved with id 1"));
        assertTrue(output.contains("Ivan Petrov, group BSE245: 4.50 -> 5"));
        assertTrue(output.contains("Goodbye"));
    }

    @Test
    @DisplayName("Errors are printed and the menu keeps working")
    void printsErrors() {
        var output = run(String.join(
                "\n",
                "4", "Lectures", "10", "0.5",
                "1", "Ivan Petrov", "BSE245",
                "7", "1", "1", "12", "",
                "7", "1", "abc",
                "99",
                "0"
        ));

        assertTrue(output.contains("Error: Score must be from 0 to 10.0"));
        assertTrue(output.contains("Error: a number was expected"));
        assertTrue(output.contains("Unknown command"));
        assertTrue(output.contains("Goodbye"));
    }

    private static String run(final String input) {
        var facade = new GradingFacade(
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
        var bytes = new ByteArrayOutputStream();
        var runner = new ConsoleRunner(
                facade,
                new ByteArrayInputStream(
                        input.getBytes(StandardCharsets.UTF_8)
                ),
                new PrintStream(bytes, true, StandardCharsets.UTF_8)
        );

        runner.run();

        return bytes.toString(StandardCharsets.UTF_8);
    }
}
