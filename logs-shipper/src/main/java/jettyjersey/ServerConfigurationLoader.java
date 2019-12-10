package jettyjersey;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class ServerConfigurationGenerator {
    public static ServerConfiguration generate() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(new File("resources/server.config"), ServerConfiguration.class);
    }
}
