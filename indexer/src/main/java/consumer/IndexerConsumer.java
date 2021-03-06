package consumer;

import api.AccountInvalidTokenException;
import api.AccountTokenUnauthorizedException;
import api.AccountsServiceApi;
import com.google.inject.Inject;
import config.ConsumerConfiguration;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import parser.JsonParser;
import pojos.Account;

import javax.inject.Singleton;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;

import static java.util.Objects.requireNonNull;

@Singleton
public class IndexerConsumer {

    private final KafkaConsumer<String, String> consumer;
    private final RestHighLevelClient elasticsearchClient;
    private static final Logger logger = LogManager.getLogger(IndexerConsumer.class);
    private final AccountsServiceApi accountsServiceApi;
    private final ConsumerConfiguration consumerConfiguration;


    @Inject
    public IndexerConsumer(KafkaConsumer<String, String> consumer, RestHighLevelClient elasticsearchClient,
                           AccountsServiceApi accountsServiceApi, ConsumerConfiguration consumerConfiguration) {
        this.consumer = requireNonNull(consumer);
        this.elasticsearchClient = requireNonNull(elasticsearchClient);
        this.accountsServiceApi = accountsServiceApi;
        this.consumerConfiguration = consumerConfiguration;
    }

    public void consume() {

        consumer.subscribe(Collections.singletonList(consumerConfiguration.getTopic()));      // list contains just 1 topic

        // consume forever
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            if (records.isEmpty()) continue;

            BulkRequest request = getBulkRequestForRecords(records);

            if (request.numberOfActions() == 0) continue;
            try {
                BulkResponse bulkResponse =elasticsearchClient.bulk(request, RequestOptions.DEFAULT);
                if (bulkResponse.hasFailures()) {
                    logger.debug("Bulk: an error has occured in bulk response " + bulkResponse.buildFailureMessage());
                }
                consumer.commitAsync();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private BulkRequest getBulkRequestForRecords(ConsumerRecords<String, String> records) {
        BulkRequest bulkRequest = new BulkRequest();
        records.forEach(r -> {

            // index record to ES
            try {
                Map<String, Object> rValAsMap = JsonParser.getObjFromJsonString(r.value(), Map.class);

                Account account = accountsServiceApi.getAccount(r.key());
                IndexRequest indexRequest = new IndexRequest(account.getEsIndex(), "_doc")
                        .source(rValAsMap);
                bulkRequest.add(indexRequest);

                logger.debug("Record was added to bulk request: ({}, {}, {}, {})\n", r.key(), r.value(), r.partition(), r.offset());
            } catch (AccountInvalidTokenException | AccountTokenUnauthorizedException e) {
                e.printStackTrace();
            }
        });
        return bulkRequest;
    }
}