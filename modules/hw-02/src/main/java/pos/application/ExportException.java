package pos.application;

// Reports that results could not be exported
public class ExportException extends RuntimeException {

    // Creates the exception
    public ExportException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
