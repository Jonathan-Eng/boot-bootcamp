package config;

import javax.validation.constraints.NotNull;

public class AccountsServiceApiConfiguration {
    @NotNull
    private String protocol;

    @NotNull
    private String host;

    @NotNull
    private Integer port;

    public AccountsServiceApiConfiguration() {}

    public String getProtocol() {
        return protocol;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }
}
