package com.zhuocun.jira_vertx_server.utils.database;

import java.util.List;

import com.zhuocun.jira_vertx_server.config.EnvConfig;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import com.zhuocun.jira_vertx_server.constants.DatabaseType;
import com.zhuocun.jira_vertx_server.constants.ErrorMsg;

public class DBUtils {

    public static Future<JsonObject> findOne(JsonObject reqBody, String tableName) {
        switch (getDBType()) {
            case DatabaseType.POSTGRESQL:
                return PostgresUtils.findOne(reqBody, tableName);
            default:
                throw new Error(ErrorMsg.INVALID_DB);
        }
    }

    public static Future<Void> createItem(JsonObject item, String tableName) {
        switch (getDBType()) {
            case DatabaseType.POSTGRESQL:
                return PostgresUtils.createItem(item, tableName);
            default:
                throw new Error(ErrorMsg.INVALID_DB);
        }
    }

    public static Future<List<JsonObject>> find(JsonObject reqBody, String tableName) {
        switch (getDBType()) {
            case DatabaseType.POSTGRESQL:
                return PostgresUtils.find(reqBody, tableName);
            default:
                throw new Error(ErrorMsg.INVALID_DB);
        }
    }

    public static Future<JsonObject> findById(String id, String tableName) {
        switch (getDBType()) {
            case DatabaseType.POSTGRESQL:
                return PostgresUtils.findById(id, tableName);
            default:
                throw new Error(ErrorMsg.INVALID_DB);
        }
    }

    public static Future<JsonObject> findByIdAndDelete(String id, String tableName) {
        switch (getDBType()) {
            case DatabaseType.POSTGRESQL:
                return PostgresUtils.findByIdAndDelete(id, tableName);
            default:
                throw new Error(ErrorMsg.INVALID_DB);
        }
    }

    public static Future<JsonObject> findByIdAndUpdate(String id, JsonObject update, String tableName) {
        switch (getDBType()) {
            case DatabaseType.POSTGRESQL:
                return PostgresUtils.findByIdAndUpdate(id, update, tableName);
            default:
                throw new Error(ErrorMsg.INVALID_DB);
        }
    }

    public static String getDBType() {
        EnvConfig config = new EnvConfig(".env");
        return config.getProperty("DATABASE");
    }
}
