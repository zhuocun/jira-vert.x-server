package com.zhuocun.jira_vertx_server.services;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import com.zhuocun.jira_vertx_server.constants.TableName;
import com.zhuocun.jira_vertx_server.utils.database.DBUtils;

import java.util.List;

public class UserService {

    private static final String TABLE_NAME = TableName.USER;

    public Future<List<JsonObject>> getAllUsers() {
        return DBUtils.find(new JsonObject(), TABLE_NAME);
    }

    public Future<Void> createUser(JsonObject user) {
        return DBUtils.createItem(user, TABLE_NAME);
    }

    public Future<JsonObject> getUserById(String _id) {
        return DBUtils.findById(_id, TABLE_NAME);
    }

    public Future<JsonObject> updateUserById(String _id, JsonObject update) {
        return DBUtils.findByIdAndUpdate(_id, update, TABLE_NAME);
    }

    public Future<JsonObject> deleteUserById(String _id) {
        return DBUtils.findByIdAndDelete(_id, TABLE_NAME);
    }
}
