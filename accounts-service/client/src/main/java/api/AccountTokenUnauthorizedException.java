package api;

public class AccountTokenUnauthorizedException extends Exception {
    public AccountTokenUnauthorizedException(String msg) {
        super(msg);
    }
}
