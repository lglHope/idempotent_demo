package liu.hope.idempotent_demo.exception;

public class IdempotentException extends RuntimeException {

    public IdempotentException(String message) {
        super(message);
    }

}