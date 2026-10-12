package vyshkat.exception;

// Reports invalid data
public class DomainValidationException extends RuntimeException {

    // Creates the exception
    public DomainValidationException(final String message) {
        super(message);
    }
}
