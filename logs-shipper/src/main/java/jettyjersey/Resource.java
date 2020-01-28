package jettyjersey;

import config.LogsConfiguration;
import org.apache.logging.log4j.LogManager;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.Logger;
import java.io.IOException;

import static java.util.Objects.requireNonNull;

@Path("/")
public class Resource {

    private static int count;
    private static Logger logger = LogManager.getLogger(Resource.class);
    private LogsConfiguration logsConfig;

    @Inject
    public Resource(LogsConfiguration logsConfig) {
        this.logsConfig = requireNonNull(logsConfig);
    }

    @GET
    @Path("boot-bootcamp")
    @Produces(MediaType.TEXT_PLAIN)
    public Response logBootBoot() {

        // increment count
        count++;

        // prepare log info
        String response = logsConfig.getMsg() + " " + Resource.count;

        // send a log
        logger.info(response);

        return Response.ok().entity(response).build();
    }
}