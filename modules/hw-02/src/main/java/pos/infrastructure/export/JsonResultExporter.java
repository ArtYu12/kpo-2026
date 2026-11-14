package pos.infrastructure.export;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Locale;
import org.springframework.stereotype.Component;
import pos.application.StudentResult;

// Writes results as a JSON array
@Component
public final class JsonResultExporter extends FileResultExporter {

    private static final String INDENT = "  ";

    @Override
    public String format() {
        return "json";
    }

    @Override
    protected void writeHeader(final BufferedWriter writer)
            throws IOException {
        writer.write("[");
        writer.newLine();
    }

    @Override
    protected void writeRow(
            final BufferedWriter writer,
            final StudentResult result,
            final int index
    ) throws IOException {
        if (index > 0) {
            writer.write(",");
            writer.newLine();
        }
        writer.write(INDENT + "{"
                + "\"studentId\": " + result.studentId()
                + ", \"fullName\": " + text(result.fullName())
                + ", \"group\": " + text(result.group())
                + ", \"grade\": "
                + String.format(Locale.ROOT, "%.2f", result.grade())
                + ", \"roundedGrade\": " + result.roundedGrade()
                + ", \"rule\": " + text(result.rule())
                + "}");
    }

    @Override
    protected void writeFooter(final BufferedWriter writer)
            throws IOException {
        writer.newLine();
        writer.write("]");
        writer.newLine();
    }

    // Makes a JSON string with escaped quotes
    private static String text(final String value) {
        String escaped = value
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
        return "\"" + escaped + "\"";
    }
}
