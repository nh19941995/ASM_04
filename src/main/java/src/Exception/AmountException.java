package src.Exception;

import java.util.ArrayList;
import java.util.List;

public class AmountException extends RuntimeException{

    public AmountException() {
        super();
    }

    public AmountException(String message) {
        super(message);
    }

    public AmountException(String message, Throwable cause) {
        super(message, cause);
    }

    public AmountException(Throwable cause) {
        super(cause);
    }

}
