package config;

import javax.validation.constraints.NotNull;
import java.util.Map;

public class ProducerConfiguration {

    @NotNull
    private String host;

    @NotNull
    private int port;

    @NotNull
    private String clientId;

    @NotNull
    private String topic;

    public ProducerConfiguration() {}

    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getClientId() { return clientId; }
    public String getTopic() { return topic; }
}
