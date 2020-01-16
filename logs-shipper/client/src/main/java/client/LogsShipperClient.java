package client;

import globals.accounts.AccountGlobals;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class LogsShipperClient {
    private static final String REST_API_URI = "http://localhost:8000/";

    private WebTarget webTarget;

    public LogsShipperClient() {
        this.webTarget = ClientBuilder.newClient().target(REST_API_URI);
    }

    /**
     * Run a POST request with a custom message. We index the new message into the
     * elastic search using our custom message
     *
     * @param message - the custom message that we index, along with a static user-agent.
     * @return
     */
    public Response indexRequestWithCustomMessage(String token, String message, String userAgent) {

        return webTarget.path("index/" + token)
                .request(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .post(Entity.json(message));
    }


    /**
     * Run a GET request with a custom message. We search for patterns in
     * elastic search with the delivered message.
     *
     * @param message - the custom message that we wish to search for
     * @return
     */
    public Response searchRequestWithCustomMessage(String token, String message, String userAgentHeader) {

        return webTarget.path("search")
                .queryParam("message", message)
                .queryParam("User-Agent",userAgentHeader)
                .request(MediaType.APPLICATION_JSON)
                .header(AccountGlobals.ACCOUNT_X_TOKEN, token)
                .get(Response.class);
    }
}
