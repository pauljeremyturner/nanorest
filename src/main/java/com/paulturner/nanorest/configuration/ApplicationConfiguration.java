package com.paulturner.nanorest.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ApplicationConfiguration {

    public static final String KEY_PORT = "com.com.paulturner.common.port";

    private static final Logger logger = Logger.getLogger(ApplicationConfiguration.class.getName());

    private static Properties properties = new Properties();

    public static void loadFromPath(String propertiesPath) {
        File propertiesFile = new File(propertiesPath);
        try (FileInputStream fis = new FileInputStream(propertiesFile)) {
            logger.log(Level.INFO, "Loading application properties from file [{0}]", propertiesPath);
            properties.load(fis);
            logger.log(Level.INFO, "Loaded application properties [{0}]", properties);
            properties.stringPropertyNames().forEach(pn -> System.setProperty(pn, properties.getProperty(pn)));
        } catch (IOException ioe) {
            logger.log(Level.SEVERE, "Failed to load properties", ioe);
            throw new IllegalArgumentException("Failed to load properties", ioe);
        }
    }
}
