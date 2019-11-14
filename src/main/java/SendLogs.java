import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class SendLogs {

    private static int count;

    private static Logger logger = LogManager.getLogger(SendLogs.class);

    public static void main(String[] args) throws IOException {
        /*
        // create a file
        File file = new File("newfile.txt");
        if(file.createNewFile()){
            System.out.println("file.txt File Created in Project root directory");
        } else System.out.println("File file.txt already exists in the project root directory");
        */


        HttpServer server = HttpServer.create(new InetSocketAddress(8500), 0);
        HttpContext context = server.createContext("/boot-bootcamp");
        context.setHandler(SendLogs::handleRequest);
        server.start();
        System.out.println("server run successfully");
    }

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
    }

}
