package api;

import parser.JsonParser;
import pattern.PatternValidator;
import pojos.Account;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.HttpURLConnection;

public class AccountServiceApi {

    private AccountServiceClient accountServiceClient;

    public AccountServiceApi() {
        this.accountServiceClient = new AccountServiceClient();
    }

    public AccountServiceApi(String protocol, String host, int port) {
        this.accountServiceClient = new AccountServiceClient(protocol, host, port);
    }

    public Account createAccount(String accountName) {
        if (PatternValidator.isNameValid(accountName)) {
            Response response = accountServiceClient.createAccount(accountName);
            if (response.getStatus() == HttpURLConnection.HTTP_OK) {
                return JsonParser.getObjFromJsonString(response.readEntity(String.class), Account.class);
            }
        }
        return null;
    }

    public Account getAccount(String token) {
        if (PatternValidator.isTokenValid(token)) {
            Response response = accountServiceClient.getAccountByToken(token);
            if (response.getStatus() == HttpURLConnection.HTTP_OK) {
                return JsonParser.getObjFromJsonString(response.readEntity(String.class), Account.class);
            }
        }
        return null;
    }

    class AccountServiceClient {
        private static final String DEFAULT_PROTOCOL = "http";
        private static final String DEFAULT_HOST = "accounts-service";
        private static final int DEFAULT_PORT = 8000;
        private static final String CREATE_ACCOUNT_ENDPOINT = "create-account";

        private String host;
        private int port;
        private String protocol;
        private WebTarget webTarget;

        public AccountServiceClient() {
            this.protocol = DEFAULT_PROTOCOL;
            this.host = DEFAULT_HOST;
            this.port = DEFAULT_PORT;
            this.webTarget = initWebTarget(protocol, host, port);
        }


        public AccountServiceClient(String protocol, String host, int port) {
            this.protocol = protocol;
            this.host = host;
            this.port = port;
            this.webTarget = initWebTarget(protocol, host, port);
        }

        private WebTarget initWebTarget(String protocol, String host, int port) {
            return ClientBuilder.newClient().target(protocol + "://" + host + ":" + port + "/");
        }

        /**
         * Gets an the account belonging to accountToken. Return the account in json format
         * @param accountToken
         * @return
         */
        public Response getAccountByToken(String accountToken) {
            return webTarget.path("/account/token/" + accountToken)
                    .request(MediaType.APPLICATION_JSON)
                    .get();
        }

        /**
         * Creates a new account with the account's name
         * @param accountName
         * @return
         */
        public Response createAccount(String accountName) {
            String jsonRequestStr = String.format("{\"accountName\": \"%s\"}", accountName);
            return webTarget.path(CREATE_ACCOUNT_ENDPOINT)
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.json(jsonRequestStr));
        }
    }

}
