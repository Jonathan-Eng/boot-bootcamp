package globals.accounts;

import pattern.PatternValidator;
import pojos.Account;

import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;

public class AccountResponses {
    public static Response tokenOkResponse(Account account) {
        return Response.ok().entity(account).build();
    }
    public static Response invalidTokenResponse() {
        return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("Invalid token pattern").build();
    }
    public static Response unauthorizedTokenResponse() {
        return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity("Unauthorized token").build();
    }
}
