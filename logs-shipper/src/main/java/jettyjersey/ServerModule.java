import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.ProvidesIntoSet;
import com.google.inject.servlet.ServletModule;
import io.logz.guice.jersey.JerseyModule;
import io.logz.guice.jersey.configuration.JerseyConfiguration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ServerModule extends AbstractModule {

    private JerseyConfiguration configuration;

    public ServerModule(int port, String packageName) {
        configuration = JerseyConfiguration.builder()
                .addPackage(packageName).addPort(port).build();
    }

    @Override
    protected void configure() {
        install(new JerseyModule(configuration));
    }

    @Provides
    public ServerConfiguration provideServerConfig () throws IOException {

        return new ServerConfiguration();
    }

}
