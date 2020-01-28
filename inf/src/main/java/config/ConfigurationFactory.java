package config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.FileInputStream;

public class ConfigurationFactory {

    /**
     * creates an instance of type T according to a json file located at filePath
     * @param filePath - path to json file
     * @param classObj - object of type Class
     * @param <T> - generic template class
     * @return T
     */
    public static <T> T create (String filePath, Class<T> classObj) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            FileInputStream fileInputStream = new FileInputStream(filePath);
            return objectMapper.readValue(fileInputStream, classObj);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
