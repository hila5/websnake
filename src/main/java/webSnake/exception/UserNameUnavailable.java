package webSnake.exception;

public class UserNameUnavailable extends RuntimeException {

    public UserNameUnavailable(String message) {
        super(message);
    }

}