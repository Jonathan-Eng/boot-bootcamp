package accounts;

import pojos.Account;

import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;

public class AccountGlobals {
    public static final String ACCOUNT_TOKEN = "accountToken";

    private Response tokenOkResponse(Account account) {
        return Response.ok().entity(account).build();
    }

    private Response invalidTokenResponse() {
        return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("Invalid token pattern").build();
    }

    private Response unauthorizedTokenResponse() {
        return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity("Unauthorized token").build();
    }
}
