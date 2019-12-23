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

    private JerseyConfiguration jc;
    private ServerConfiguration sc;


    public ServerModule() {
        sc = ConfigurationFactory.create(
                ConfigFileFinder.findRealPath(CONFIG_FILE_NAME),
                ServerConfiguration.class);

        jc = JerseyConfiguration.builder()
                .addPackage(sc.getPackageName()).addPort(sc.getPort()).build();
    }

    @Override
    protected void configure() {
        install(new JerseyModule(jc));
    }

}
