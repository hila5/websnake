package webSnake.exception;

public class NotYoursGameException extends RuntimeException {
    public NotYoursGameException(String message) {
        super(message);
    }
}
