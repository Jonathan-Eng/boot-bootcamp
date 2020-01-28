package jettyjersey;

import api.AccountInvalidTokenException;
import api.AccountTokenUnauthorizedException;
import api.AccountsServiceApi;
import config.ProducerConfiguration;
import globals.accounts.AccountGlobals;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import parser.JsonParser;
import util.AccountResponses;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;
import static java.util.Objects.requireNonNull;

@Singleton
@Path("/")
public class IndexResource {

    private final KafkaProducer<String, String> prod;
    private final ProducerConfiguration producerConfiguration;
    private final static Logger logger = LogManager.getLogger(IndexResource.class);
    private AccountsServiceApi accountsServiceApi;

    @Inject
    public IndexResource(KafkaProducer<String, String> prod, AccountsServiceApi accountsServiceApi, ProducerConfiguration producerConfiguration) {
        this.prod = requireNonNull(prod);
        this.accountsServiceApi = accountsServiceApi;
        this.producerConfiguration = producerConfiguration;
    }


    @POST
    @Path("/index/{" + AccountGlobals.ACCOUNT_TOKEN + "}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveInfoAndSendToEs(IndexBody indexBody,
                                            @Context HttpHeaders httpHeaders,
                                            @PathParam(AccountGlobals.ACCOUNT_TOKEN) String accountToken) {
        try {
            accountsServiceApi.getAccount(accountToken);
        } catch (AccountTokenUnauthorizedException e) {
            return AccountResponses.unauthorizedTokenResponse(accountToken);
        } catch (AccountInvalidTokenException e) {
            return AccountResponses.invalidTokenResponse(accountToken);
        }

        // get User-Agent
        String userAgent = httpHeaders.getRequestHeaders().get("User-Agent").get(0);

        // send info to kafka
        sendInfoToKafka(accountToken, indexBody, userAgent);

        return Response.ok().entity(indexBody).build();
    }

    private void sendInfoToKafka (String accountToken, IndexBody indexBody, String userAgent) throws ElasticsearchException {

        // generate map of properties to store
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("message", indexBody.getMessage());
        jsonMap.put("User-Agent", userAgent);

        try {
            // write indexBody as String
            String msg = JsonParser.getJsonStringFromObject(jsonMap);

            // create record to send
            ProducerRecord<String, String> prodRecord = new ProducerRecord<>(producerConfiguration.getTopic(), accountToken, msg);
            prod.send(prodRecord);

            logger.debug("Successfully Sent message: " + msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
