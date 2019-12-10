package jettyjersey;

import org.apache.logging.log4j.LogManager;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import org.apache.logging.log4j.Logger;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Path("/")
public class Resource {

    private static int count;
    private static Logger logger = LogManager.getLogger(Resource.class);


    @GET
    @Path("boot-bootcamp")
    @Produces(MediaType.TEXT_PLAIN)
    public String aaa() {
        // increment count
        count++;

        // prepare log info
        String response = "boot boot " + Resource.count;
        response += " " + Resource.getMyIPandHostname();

        // send a log
        logger.info(response);

        return response;
    }

    private static String getMyIPandHostname() {
        InetAddress ip;
        String hostname = "";
        String ipStr = "";
        try {
            ip = InetAddress.getLocalHost();
            hostname = ip.getHostName();
            ipStr = ip.toString();

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return ipStr + " " + hostname;
    }

}