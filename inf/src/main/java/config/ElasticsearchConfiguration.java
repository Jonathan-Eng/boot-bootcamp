package config;

import javax.validation.constraints.NotNull;

public class ElasticsearchConfiguration {

    @NotNull
    private String hostname;

    @NotNull
    private int port;

    @NotNull
    private String scheme;

    public ElasticsearchConfiguration(){}

    public String getHostname() { return hostname; }
    public int getPort() { return port; }
    public String getScheme() { return scheme;}
}
