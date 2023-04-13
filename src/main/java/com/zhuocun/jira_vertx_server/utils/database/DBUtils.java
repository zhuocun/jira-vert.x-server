package com.zhuocun.jira_vertx_server.utils.database;

import java.util.List;

import com.zhuocun.jira_vertx_server.config.EnvConfig;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import com.zhuocun.jira_vertx_server.constants.EDatabase;
import com.zhuocun.jira_vertx_server.constants.ErrorMsg;

public class DBUtils {

    public static Future<JsonObject> findOne(JsonObject reqBody, String tableName) {
        EnvConfig config = new EnvConfig(".env");
        String database = config.getProperty("DATABASE").toUpperCase();
        switch (EDatabase.valueOf(database)) {
            case POSTGRESQL:
                return PostgresUtils.findOne(reqBody, tableName);
            default:
                throw new Error(ErrorMsg.INVALID_DB);
        }
    }

    public static Future<Void> createItem(JsonObject item, String tableName) {
        EnvConfig config = new EnvConfig(".env");
        String database = config.getProperty("DATABASE").toUpperCase();
        switch (EDatabase.valueOf(database)) {
            case POSTGRESQL:
                return PostgresUtils.createItem(item, tableName);
            default:
                throw new Error(ErrorMsg.INVALID_DB);
        }
    }

    public static Future<List<JsonObject>> find(JsonObject reqBody, String tableName) {
        EnvConfig config = new EnvConfig(".env");
        String database = config.getProperty("DATABASE").toUpperCase();
        switch (EDatabase.valueOf(database)) {
            case POSTGRESQL:
                return PostgresUtils.find(reqBody, tableName);
            default:
                throw new Error(ErrorMsg.INVALID_DB);
        }
    }

    public static Future<JsonObject> findById(String id, String tableName) {
        EnvConfig config = new EnvConfig(".env");
        String database = config.getProperty("DATABASE").toUpperCase();
        switch (EDatabase.valueOf(database)) {
            case POSTGRESQL:
                return PostgresUtils.findById(id, tableName);
            default:
                throw new Error(ErrorMsg.INVALID_DB);
        }
    }

    public static Future<JsonObject> findByIdAndDelete(String id, String tableName) {
        EnvConfig config = new EnvConfig(".env");
        String database = config.getProperty("DATABASE").toUpperCase();
        switch (EDatabase.valueOf(database)) {
            case POSTGRESQL:
                return PostgresUtils.findByIdAndDelete(id, tableName);
            default:
                throw new Error(ErrorMsg.INVALID_DB);
        }
    }

    public static Future<JsonObject> findByIdAndUpdate(String id, JsonObject update, String tableName) {
        EnvConfig config = new EnvConfig(".env");
        String database = config.getProperty("DATABASE").toUpperCase();
        switch (EDatabase.valueOf(database)) {
            case POSTGRESQL:
                return PostgresUtils.findByIdAndUpdate(id, update, tableName);
            default:
                throw new Error(ErrorMsg.INVALID_DB);
        }
    }
}
