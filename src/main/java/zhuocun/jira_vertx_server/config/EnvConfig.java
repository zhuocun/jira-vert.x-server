package zhuocun.jira_vertx_server.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import zhuocun.jira_vertx_server.utils.exceptions.MyRuntimeException;

public class EnvConfig {
    private final Properties properties;

    public EnvConfig(String envFilePath) {
        properties = new Properties();
        try (InputStream input = new FileInputStream(envFilePath)) {
            properties.load(input);
        } catch (IOException e) {
            throw new MyRuntimeException("Failed to load .env file", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }
}
