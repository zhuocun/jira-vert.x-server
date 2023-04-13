package com.zhuocun.jira_vertx_server.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvConfig {
    private final Properties properties;
    public static final String DATABASE = System.getenv("DATABASE");

    public EnvConfig(String envFilePath) {
        properties = new Properties();
        try (InputStream input = new FileInputStream(envFilePath)) {
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load .env file", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
