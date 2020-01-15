package util;

import pattern.PatternValidator;
import pojos.Account;

import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;

public class AccountResponses {
    public static Response tokenOkResponse(Account account) {
        return Response.ok().entity(account).build();
    }
    public static Response invalidTokenResponse(String token) {
        return Response.status(HttpURLConnection.HTTP_BAD_REQUEST).entity("Invalid token pattern for token " + token).build();
    }
    public static Response unauthorizedTokenResponse(String token) {
        return Response.status(HttpURLConnection.HTTP_UNAUTHORIZED).entity("Unauthorized token " + token).build();
    }
    public static Response invalidAccountName(String accountName) {
        String badRequestMsg = String.format("Account name %s does not abide by the account name pattern\n", accountName);
        return Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                .entity(badRequestMsg).build();
    }
    public static Response accountNameAlreadyExists(String accountName) {
        String badRequestMsg = String.format("Account name %s already exists\n", accountName);
        return Response.status(HttpURLConnection.HTTP_CONFLICT)
                .entity(badRequestMsg).build();
    }

}
