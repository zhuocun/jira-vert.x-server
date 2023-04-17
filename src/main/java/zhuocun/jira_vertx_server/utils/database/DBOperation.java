package zhuocun.jira_vertx_server.utils.database;

import java.util.List;
import zhuocun.jira_vertx_server.config.EnvConfig;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import lombok.Setter;
import lombok.experimental.UtilityClass;
import zhuocun.jira_vertx_server.utils.database.crud.AbstractDbUtils;

@UtilityClass
@Setter
public class DBOperation {

    private AbstractDbUtils dbUtils;

    public Future<JsonObject> findOne(JsonObject reqBody, String tableName) {
        return dbUtils.findOne(reqBody, tableName);
    }

    public Future<Void> createItem(JsonObject item, String tableName) {
        return dbUtils.createItem(item, tableName);
    }

    public Future<List<JsonObject>> find(JsonObject reqBody, String tableName) {
        return dbUtils.find(reqBody, tableName);
    }

    public Future<JsonObject> findById(String id, String tableName) {
        return dbUtils.findById(id, tableName);
    }

    public Future<JsonObject> findByIdAndDelete(String id, String tableName) {
        return dbUtils.findByIdAndDelete(id, tableName);
    }

    public Future<JsonObject> findByIdAndUpdate(String id, JsonObject update,
            String tableName) {
        return dbUtils.findByIdAndUpdate(id, update, tableName);
    }

    public String getDBType() {
        EnvConfig config = new EnvConfig();
        return config.getProperty("DATABASE");
    }

    public static void setDbUtils(AbstractDbUtils dbUtils) {
        DBOperation.dbUtils = dbUtils;
    }
}
