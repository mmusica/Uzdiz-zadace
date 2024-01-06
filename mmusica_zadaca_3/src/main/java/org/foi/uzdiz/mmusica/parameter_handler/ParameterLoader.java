package org.foi.uzdiz.mmusica.parameter_handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class ParameterLoader {

    public ParameterLoader() {

    }
    public Properties loadProperties(String fileName) {
        Path path = Paths.get(fileName);
        Properties properties = new Properties();
        try (BufferedReader br = Files.newBufferedReader(path)) {
            String keyValuePair = br.readLine();
            while (keyValuePair != null) {
                String[] props = keyValuePair.trim().split("=");
                properties.setProperty(props[0], props[1].trim());
                keyValuePair = br.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
