package main;

import com.google.inject.Guice;
import io.logz.guice.jersey.JerseyServer;
import juice.modules.AccountsServiceModule;
import juice.modules.StrictExplicitBindingModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountsService {
    private static final Logger logger = LogManager.getLogger(AccountsService.class);

    public static void main(String[] args ) throws Exception {

        // setup server and run it
        try {
            Guice.createInjector(new AccountsServiceModule(), new StrictExplicitBindingModule())
                    .getInstance(JerseyServer.class).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.debug("Accounts Service is running!");
    }
}
