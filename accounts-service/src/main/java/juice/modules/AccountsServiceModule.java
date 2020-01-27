package juice.modules;

import com.google.inject.AbstractModule;

public class AccountsServiceModule extends AbstractModule {
    public void configure() {
        install(new ServerModule());
        install(new AccountsDbModule());
    }
}
