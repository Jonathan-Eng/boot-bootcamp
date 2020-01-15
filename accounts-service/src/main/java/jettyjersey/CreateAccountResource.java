package jettyjersey;

import com.fasterxml.jackson.core.JsonProcessingException;
import config.ConfigFileFinder;
import mybatis.mappers.AccountMapper;
import pattern.PatternValidator;
import pattern.PatternedStringGenerator;
import pojos.Account;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

@Path("/")
public class CreateAccountResource {

    private final AccountMapper accountMapper;

    @Inject
    public CreateAccountResource(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @POST
    @Path("create-account")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createAccount(CreateAccountBody createAccountBody) throws IOException {
        String accountName = createAccountBody.getAccountName();

        // check account name abides pattern
        if(!PatternValidator.isNameValid(accountName)) {
            String badRequestMsg = String.format("Account name %s does not abide by the account name pattern\n", accountName);
            return Response.status(HttpURLConnection.HTTP_BAD_REQUEST)
                    .entity(badRequestMsg).build();
        }

        // check account name not already exists
        if (accountMapper.getAccountByName(accountName) != null) {
            String badRequestMsg = String.format("Account name %s already exists\n", accountName);
            return Response.status(HttpURLConnection.HTTP_CONFLICT)
            .entity(badRequestMsg).build();
        }

        String token = generateUniqueToken();
        String esindex = generateUniqueEsindex();

        Account account = new Account(accountName, token, esindex);
        accountMapper.insert(account);

        // try return valid response
        try {
            return Response.status(HttpURLConnection.HTTP_OK).entity(account.toJsonString()).build();
        }
        catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // if reached here - json parsing went wrong
        return Response.status(HttpURLConnection.HTTP_NOT_ACCEPTABLE).entity("Task failed. Could not parse json").build();
    }

    private String generateUniqueToken() {
        String token = "";
        do {
            token = PatternedStringGenerator.generateToken();
        } while (accountMapper.getAccountByToken(token) != null);
        return token;
    }

    private String generateUniqueEsindex() {
        String esindex = "";
        do {
            esindex = PatternedStringGenerator.generateEsindex();
        } while (accountMapper.getAccountByEsindex(esindex) != null);
        return esindex;
    }

}
