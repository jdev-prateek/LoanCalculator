package org.example.loancalculator.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PropertiesUtil {
    private static final Logger log = LoggerFactory.getLogger(PropertiesUtil.class);
    private static final Map<String, String> mapping = new HashMap<>();

    public static void load() {
        Properties properties = new Properties();
        String settingsPropertiesPath = AppState.getSettingsPropertiesPath();

        try {
            FileInputStream fileInputStream = new FileInputStream(settingsPropertiesPath);
            properties.load(fileInputStream);

            for (String stringPropertyName : properties.stringPropertyNames()) {
                mapping.put(stringPropertyName, properties.getProperty(stringPropertyName));
            }

            AppState.setSettings(mapping);
            log.info("mapping loaded from settings.properties");
        } catch (IOException e) {
            log.error("Failed to read file: ", e);
        }

    }

    public static void dump(){
        Properties properties = new Properties();
        properties.putAll(AppState.getSettings());

        String settingsPropertiesPath = AppState.getSettingsPropertiesPath();

        try {
//            URL resourceUrl = getClass().getResource("/org/example/loancalculator/settings.properties");
            FileOutputStream outputStream = new FileOutputStream(settingsPropertiesPath);
            properties.store(outputStream, "");
            log.info("mapping saved to settings.properties");
        } catch (IOException e) {
            log.error("Failed to write file: ", e);
        }
    }
}
