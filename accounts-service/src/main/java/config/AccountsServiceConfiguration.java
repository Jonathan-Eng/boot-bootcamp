package config;

import java.util.Map;

public class AccountsServiceConfiguration {
    private final String FIELD_KEY = "field";

    private String field;

    public AccountsServiceConfiguration(Map<String, Object> jsonMap) {
        this.field = (String) jsonMap.get(FIELD_KEY);
    }

    public String getField() { return field; }
}
