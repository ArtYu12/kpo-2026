package pos.domain.exception;

// Reports that a domain rule was violated
public class DomainValidationException extends RuntimeException {

    // Creates the exception
    public DomainValidationException(final String message) {
        super(message);
    }
}
