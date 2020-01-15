package juice.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import config.AccountsServiceConfiguration;
import config.ConfigFileFinder;
import config.ConfigurationFactory;
import main.AccountsService;

public class AccountsServiceModule extends AbstractModule {

    public void configure() {
        install(new ServerModule());
        install(new AccountsDbModule());
    }

    /*
    @Provides
    AccountsServiceConfiguration provideAccountsServiceConfiguration() {
        return ConfigurationFactory.create(
                ConfigFileFinder.findRealPath(CONFIG_FILE_NAME),
                AccountsServiceConfiguration.class);
    }*/
}
