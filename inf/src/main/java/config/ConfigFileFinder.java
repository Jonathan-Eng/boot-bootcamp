package config;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ConfigFileFinder {

    private static final List<String> supportedPaths = Arrays.asList(
            "resources/main/",  // docker of service having a main class
            "src/main/resources/",  // intellij having a main class
            Paths.get(System.getProperty("user.dir")).getParent().toString() + "/inf/src/main/resources/"   // intellij file in inf
    );

    public static String findRealPath(String fileName) {

        String realPath = supportedPaths.get(0) + fileName;

        for (String path : supportedPaths) {
            File f = new File(path + fileName);

            if (f.exists()) {
                realPath = path + fileName;
                break;
            }
        }

        return realPath;
    }



}
