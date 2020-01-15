package jettyjersey;

import api.AccountInvalidNameException;
import api.AccountNameAlreadyExistsException;
import api.AccountTokenUnauthorizedException;
import api.AccountsServiceApi;
import client.LogsShipperClient;
import config.ConfigFileFinder;
import config.ConfigurationFactory;
import config.ServerConfiguration;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import pojos.Account;

import static org.junit.Assert.*;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import static org.awaitility.Awaitility.await;

public class ResourceTest {

    private static final String CONFIG_FILE_NAME = "server.config";
    private static final int RANDOM_STRING_LEN = 20;
    private static final int MAX_SEC_TO_WAIT = 15;
    private static final int ACCOUNT_RAND_LOW = 1;
    private static final int ACCOUNT_RAND_HIGH = 1000;


    private final ServerConfiguration sc;
    private LogsShipperClient handler;
    private AccountsServiceApi accountServiceApi;


    public ResourceTest() {
        sc = ConfigurationFactory.create(
                ConfigFileFinder.findRealPath(CONFIG_FILE_NAME),
                ServerConfiguration.class);
        handler = new LogsShipperClient();
        accountServiceApi = new AccountsServiceApi("http", "localhost", 8888);
    }

    /**createTwoAccountsAndSearch
     * tests index and search. generates a random String and looks for it in ES
     */
    @Test
    public void testIndexAndSearch() {

        // create an account
        int rand = new Random().nextInt(ACCOUNT_RAND_HIGH - ACCOUNT_RAND_LOW) + ACCOUNT_RAND_LOW;
        String accountName = "account" + rand;

        Account account = createAccount(accountName);
        String userAgentHeader = "Mozilla/5.0 (Macintosh; Intel Mac OS X)";
        String randomString = genRandomString();
        String msgInJsonFormat = "{\"message\":\"" + randomString + "\"}";

        Response response = handler.indexRequestWithCustomMessage(account.getToken(), msgInJsonFormat, userAgentHeader);

        assertNotNull(response);
        assertEquals(response.getStatus(), HttpURLConnection.HTTP_OK);

        // search for string at most 15 sec. if not found within this time await throws an exception
        await().atMost(MAX_SEC_TO_WAIT, TimeUnit.SECONDS).until(() -> isDocumentIndexed(account.getToken(), randomString, userAgentHeader));
    }

    private static String genRandomString() {
        return RandomStringUtils.randomAlphabetic(RANDOM_STRING_LEN);
    }

    private Account createAccount(String accountName) {
        try {
            return accountServiceApi.createAccount(accountName);   // test fails if we get an exception
        }
        catch (AccountNameAlreadyExistsException e) {
            fail("test failed. Account name already exists");
        }
        catch (AccountInvalidNameException e) {
            fail("test failed. Account name is invalid");
        }
        throw new RuntimeException("test failed due to unexpected exception.");
    }

    private boolean isDocumentIndexed(String token, String randomString, String userAgentHeader) {

        Response response = handler.searchRequestWithCustomMessage(token, randomString, userAgentHeader);

        DocsResponse dr = response.readEntity(DocsResponse.class);

        boolean isMessageIndexed = response.getStatus() == HttpURLConnection.HTTP_OK;
        boolean isMessageFoundOnce = 1 == dr.getResponse().size();

        return isMessageIndexed && isMessageFoundOnce;
    }
}
