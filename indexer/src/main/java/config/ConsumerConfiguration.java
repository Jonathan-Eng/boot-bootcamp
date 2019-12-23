package config;

import java.util.Map;

public class ConsumerConfiguration {
    private final String HOST_KEY = "host";
    private final String PORT_KEY = "port";
    private final String GROUP_ID_KEY = "group.id";

    private String host;
    private int port;
    private String groupId;

    public ConsumerConfiguration(Map<String, Object> jsonMap) {
        this.host = (String) jsonMap.get(HOST_KEY);
        this.port = (Integer) jsonMap.get(PORT_KEY);
        this.groupId = (String) jsonMap.get(GROUP_ID_KEY);
    }

    public String getHost() { return host; }
    public int getPort() { return port; }
    public String getGroupId() { return groupId; }

}
