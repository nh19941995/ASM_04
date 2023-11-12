package src.Exception;

public class CustomerIdNotValidException extends RuntimeException{

    public CustomerIdNotValidException() {
        super();
    }

    public CustomerIdNotValidException(String message) {
        super(message);
    }

    public CustomerIdNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public CustomerIdNotValidException(Throwable cause) {
        super(cause);
    }
}
