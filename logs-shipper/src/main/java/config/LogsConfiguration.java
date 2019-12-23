package config;

import java.util.Map;

public class LogsConfiguration {

    private final String MSG_KEY = "msg";

    private String msg;

    public LogsConfiguration(Map<String, Object> jsonMap) {
        this.msg = (String) jsonMap.get(MSG_KEY);
    }

    public String getMsg() {
        return msg;
    }

}
