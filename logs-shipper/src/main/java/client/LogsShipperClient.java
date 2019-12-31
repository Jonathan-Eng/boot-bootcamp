package client;

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
    public Response indexRequestWithCustomMessage(String message, String userAgent) {

        return webTarget.path("index")
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
    public Response searchRequestWithCustomMessage(String message, String userAgentHeader) {

        return webTarget.path("search")
                .queryParam("message", message)
                .queryParam("User-Agent",userAgentHeader)
                .request(MediaType.APPLICATION_JSON) // I want in return to get text
                .get(Response.class);   // convert the json response to Response class
    }
}
