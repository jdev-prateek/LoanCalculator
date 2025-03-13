package org.example.loancalculator.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {
    private static final Logger log = LoggerFactory.getLogger(PropertiesUtil.class);
    public static String FILE_PATH = "/org/example/loancalculator/settings.properties";
    private final Map<String, String> mapping = new HashMap<>();

    public Map<String, String> load() {
        Properties properties = new Properties();
        try {
            InputStream inputStream = getClass().getResourceAsStream(FILE_PATH);
            if(inputStream == null){
                throw new IllegalArgumentException("inputStream can't be null");
            }

            properties.load(inputStream);

            for (String stringPropertyName : properties.stringPropertyNames()) {
                mapping.put(stringPropertyName, properties.getProperty(stringPropertyName));
            }

            log.info("mapping loaded from settings.properties");
        } catch (IOException e) {
            log.error("Failed to read file: ", e);
        }

        return mapping;
    }

    public void dump(){
        Properties properties = new Properties();
        properties.putAll(AppState.getSettings());

        try {
            URL resourceUrl = getClass().getResource("/org/example/loancalculator/settings.properties");
            FileOutputStream outputStream = new FileOutputStream(resourceUrl.getPath());
            properties.store(outputStream, "");
            log.info("mapping saved to settings.properties");
        } catch (IOException e) {
            log.error("Failed to write file: ", e);
        }
    }
}
