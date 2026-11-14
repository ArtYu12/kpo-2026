package pos.application;

import java.nio.file.Path;
import java.util.List;

// Port for exporting final results to a file
public interface ResultExporter {

    // Returns file format, for example csv
    String format();

    // Writes results to a file
    void export(List<StudentResult> results, Path file);
}
