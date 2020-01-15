package api;

public class AccountInvalidTokenException extends Throwable {
    public AccountInvalidTokenException(String msg) {
        super(msg);
    }
}
