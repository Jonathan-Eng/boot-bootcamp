package jettyjersey;

import api.AccountInvalidNameException;
import api.AccountNameAlreadyExistsException;
import api.AccountsServiceApi;
import client.LogsShipperClient;
import config.ConfigFileFinder;
import config.ConfigurationFactory;
import config.ServerConfiguration;
import org.apache.commons.lang3.RandomStringUtils;
import org.awaitility.core.ConditionTimeoutException;
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
    private static final int ACCOUNT_RAND_HIGH = 10000;



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

    /**
     * Creates two accounts: account1 and account2. Checks if account1 can read and write to account2.
     */
    @Test
    public void testAccountCanReadAndWriteExclusivelyToItself() {
        Account account1 = createAccount("account" + genRandAccountNum());
        Account account2 = createAccount("account" + genRandAccountNum());

        String userAgentHeader1 = "Mozilla/5.0 (Macintosh; Intel Mac OS X)";
        String userAgentHeader2 = "Other header";

        String msg1 = writeToEs(account1.getToken(), userAgentHeader1);
        String msg2 = writeToEs(account2.getToken(), userAgentHeader2);

        assertTrue(isMsgExistsInAccount(msg1, account1.getToken(), userAgentHeader1)); // can find msg1 in account 1
        assertFalse(isMsgExistsInAccount(msg1, account2.getToken(), userAgentHeader2));  // can't find msg1 in account 2
    }

    /**
     * tests index and search. generates a random String and looks for it in ES
     */
    @Test
    public void testIndexAndSearch() {

        // create an account
        int rand = genRandAccountNum();
        String accountName = "account" + rand;

        Account account = createAccount(accountName);
        String userAgentHeader = "Mozilla/5.0 (Macintosh; Intel Mac OS X)";
        String msg = writeToEs(account.getToken(), userAgentHeader);

        assertTrue(isMsgExistsInAccount(msg, account.getToken(), userAgentHeader));
    }

    private boolean isMsgExistsInAccount(String msg, String token, String userAgentHeader) {
        try {
            await().atMost(MAX_SEC_TO_WAIT, TimeUnit.SECONDS).untilAsserted(() -> isDocumentIndexed(token, msg, userAgentHeader));
        } catch (ConditionTimeoutException e) {
            return false;
        }
        return true;
    }

    private String writeToEs(String token, String userAgentHeader) {
        String msg = genRandomString();
        String msgJson = toJsonMsg(msg);

        Response response = handler.indexRequestWithCustomMessage(token, msgJson, userAgentHeader);
        assertNotNull(response);
        assertEquals(response.getStatus(), HttpURLConnection.HTTP_OK);

        return msg;
    }

    private void isDocumentIndexed(String token, String randomString, String userAgentHeader) {

        Response response = handler.searchRequestWithCustomMessage(token, randomString, userAgentHeader);

        DocsResponse dr = response.readEntity(DocsResponse.class);
        assertNotNull(dr);

        boolean isMessageIndexed = response.getStatus() == HttpURLConnection.HTTP_OK;
        boolean isMessageFoundOnce = 1 == dr.getResponse().size();

        assertTrue(isMessageIndexed);
        assertTrue(isMessageFoundOnce);
    }

    private static int genRandAccountNum() {
        return new Random().nextInt(ACCOUNT_RAND_HIGH - ACCOUNT_RAND_LOW) + ACCOUNT_RAND_LOW;
    }

    private static String genRandomString() {
        return RandomStringUtils.randomAlphabetic(RANDOM_STRING_LEN);
    }

    private static String toJsonMsg(String msg) {
        return "{\"message\":\"" + msg + "\"}";
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
}
