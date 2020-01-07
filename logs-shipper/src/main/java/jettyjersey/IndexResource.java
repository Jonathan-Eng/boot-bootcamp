package jettyjersey;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.ElasticsearchException;
import parser.JsonParser;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import static java.util.Objects.requireNonNull;

@Singleton
@Path("/")
public class IndexResource {

    private final KafkaProducer<String, String> prod;
    private static int prodRecNum = 0;
    private String topic = "my_topic";
    private final static Logger logger = LogManager.getLogger(IndexResource.class);

    @Inject
    public IndexResource(KafkaProducer<String, String> prod) {
        this.prod = requireNonNull(prod);
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @POST
    @Path("index")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response retrieveInfoAndSendToEs(IndexBody indexBody, @Context HttpHeaders httpHeaders) throws IOException {
        // get User-Agent
        String userAgent = httpHeaders.getRequestHeaders().get("User-Agent").get(0);

        // send info to kafka
        sendInfoToKafka(indexBody, userAgent);

        return Response.ok().entity(indexBody).build();
    }

    private void sendInfoToKafka (IndexBody indexBody, String userAgent) throws IOException, ElasticsearchException {

        // generate map of properties to store
        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("message", indexBody.getMessage());
        jsonMap.put("User-Agent", userAgent);

        try {
            // write indexBody as String
            String msg = JsonParser.getJsonStringFromObject(jsonMap);

            // create record to send
            ProducerRecord<String, String> prodRecord = new ProducerRecord<>(topic, Integer.toString(++prodRecNum), msg);
            prod.send(prodRecord);

            logger.debug("Successfully Sent message: " + msg);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
