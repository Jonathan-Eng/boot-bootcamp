package juice.modules;

import com.google.inject.AbstractModule;
import config.ConfigFileFinder;
import config.ConfigurationFactory;
import config.ElasticsearchConfiguration;
import com.google.inject.Provides;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;


public class ElasticsearchClientModule extends AbstractModule {

    private final String CONFIG_FILE_NAME = "elasticsearch.config";

    @Provides
    public RestHighLevelClient getHighRestLevelClient() {

        ElasticsearchConfiguration esConfig = ConfigurationFactory.create(
                ConfigFileFinder.findRealPath(CONFIG_FILE_NAME),
                ElasticsearchConfiguration.class);

        return new RestHighLevelClient(
                RestClient.builder(
                        new HttpHost(esConfig.getHostname(), esConfig.getPort(), esConfig.getScheme())));
    }
}
