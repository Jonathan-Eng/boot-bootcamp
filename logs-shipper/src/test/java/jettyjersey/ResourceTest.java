package jettyjersey;

import config.ConfigFileFinder;
import config.ConfigurationFactory;
import config.ServerConfiguration;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;

import static org.junit.Assert.*;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.net.HttpURLConnection;

public class ResourceTest {

    private static final String CONFIG_FILE_NAME = "server.config";
    private final static int RES_NUM = 1;
    private final ServerConfiguration sc;

    public ResourceTest() {
        sc = ConfigurationFactory.create(
                ConfigFileFinder.findRealPath(CONFIG_FILE_NAME),
                ServerConfiguration.class);

    }


    public WebTarget getWebTarget() {
        return ClientBuilder.newClient().target(sc.getHost() + ":" + sc.getPort());
    }


    /**
     * tests index and search. generates a random String and looks for it in ES
     */
    @Test
    public void testIndexAndSearch () throws InterruptedException {

        // user agent header
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X)";

        // generate random String
        String random = RandomStringUtils.randomAlphabetic(20);

        // adapt to json format
        String jsonObjectAsString = "{\"message\":\"" + random + "\"}";

        // index to es
        getWebTarget().path("index")
            .request(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.USER_AGENT, userAgent)
            .post(Entity.json(jsonObjectAsString));

        // wait
        Thread.sleep(5000);

        // check if you find string
        Response response = getWebTarget().path("search")
                .queryParam("message", random)
                .queryParam("User-Agent", "Macintosh")
                .request(MediaType.APPLICATION_JSON) // I want in return to get text
                .get(Response.class);   // convert the json response to Response class

        DocsResponse dr = response.readEntity(DocsResponse.class);  // read entity as a DocsResponse class
        assertEquals(RES_NUM, dr.getResponse().size() );
        assertEquals(dr.getResponse().get(0).get("message"), random);

    }

    /**
     * test sending data to ES
     */
    @Test
    public void testSendDataToEs() {
        String jsonObjectAsString = "{\"message\":\"boot camp first index\"}";
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X)";
        Response response = getWebTarget().path("index")
                .request(MediaType.TEXT_PLAIN)
                .header(HttpHeaders.USER_AGENT, userAgent)
                .post(Entity.json(jsonObjectAsString));

        assertNotNull(response);
        assertTrue(response.getStatus() == HttpURLConnection.HTTP_OK);
    }

    /**
     * test search data in ES
     */
    @Test
    public void testSearchDataFromEs() {

        Response response = getWebTarget().path("search")
                .queryParam("message", "camp")
                .queryParam("User-Agent", "Macintosh")
                .request(MediaType.TEXT_PLAIN) // I want in return to get text
                .get(Response.class);   // convert the json response to Response class

        assertNotNull(response);
        assertEquals(response.getStatus(), HttpURLConnection.HTTP_OK);
    }

}
