package com.canevi.engine.io;

import lombok.extern.slf4j.Slf4j;

import java.net.URL;

@Slf4j
public class ResourceLoader {
    public static String load(String filePath) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        URL resourceUrl = classLoader.getResource(filePath);

        if (resourceUrl != null) {
            return resourceUrl.getPath();
        } else {
            log.info("File not found in the classpath.");
        }
        return filePath;
    }
}
