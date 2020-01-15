package parser;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T getObjFromJsonString(String jsonString, Class<T> clazz) throws RuntimeException {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Could not parse String %s to object of class %s", jsonString, clazz.getName()));
        }
    }

    public static <T> String getJsonStringFromObject(T obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        }
        catch (IOException e) {
            throw new RuntimeException(String.format("Could not parse object to String"));
        }
    }
}
