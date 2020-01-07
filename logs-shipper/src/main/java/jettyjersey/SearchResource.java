package jettyjersey;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import parser.QueryStringParser;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@Singleton
@Path("/")
public class SearchResource {

    private final RestHighLevelClient esClient;

    @Inject
    public SearchResource(RestHighLevelClient esClient) {
        this.esClient = requireNonNull(esClient);
    }

    @GET
    @Path("search")
    @Produces(MediaType.APPLICATION_JSON)
    public DocsResponse search(@Context UriInfo uriInfo) throws IOException {

        Map<String, String> queryMap = getQueryAsMap(uriInfo);
        SearchRequest searchRequest = buildSearchRequest(queryMap);

        // send request
        SearchResponse searchResponse = esClient.search(searchRequest, RequestOptions.DEFAULT);

        return buildResponse(searchResponse);

    }

    private DocsResponse buildResponse(SearchResponse searchResponse) {
        // retrieve hits
        SearchHits hits = searchResponse.getHits();
        SearchHit[] searchHits = hits.getHits();

        // add hits to our response
        List<Map<String, Object>> response = new ArrayList<>();
        for (SearchHit hit : searchHits) {
            response.add(hit.getSourceAsMap());
        }

        return new DocsResponse(response);
    }

    private SearchRequest buildSearchRequest(Map<String, String> queryMap) {
        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        for (Map.Entry<String, String> e : queryMap.entrySet()) {
            boolQueryBuilder.must(QueryBuilders.matchQuery(e.getKey(), e.getValue()));
        }
        sourceBuilder.query(boolQueryBuilder);
        searchRequest.source(sourceBuilder);

        return searchRequest;
    }

    private Map<String,String> getQueryAsMap(UriInfo uriInfo) {
        String query = uriInfo.getRequestUri().getQuery();
        return QueryStringParser.getQueryMap(query);
    }

}
