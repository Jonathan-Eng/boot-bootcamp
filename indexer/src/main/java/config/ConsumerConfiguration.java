package config;

import javax.validation.constraints.NotNull;
import java.util.Map;

import static java.util.Objects.requireNonNull;

public class ConsumerConfiguration {

    @NotNull
    private String host;

    @NotNull
    private int port;

    @NotNull
    private String groupId;

    @NotNull
    private String topic;

    public ConsumerConfiguration() {}

    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getGroupId() { return groupId; }
    public String getTopic() { return topic; }

}
