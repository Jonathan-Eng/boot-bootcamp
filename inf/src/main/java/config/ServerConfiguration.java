package config;

import java.util.Map;

public class ServerConfiguration {

    private static final String LOG_MESSAGE_KEY = "logMessage";
    private static final String PACKAGE_NAME_KEY = "packageName";
    private static final String PORT_KEY = "port";
    private static final String HOST_KEY = "host";

    private String logMessage;
    private String packageName;
    private int port;
    private String host;


    public ServerConfiguration (Map<String, Object> jsonMap) {
        this.logMessage = (String) jsonMap.get(LOG_MESSAGE_KEY);
        this.packageName = (String) jsonMap.get(PACKAGE_NAME_KEY);
        this.port = (Integer) jsonMap.get(PORT_KEY);
        this.host = (String) jsonMap.get(HOST_KEY);
    }

    public String getHost() {
        return host;
    }
    public String getLogMessage() {
        return logMessage;
    }
    public String getPackageName() {
        return packageName;
    }
    public int getPort() { return port; }
}

