package pos.infrastructure.export;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import pos.application.ExportException;
import pos.application.StudentResult;

@DisplayName("Result exporters")
class ResultExporterTest {

    private static final List<StudentResult> RESULTS = List.of(
            new StudentResult(1, "Ivan Petrov", "BSE245", 8.456, 8, "Sum"),
            new StudentResult(2, "Anna \"A\"", "BSE246", 5.5, 6, "Sum")
    );

    @Test
    @DisplayName("CSV has a header and one row per student")
    void writesCsv(@TempDir final Path folder) throws IOException {
        var file = folder.resolve("results.csv");

        new CsvResultExporter().export(RESULTS, file);

        var lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        assertEquals(3, lines.size());
        assertEquals(
                "student_id;full_name;group;grade;rounded_grade;rule",
                lines.get(0)
        );
        assertEquals(
                "1;\"Ivan Petrov\";\"BSE245\";8.46;8;\"Sum\"",
                lines.get(1)
        );
        assertEquals(
                "2;\"Anna \"\"A\"\"\";\"BSE246\";5.50;6;\"Sum\"",
                lines.get(2)
        );
    }

    @Test
    @DisplayName("JSON is an array with escaped strings")
    void writesJson(@TempDir final Path folder) throws IOException {
        var file = folder.resolve("results.json");

        new JsonResultExporter().export(RESULTS, file);

        var text = Files.readString(file, StandardCharsets.UTF_8);
        assertEquals(
                "[\n"
                        + "  {\"studentId\": 1, \"fullName\": \"Ivan Petrov\","
                        + " \"group\": \"BSE245\", \"grade\": 8.46,"
                        + " \"roundedGrade\": 8, \"rule\": \"Sum\"},\n"
                        + "  {\"studentId\": 2,"
                        + " \"fullName\": \"Anna \\\"A\\\"\","
                        + " \"group\": \"BSE246\", \"grade\": 5.50,"
                        + " \"roundedGrade\": 6, \"rule\": \"Sum\"}\n"
                        + "]\n",
                text.replace(System.lineSeparator(), "\n")
        );
    }

    @Test
    @DisplayName("Missing folder is created")
    void createsFolder(@TempDir final Path folder) {
        var file = folder.resolve("results").resolve("grades.csv");

        new CsvResultExporter().export(RESULTS, file);

        assertTrue(Files.exists(file));
    }

    @Test
    @DisplayName("Folder instead of a file gives an export error")
    void reportsWriteError(@TempDir final Path folder) {
        assertThrows(
                ExportException.class,
                () -> new CsvResultExporter().export(RESULTS, folder)
        );
    }
}
