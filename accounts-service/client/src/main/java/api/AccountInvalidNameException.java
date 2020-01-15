package api;

public class InvalidAccountNameException extends Throwable {
    public InvalidAccountNameException(String msg) {
        super(msg);
    }
}
