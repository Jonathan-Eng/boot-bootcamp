package juice.modules;

import api.AccountsServiceApi;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import config.AccountsServiceApiConfiguration;
import config.ConfigFileFinder;
import config.ConfigurationFactory;

public class AccountsServiceApiModule extends AbstractModule {

    private static String CONFIG_FILE_NAME = "accounts-service-api.config";

    @Provides
    public AccountsServiceApi provideAccountsService() {
        AccountsServiceApiConfiguration accountsServiceApiConfiguration = ConfigurationFactory.create(
                ConfigFileFinder.findRealPath(CONFIG_FILE_NAME),
                AccountsServiceApiConfiguration.class);

        return new AccountsServiceApi(accountsServiceApiConfiguration.getProtocol(), accountsServiceApiConfiguration.getHost(), accountsServiceApiConfiguration.getPort() );

    }
}
