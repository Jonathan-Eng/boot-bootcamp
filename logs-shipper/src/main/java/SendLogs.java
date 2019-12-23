import com.google.inject.Guice;
import io.logz.guice.jersey.JerseyServer;
import juice.modules.LogsShipperModule;
import juice.modules.StrictExplicitBindingModule;


public class SendLogs {

    public static void main(String[] args) throws Exception {

        Guice.createInjector(new LogsShipperModule(), new StrictExplicitBindingModule())
                .getInstance(JerseyServer.class).start();
        System.out.println("Server is running!");

    }

}
