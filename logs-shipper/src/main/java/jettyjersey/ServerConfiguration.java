import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class ServerConfiguration {
    private String logMsg;

    public ServerConfiguration () throws IOException {

        // read from the json file
        File file = new File("server.config");
        logMsg = "";

        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null)
            logMsg += st + "\n";
    }
}
