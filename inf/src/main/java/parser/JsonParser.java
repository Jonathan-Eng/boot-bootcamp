package parser;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T getObjFromJsonString(String jsonString, Class<T> clazz) throws IOException {
        return objectMapper.readValue(jsonString, clazz);
    }
    public static <T> String getJsonStringFromObject(T obj) throws IOException {
        return objectMapper.writeValueAsString(obj);
    }
}
