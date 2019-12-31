package juice.modules;

import com.google.inject.AbstractModule;
import main.Indexer;

public class IndexerModule extends AbstractModule {
    public void configure() {
        install(new ServerModule());
        install(new ElasticsearchClientModule());
        install(new ConsumerModule());
        bind(Indexer.class);
    }

}
