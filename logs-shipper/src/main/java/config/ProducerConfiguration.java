package config;

import java.util.Map;

public class ProducerConfiguration {

    private final String HOST_KEY = "host";
    private final String PORT_KEY = "port";
    private final String CLIENT_ID_KEY = "client.id";

    private String host;
    private int port;
    private String clientId;

    public ProducerConfiguration(Map<String, Object> jsonMap) {
        this.host = (String) jsonMap.get(HOST_KEY);
        this.port = (Integer) jsonMap.get(PORT_KEY);
        this.clientId = (String) jsonMap.get(CLIENT_ID_KEY);
    }

    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getClientId() { return clientId; }

}
