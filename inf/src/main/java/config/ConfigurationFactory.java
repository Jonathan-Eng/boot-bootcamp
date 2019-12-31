package config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

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

            // get json map
//            System.out.println("!!!!!" + filePath);
            Map<String, Object> jsonMap = getJsonMap(filePath);

            // get constuctor of class represented by jsonMap
            Constructor<T> constructor = classObj.getConstructor(Map.class);

            // use map to construct instance of classObj
            return constructor.newInstance(new Object[] {jsonMap});

        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }

    /**
     * Reads from a json file located in filePath and generates a Map<String, Object> that reflects the json file
     * @param filePath - path to json file
     * @return Map<String, Object>
     * @throws IOException
     */
    private static Map<String, Object> getJsonMap(String filePath) throws IOException {
        ObjectMapper objMapper = new ObjectMapper();
        return objMapper.readValue(
                new File(filePath),
                new TypeReference<Map<String, Object>>() {});
    }


}
