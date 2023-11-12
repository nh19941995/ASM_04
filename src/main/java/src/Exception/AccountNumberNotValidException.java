package src.Exception;

public class AccountNumberNotValidException extends RuntimeException{

    public AccountNumberNotValidException() {
        super();
    }

    public AccountNumberNotValidException(String message) {
        super(message);
    }

    public AccountNumberNotValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountNumberNotValidException(Throwable cause) {
        super(cause);
    }

}
