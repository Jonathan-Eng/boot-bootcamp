package jettyjersey;

import client.LogsShipperClient;
import config.ConfigFileFinder;
import config.ConfigurationFactory;
import config.ServerConfiguration;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;
import static org.awaitility.Awaitility.await;

public class ResourceTest {

    private static final String CONFIG_FILE_NAME = "server.config";
    private final ServerConfiguration sc;
    public LogsShipperClient handler;

    public ResourceTest() {
        sc = ConfigurationFactory.create(
                ConfigFileFinder.findRealPath(CONFIG_FILE_NAME),
                ServerConfiguration.class);
        handler = new LogsShipperClient();
    }


    /**
     * tests index and search. generates a random String and looks for it in ES
     */
    @Test
    public void testIndexAndSearch () throws InterruptedException {

        // user agent header
        String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X)";

        // generate random String
        String randomString = RandomStringUtils.randomAlphabetic(20);

        // adapt to json format
        String jsonObjectAsString = "{\"message\":\"" + randomString + "\"}";

        // index to es
        Response response = handler.indexRequestWithCustomMessage(jsonObjectAsString, userAgent);

        // assert if request went wrong
        assertNotNull(response);
        assertEquals(response.getStatus(), HttpURLConnection.HTTP_OK);

        // search for string at most 15 sec. if not found within this time await throws
        await().atMost(15, TimeUnit.SECONDS).until(() -> isDocumentIndexed(randomString, userAgent));

    }

    private boolean isDocumentIndexed(String randomString, String userAgentHeader) {
        Response response = handler.searchRequestWithCustomMessage(randomString, userAgentHeader);

        // read response as DocsResponse
        DocsResponse dr = response.readEntity(DocsResponse.class);

        boolean isMessageIndexed = response.getStatus() == HttpURLConnection.HTTP_OK;
        boolean isMessageFoundOnce = 1 == dr.getResponse().size();

        return isMessageIndexed && isMessageFoundOnce;
    }
}
