package com.zhuocun.jira_vertx_server.services;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import com.zhuocun.jira_vertx_server.utils.database.DBUtils;

import java.util.List;

public class UserService {

    private static final String TABLE_NAME = "users";

    public Future<List<JsonObject>> getAllUsers() {
        return DBUtils.find(new JsonObject(), TABLE_NAME);
    }

    public Future<Void> createUser(JsonObject user) {
        return DBUtils.createItem(user, TABLE_NAME);
    }

    public Future<JsonObject> getUserById(String id) {
        return DBUtils.findById(id, TABLE_NAME);
    }

    public Future<JsonObject> updateUserById(String id, JsonObject update) {
        return DBUtils.findByIdAndUpdate(id, update, TABLE_NAME);
    }

    public Future<JsonObject> deleteUserById(String id) {
        return DBUtils.findByIdAndDelete(id, TABLE_NAME);
    }
}
