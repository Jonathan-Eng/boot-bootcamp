import com.google.inject.Guice;
import io.logz.guice.jersey.JerseyServer;
import juice.modules.LogsShipperModule;
import juice.modules.StrictExplicitBindingModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class SendLogs {
    private static final Logger logger = LogManager.getLogger(SendLogs.class);

    public static void main(String[] args) throws Exception {

        Guice.createInjector(new LogsShipperModule(), new StrictExplicitBindingModule())
                .getInstance(JerseyServer.class).start();
        logger.info("Server is running!");

    }

}
