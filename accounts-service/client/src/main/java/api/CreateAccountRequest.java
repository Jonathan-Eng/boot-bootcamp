package api;

public class CreateAccountRequest {

    private String accountName;

    public CreateAccountRequest() {}

    public CreateAccountRequest(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountName() {
        return accountName;
    }
}