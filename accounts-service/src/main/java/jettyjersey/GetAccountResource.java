package jettyjersey;

import globals.accounts.AccountGlobals;
import util.AccountResponses;
import mybatis.mappers.AccountMapper;
import pattern.PatternValidator;
import pojos.Account;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Singleton
@Path("/account")
public class GetAccountResource {

    private final AccountMapper accountMapper;

    @Inject
    public GetAccountResource(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @GET
    @Path("/token/{" + AccountGlobals.ACCOUNT_TOKEN + "}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAccountByToken(@PathParam(AccountGlobals.ACCOUNT_TOKEN) String accountToken){
        if (!PatternValidator.isTokenValid(accountToken)) return AccountResponses.invalidTokenResponse(accountToken);
        Account account = accountMapper.getAccountByToken(accountToken);
        if (account == null) return AccountResponses.unauthorizedTokenResponse(accountToken);   //  TODO change from unauthorized to not found
        return AccountResponses.tokenOkResponse(account);
    }
}
