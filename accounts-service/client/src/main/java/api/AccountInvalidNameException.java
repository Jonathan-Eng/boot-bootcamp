package api;

public class AccountInvalidNameException extends Exception {
    public AccountInvalidNameException(String msg) {
        super(msg);
    }
}
