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
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Path("/")
public class Resource {

    private static int count;
    private static Logger logger = LogManager.getLogger(Resource.class);
    private LogsConfiguration logsConfig;


    @Inject
    public Resource(LogsConfiguration logsConfig) {
        this.logsConfig = logsConfig;
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





    /*
    @POST
    @Path("index")
    @Consumes(MediaType.APPLICATION_JSON)
    public String retrieveInfoAndSendToEs(IndexBody indexBody, @Context HttpHeaders httpHeaders) throws IOException {
        Resource.logger = LogManager.getLogger(Resource.class);
        // get User-Agent
        String userAgent = httpHeaders.getRequestHeaders().get("User-Agent").get(0);

        // send info to es
        sendInfoToEs(indexBody, userAgent);

        return "Successful";
    }

    private void sendInfoToEs (IndexBody indexBody, String userAgent) throws IOException,  ElasticsearchException{

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost("elasticsearch", 9200, "http")));


        Map<String, Object> jsonMap = new HashMap<>();
        jsonMap.put("message", indexBody.getMessage());
        jsonMap.put("User-Agent", userAgent);

        IndexRequest indexRequest = new IndexRequest("logs", "_doc")
                .source(jsonMap);

        client.index(indexRequest, RequestOptions.DEFAULT);

        client.close();
    }*/




}