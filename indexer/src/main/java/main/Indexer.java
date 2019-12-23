package main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Inject;
import juice.modules.IndexerModule;
import juice.modules.StrictExplicitBindingModule;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;

import javax.inject.Singleton;
import java.time.Duration;
import java.util.Collections;
import java.util.Map;

@Singleton
public class Indexer {

        private final KafkaConsumer<String, String> consumer;
        private final RestHighLevelClient client;

        @Inject
        public Indexer(KafkaConsumer<String, String> consumer, RestHighLevelClient client) {

                this.consumer = consumer;
                this.client = client;
        }

        public void consume() {

                // subscribe to topic
                consumer.subscribe(Collections.singletonList("my_topic"));      // list contains just 1 topic

                // consume forever
                while (true) {


                        // prepare object mapper to read json
                        ObjectMapper objectMapper = new ObjectMapper();

                        // poll records
                        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(1000));

                        records.forEach(r -> {

                                // index record to ES
                                try {

                                        // convert record to Map
                                        Map<String, String> rValAsMap= objectMapper.readValue(r.value(), Map.class);

                                        // index request
                                        IndexRequest indexRequest = new IndexRequest("logs", "_doc")
                                                .source(rValAsMap);
                                        client.index(indexRequest, RequestOptions.DEFAULT);

                                        // print indexed record
                                        System.out.printf("Record was indexed: (%s, %s, %d, %d)\n",
                                                r.key(), r.value(), r.partition(), r.offset());


                                } catch (Exception e) {
                                        e.printStackTrace();
                                }
                        });


                        consumer.commitAsync(); // TODO check if commitSync is more appropriate
                }



                // TODO close client
                //client.close();
        }

        public static void main(String[] args ) {
                Guice.createInjector(new IndexerModule(), new StrictExplicitBindingModule())
                        .getInstance(Indexer.class).consume();
                System.out.println("Consumer is running!");
        }


}
