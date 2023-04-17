package zhuocun.jira_vertx_server.utils.database;

import java.util.List;

import zhuocun.jira_vertx_server.config.EnvConfig;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import lombok.experimental.UtilityClass;
import zhuocun.jira_vertx_server.utils.database.crud.AbstractDbUtils;
import zhuocun.jira_vertx_server.utils.database.crud.DBUtilsFactory;

@UtilityClass
public class DBOperation {

    private DBUtilsFactory dbUtilsFactory = DBUtilsFactory.getDBUtilsFactory();
    private AbstractDbUtils dbUtils = dbUtilsFactory.createDBUtils();

    public static Future<JsonObject> findOne(JsonObject reqBody, String tableName) {
        return dbUtils.findOne(reqBody, tableName);
    }

    public static Future<Void> createItem(JsonObject item, String tableName) {
        return dbUtils.createItem(item, tableName);
    }

    public static Future<List<JsonObject>> find(JsonObject reqBody, String tableName) {
        return dbUtils.find(reqBody, tableName);
    }

    public static Future<JsonObject> findById(String id, String tableName) {
        return dbUtils.findById(id, tableName);
    }

    public static Future<JsonObject> findByIdAndDelete(String id, String tableName) {
        return dbUtils.findByIdAndDelete(id, tableName);
    }

    public static Future<JsonObject> findByIdAndUpdate(String id, JsonObject update,
            String tableName) {
        return dbUtils.findByIdAndUpdate(id, update, tableName);
    }

    public static String getDBType() {
        EnvConfig config = new EnvConfig();
        return config.getProperty("DATABASE");
    }

    public static void setDbUtils(AbstractDbUtils u) {
        dbUtils = u;
    }
}
