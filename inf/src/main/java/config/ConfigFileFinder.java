package config;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class ConfigFileFinder {

    private static final List<String> supportedPaths = Arrays.asList(
            System.getProperty("user.dir") + "/resources/main/",  // docker of service having a main class
            System.getProperty("user.dir") + "/src/main/resources/",  // intellij having a main class
            Paths.get(System.getProperty("user.dir")).getParent().toString() + "/inf/src/main/resources/"   // intellij file in inf
    );

    public static String findRealPath(String fileName) {

////        System.out.println(System.getProperty("user.dir"));
//        File folder = new File(System.getProperty("user.dir") + "/resources/main");
//        File[] listOfFiles = folder.listFiles();
//
//        for (int i = 0; i < listOfFiles.length; i++) {
//            if (listOfFiles[i].isFile()) {
//                System.out.println("File " + listOfFiles[i].getName());
//            } else if (listOfFiles[i].isDirectory()) {
//                System.out.println("Directory " + listOfFiles[i].getName());
//            }
//        }

        String realPath = supportedPaths.get(0) + fileName;

        for (String path : supportedPaths) {
//            System.out.println("?????" + path + fileName);
//            System.out.println(new File(path + fileName).getAbsolutePath());
            File f = new File(path + fileName);

            if (f.exists()) {
                realPath = f.getAbsolutePath();
                break;
            }
        }

        return realPath;
    }



}
