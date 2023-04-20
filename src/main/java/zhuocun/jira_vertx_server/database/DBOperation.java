package zhuocun.jira_vertx_server.database;

import java.util.List;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import zhuocun.jira_vertx_server.database.crud.IDBUtils;

@Singleton
public class DBOperation {

    private final IDBUtils dbUtils;

    @Inject
    public DBOperation(IDBUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

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

    public Future<JsonObject> findByIdAndUpdate(String id, JsonObject update, String tableName) {
        return dbUtils.findByIdAndUpdate(id, update, tableName);
    }
}
