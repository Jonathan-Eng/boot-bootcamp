package jettyjersey;

import config.LogsConfiguration;
import org.apache.http.HttpHost;
import org.apache.logging.log4j.LogManager;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

//for elasticsearch
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import static java.util.Objects.requireNonNull;

@Path("/")
public class Resource {

    private static int count;
    private static Logger logger = LogManager.getLogger(Resource.class);
    private LogsConfiguration logsConfig;


    @Inject
    public Resource(LogsConfiguration logsConfig) {
        this.logsConfig = requireNonNull(logsConfig);
    }

    @GET
    @Path("boot-bootcamp")
    @Produces(MediaType.TEXT_PLAIN)
    public String logBootBoot() throws IOException {

        // increment count
        count++;

        // prepare log info
        String response = logsConfig.getMsg() + " " + Resource.count;

        // send a log
        logger.info(response);

        return response;
    }
}