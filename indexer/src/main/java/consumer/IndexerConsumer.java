package consumer;

import com.google.inject.Inject;
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

import javax.inject.Singleton;
import java.io.IOException;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;
import static java.util.Objects.requireNonNull;

@Singleton
public class IndexerConsumer {

    private final KafkaConsumer<String, String> consumer;
    private final RestHighLevelClient elasticsearchClient;
    private String topic = "my_topic";
    private static final Logger logger = LogManager.getLogger(IndexerConsumer.class);

    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Inject
    public IndexerConsumer(KafkaConsumer<String, String> consumer, RestHighLevelClient elasticsearchClient) {
        this.consumer = requireNonNull(consumer);
        this.elasticsearchClient = requireNonNull(elasticsearchClient);
    }

    public void consume() {

        consumer.subscribe(Collections.singletonList(topic));      // list contains just 1 topic

        // consume forever
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));
            if (records.isEmpty()) continue;
            BulkRequest request = new BulkRequest();
            records.forEach(r -> {

                // index record to ES
                try {
                    Map<String, String> rValAsMap = JsonParser.getObjFromJsonString(r.value(), Map.class);
                    IndexRequest indexRequest = new IndexRequest("logs", "_doc")
                            .source(rValAsMap);
                    request.add(indexRequest);

                    logger.debug("Record was indexed: (%s, %s, %d, %d)\n",
                            r.key(), r.value(), r.partition(), r.offset());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            if (request.numberOfActions() == 0) continue;
            try {
                BulkResponse bulkResponse =elasticsearchClient.bulk(request, RequestOptions.DEFAULT);
                if (bulkResponse.hasFailures()) {

                    logger.debug("Bulk: an error has occured in bulk response");
                }
                consumer.commitAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}