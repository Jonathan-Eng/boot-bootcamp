package juice.modules;

import com.google.inject.AbstractModule;
import consumer.IndexerConsumer;

public class IndexerModule extends AbstractModule {
    public void configure() {
        install(new ServerModule());
        install(new ElasticsearchClientModule());
        install(new KafkaConsumerModule());
        bind(IndexerConsumer.class);
    }

}
