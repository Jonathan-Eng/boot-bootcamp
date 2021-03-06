package juice.modules;

import config.ConfigFileFinder;
import config.ConfigurationFactory;
import config.ServerConfiguration;
import com.google.inject.AbstractModule;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.configuration.JerseyConfiguration;

/**
 * installs jersey dependencies
 */
public class ServerModule extends AbstractModule {

    private final String CONFIG_FILE_NAME = "server.config";

    private JerseyConfiguration jerseyConfiguration;
    private ServerConfiguration serverConfiguration;


    public ServerModule() {
        serverConfiguration = ConfigurationFactory.create(
                ConfigFileFinder.findRealPath(CONFIG_FILE_NAME),
                ServerConfiguration.class);

        jerseyConfiguration = JerseyConfiguration.builder()
                .addPackage(serverConfiguration.getPackageName()).addPort(serverConfiguration.getPort()).build();
    }

    @Override
    protected void configure() {
        install(new JerseyModule(jerseyConfiguration));
    }

}
