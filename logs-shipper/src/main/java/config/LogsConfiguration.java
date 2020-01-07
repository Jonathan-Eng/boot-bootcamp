package config;

import javax.validation.constraints.NotNull;

public class LogsConfiguration {

    @NotNull
    private String msg;

    public LogsConfiguration() {}

    public String getMsg() {
        return msg;
    }

}
