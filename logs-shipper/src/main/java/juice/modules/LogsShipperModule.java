package juice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import config.ConfigFileFinder;
import config.ConfigurationFactory;
import config.LogsConfiguration;

public class LogsShipperModule extends AbstractModule {

    private final String CONFIG_FILE_NAME = "logs.config";

    public void configure() {
        install(new ServerModule());
        install(new ElasticsearchClientModule());
        install(new ProducerModule());
    }

    @Provides
    LogsConfiguration provideLogsConfiguration() {
        return ConfigurationFactory.create(
                ConfigFileFinder.findRealPath(CONFIG_FILE_NAME),
                LogsConfiguration.class);
    }


}
