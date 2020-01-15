package main;

import com.google.inject.Guice;
import io.logz.guice.jersey.JerseyServer;
import juice.modules.AccountsServiceModule;
import juice.modules.StrictExplicitBindingModule;

public class AccountsService {

    public static void main(String[] args ) throws Exception {

        // setup server and run it
        try {
            Guice.createInjector(new AccountsServiceModule(), new StrictExplicitBindingModule())
                    .getInstance(JerseyServer.class).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Accounts Service is running!");
    }
}
