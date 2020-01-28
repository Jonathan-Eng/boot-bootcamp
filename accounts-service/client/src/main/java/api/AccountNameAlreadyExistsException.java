package api;

public class AccountNameAlreadyExistsException extends Exception {

    public AccountNameAlreadyExistsException(String msg) {
        super(msg);
    }
}
