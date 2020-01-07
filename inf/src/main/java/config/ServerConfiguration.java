package config;

import javax.validation.constraints.NotNull;

public class ServerConfiguration {

    @NotNull
    private String logMessage;

    @NotNull
    private String packageName;

    @NotNull
    private int port;

    @NotNull
    private String host;


    public ServerConfiguration(){}

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

