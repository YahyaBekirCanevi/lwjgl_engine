package com.canevi.io;

import java.net.URL;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceLoader {
    private static final Logger log = LoggerFactory.getLogger(ResourceLoader.class);
    public static String load(String filePath) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resourceUrl = classLoader.getResource(filePath);

        if (resourceUrl != null) {
            String absolutePath = resourceUrl.getPath();
            log.info("Absolute path: " + absolutePath);
            return absolutePath;
            // Rest of your code
        } else {
            log.info("File not found in the classpath.");
        }
        return filePath;
    }
}
