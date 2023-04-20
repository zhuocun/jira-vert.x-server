package zhuocun.jira_vertx_server.interfaces;

import java.util.List;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

public interface IDBUtils {

    public abstract Future<JsonObject> findOne(JsonObject reqBody, String tableName);

    public abstract Future<Void> createItem(JsonObject item, String tableName);

    public abstract Future<List<JsonObject>> find(JsonObject reqBody, String tableName);

    public abstract Future<JsonObject> findById(String id, String tableName);

    public abstract Future<JsonObject> findByIdAndDelete(String id, String tableName);

    public abstract Future<JsonObject> findByIdAndUpdate(String id, JsonObject update,
            String tableName);
}

