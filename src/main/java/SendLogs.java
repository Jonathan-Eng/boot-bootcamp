import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
// import org.apache.logging.log4j.core.jmx.Server;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;


public class SendLogs {

    // HTTP SERVER
    // private static int count;
    // HTTP SERVER
    // private static Logger logger = LogManager.getLogger(SendLogs.class);

    public static void main(String[] args) throws IOException {


        // Sun's HTTP server
        /*
        HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
        HttpContext context = server.createContext("/boot-bootcamp");
        context.setHandler(SendLogs::handleRequest);
        server.start();
        */

        // Jetty HTTP server
        ResourceConfig config = new ResourceConfig();
        config.packages("jettyjersey");
        ServletHolder servlet = new ServletHolder(new ServletContainer(config));


        Server server = new Server(8500);
        ServletContextHandler context = new ServletContextHandler(server, "/*");
        context.addServlet(servlet, "/*");


        try {
            server.start();
            server.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            server.destroy();
        }

        System.out.println("server run successfully");
    }



    // handler for HTTP request
    /*
    private static void handleRequest(HttpExchange exchange) throws IOException, IOException {

        // increment count
        count++;

        // prepare log info
        String response = "boot boot " + SendLogs.count;

        // send a log
        logger.info(response);

        // send a response to the server
        exchange.sendResponseHeaders(200, response.getBytes().length);//response code and length
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }*/

}
