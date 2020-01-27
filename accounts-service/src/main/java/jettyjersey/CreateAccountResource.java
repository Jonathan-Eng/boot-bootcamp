package jettyjersey;

import api.CreateAccountRequest;
import mybatis.mappers.AccountMapper;
import pattern.PatternValidator;
import pattern.PatternedStringGenerator;
import pojos.Account;

import util.AccountResponses;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
    public Response createAccount(CreateAccountRequest createAccountRequest) {
        String accountName = createAccountRequest.getAccountName();

        if(!PatternValidator.isNameValid(accountName)) return AccountResponses.invalidAccountName(accountName);

        if (accountMapper.getAccountByName(accountName) != null) {
            return AccountResponses.accountNameAlreadyExists(accountName);
        }

        String token = PatternedStringGenerator.generateToken();
        String esIndex = PatternedStringGenerator.generateEsIndex();

        Account account = new Account(accountName, token, esIndex);
        accountMapper.insert(account);

        Account accountWithId = accountMapper.getAccountByToken(token);

        return AccountResponses.tokenOkResponse(accountWithId);

    }
}
