package pos.infrastructure.export;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;
import org.springframework.stereotype.Component;
import pos.application.StudentResult;

// Writes results as a CSV table
@Component
public final class CsvResultExporter extends FileResultExporter {

    private static final String SEPARATOR = ";";
    private static final String QUOTE = "\"";
    private static final String HEADER =
            "student_id;full_name;group;grade;rounded_grade;rule";

    @Override
    public String format() {
        return "csv";
    }

    @Override
    protected void writeHeader(final BufferedWriter writer)
            throws IOException {
        writer.write(HEADER);
        writer.newLine();
    }

    @Override
    protected void writeRow(
            final BufferedWriter writer,
            final StudentResult result,
            final int index
    ) throws IOException {
        writer.write(String.join(
                SEPARATOR,
                String.valueOf(result.studentId()),
                quote(result.fullName()),
                quote(result.group()),
                String.format(Locale.ROOT, "%.2f", result.grade()),
                String.valueOf(result.roundedGrade()),
                quote(result.rule())
        ));
        writer.newLine();
    }

    // Quotes a text so that ; and " do not break the table
    private static String quote(final String text) {
        return QUOTE + text.replace(QUOTE, QUOTE + QUOTE) + QUOTE;
    }
}
