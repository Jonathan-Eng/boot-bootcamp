import api.AccountInvalidNameException;
import api.AccountNameAlreadyExistsException;
import api.AccountsServiceApi;
import client.LogsShipperClient;
import jettyjersey.DocsResponse;
import jettyjersey.IndexBody;
import org.apache.commons.lang3.RandomStringUtils;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.Test;
import parser.JsonParser;
import pojos.Account;

import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class IndexAndQueryE2ETests {

    private static final int RANDOM_STRING_LEN = 20;
    private static final int MAX_SEC_TO_WAIT = 15;
    private static final int ACCOUNT_RAND_LOW = 1;
    private static final int ACCOUNT_RAND_HIGH = 10000;

    private LogsShipperClient logsShipperClient;
    private AccountsServiceApi accountServiceApi;


    public IndexAndQueryE2ETests() {
        logsShipperClient = new LogsShipperClient();
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

    @Test
    public void cantIndexWithNonExistingToken() {
        Account account = createAccount("account" + genRandAccountNum());
        String userAgentHeader = "Mozilla/5.0 (Macintosh; Intel Mac OS X)";
        String nonExistingToken = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

        assertTrue(canWriteToEs(account.getToken(), userAgentHeader));
        assertFalse(canWriteToEs(nonExistingToken, userAgentHeader));
    }

    @Test
    public void cantSearchWithNonExistingToken() {
        Account account = createAccount("account" + genRandAccountNum());
        String userAgentHeader = "Mozilla/5.0 (Macintosh; Intel Mac OS X)";
        String nonExistingToken = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
        String msg = writeToEs(account.getToken(), userAgentHeader);

        assertTrue(isMsgExistsInAccount(msg, account.getToken(), userAgentHeader));
        assertFalse(isMsgExistsInAccount(msg, nonExistingToken, userAgentHeader));
    }

    private boolean isMsgExistsInAccount(String msg, String token, String userAgentHeader) {
        try {
            await().atMost(MAX_SEC_TO_WAIT, TimeUnit.SECONDS).untilAsserted(() -> assertDocumentIndexed(token, msg, userAgentHeader));
        } catch (ConditionTimeoutException e) {
            return false;
        }
        return true;
    }

    private String writeToEs(String token, String userAgentHeader) {
        String msg = genRandomString();
        String msgJson = toJsonMsg(msg);

        Response response = logsShipperClient.indexRequestWithCustomMessage(token, msgJson, userAgentHeader);
        assertNotNull(response);
        assertEquals(response.getStatus(), HttpURLConnection.HTTP_OK);

        return msg;
    }

    private boolean canWriteToEs(String token, String userAgentHeader) {
        String msg = genRandomString();
        String msgJson = toJsonMsg(msg);

        Response response = logsShipperClient.indexRequestWithCustomMessage(token, msgJson, userAgentHeader);

        return response != null && response.getStatus() == HttpURLConnection.HTTP_OK;
    }

    private void assertDocumentIndexed(String token, String randomString, String userAgentHeader) {

        Response response = logsShipperClient.searchRequestWithCustomMessage(token, randomString, userAgentHeader);
        boolean isMessageIndexed = response.getStatus() == HttpURLConnection.HTTP_OK;
        assertTrue(isMessageIndexed);

        DocsResponse dr = response.readEntity(DocsResponse.class);
        assertNotNull(dr);

        boolean isMessageFoundOnce = 1 == dr.getResponse().size();

        assertTrue(isMessageFoundOnce);
    }

    private static int genRandAccountNum() {
        return new Random().nextInt(ACCOUNT_RAND_HIGH - ACCOUNT_RAND_LOW) + ACCOUNT_RAND_LOW;
    }

    private static String genRandomString() {
        return RandomStringUtils.randomAlphabetic(RANDOM_STRING_LEN);
    }

    private static String toJsonMsg(String msg) {
        return JsonParser.getJsonStringFromObject(new IndexBody(msg));
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
