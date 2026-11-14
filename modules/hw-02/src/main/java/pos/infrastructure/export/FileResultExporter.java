package pos.infrastructure.export;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import pos.application.ExportException;
import pos.application.ResultExporter;
import pos.application.StudentResult;

// Template Method for export to a file
public abstract class FileResultExporter implements ResultExporter {

    @Override
    public final void export(
            final List<StudentResult> results,
            final Path file
    ) {
        BufferedWriter writer = null;
        try {
            createFolder(file);
            writer = Files.newBufferedWriter(file, StandardCharsets.UTF_8);
            writeHeader(writer);
            for (int i = 0; i < results.size(); i++) {
                writeRow(writer, results.get(i), i);
            }
            writeFooter(writer);
        } catch (IOException exception) {
            throw new ExportException("Could not write " + file, exception);
        } finally {
            closeWriter(writer, file);
        }
    }

    // Writes the beginning of the file
    protected abstract void writeHeader(BufferedWriter writer)
            throws IOException;

    // Writes one result
    protected abstract void writeRow(
            BufferedWriter writer,
            StudentResult result,
            int index
    ) throws IOException;

    // Writes the end of the file
    protected void writeFooter(final BufferedWriter writer)
            throws IOException {
    }

    private static void createFolder(final Path file) throws IOException {
        Path folder = file.getParent();
        if (folder != null) {
            Files.createDirectories(folder);
        }
    }

    private static void closeWriter(
            final BufferedWriter writer,
            final Path file
    ) {
        if (writer == null) {
            return;
        }
        try {
            writer.close();
        } catch (IOException exception) {
            throw new ExportException("Could not close " + file, exception);
        }
    }
}
