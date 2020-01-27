package juice.modules;

import api.AccountsServiceApi;
import com.google.inject.AbstractModule;
import consumer.IndexerConsumer;

public class IndexerModule extends AbstractModule {
    public void configure() {
        install(new ServerModule());
        install(new ElasticsearchClientModule());
        install(new KafkaConsumerModule());
        install(new AccountsServiceApiModule());
        bind(IndexerConsumer.class);
    }

}
