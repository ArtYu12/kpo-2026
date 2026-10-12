package vyshkat.console;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vyshkat.service.DefaultServiceCenter;
import vyshkat.service.FleetService;

@DisplayName("Command line menu")
class ConsoleRunnerTest {

    @Test
    @DisplayName("Menu runs all fleet scenarios")
    void runsAllFleetScenarios() {
        String input = String.join("\n",
                "1", "1", "TR-001", "8", "yes", "1.4",
                "1", "2", "TR-002", "5", "yes",
                "1", "3", "TR-003", "7", "no", "0.9",
                "2", "1", "EQ-001",
                "2", "2", "EQ-002", "4.5",
                "3", "4", "5", "6", "0"
        );

        String result = runConsole(input);

        assertTrue(result.contains("Transport accepted."));
        assertTrue(result.contains("Transport failed inspection."));
        assertTrue(result.contains("Equipment added."));
        assertTrue(result.contains("Transport: 2"));
        assertTrue(result.contains("Equipment: 2"));
        assertTrue(result.contains("Total items: 4"));
        assertTrue(result.contains("Scooter - item # TR-001"));
        assertTrue(result.contains("Bicycle - item # TR-002"));
        assertTrue(result.contains("Helmet - item # EQ-001"));
        assertTrue(result.contains("Charging station - item # EQ-002"));
        assertTrue(result.contains("Beginner transport"));
        assertTrue(result.contains("Goodbye"));
    }

    @Test
    @DisplayName("Menu reports invalid input")
    void reportsInvalidInput() {
        String input = String.join("\n",
                "9",
                "1", "9", "TR-001", "5", "yes",
                "2", "9", "EQ-001",
                "1", "2", "TR-002", "5", "maybe",
                "1", "2", "TR-003", "wrong",
                "1", "2", "DUP-001", "5", "yes",
                "2", "1", "DUP-001"
        );

        String result = runConsole(input);

        assertTrue(result.contains("Unknown command"));
        assertTrue(result.contains("Error: Unknown transport type"));
        assertTrue(result.contains("Error: Unknown equipment type"));
        assertTrue(result.contains("Error: Enter yes or no"));
        assertTrue(result.contains("Error: For input string"));
        assertTrue(result.contains("Error: Inventory number already exists"));
    }

    private String runConsole(final String commands) {
        var input = new ByteArrayInputStream(
                commands.getBytes(StandardCharsets.UTF_8)
        );
        var bytes = new ByteArrayOutputStream();
        var output = new PrintStream(bytes, true, StandardCharsets.UTF_8);
        var fleetService = new FleetService(new DefaultServiceCenter());
        var runner = new ConsoleRunner(fleetService, input, output);

        runner.run();

        return bytes.toString(StandardCharsets.UTF_8);
    }
}
