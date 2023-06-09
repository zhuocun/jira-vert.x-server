package zhuocun.jira_vertx_server.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.google.inject.Singleton;
import zhuocun.jira_vertx_server.utils.exceptions.MyRuntimeException;

@Singleton
public class EnvConfig {
    private final Properties properties;

    public EnvConfig() {
        properties = new Properties();
        try (InputStream input = new FileInputStream(".env")) {
            properties.load(input);
        } catch (IOException e) {
            throw new MyRuntimeException("Failed to load .env file", e);
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getDBType() {
        return getProperty("DATABASE");
    }
}
