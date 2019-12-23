package config;

import java.util.Map;

public class ElasticsearchConfiguration {

    private static final String HOSTNAME_KEY = "host";
    private static final String PORT_KEY = "port";
    private static final String SCHEME_KEY = "scheme";

    private String hostname;
    private int port;
    private String scheme;

    public ElasticsearchConfiguration(Map<String, Object> jsonMap) {
        this.hostname = (String) jsonMap.get(HOSTNAME_KEY);
        this.port = (Integer) jsonMap.get(PORT_KEY);
        this.scheme = (String) jsonMap.get(SCHEME_KEY);
    }

    public String getHostname() { return hostname; }
    public int getPort() { return port; }
    public String getScheme() { return scheme;}
}
