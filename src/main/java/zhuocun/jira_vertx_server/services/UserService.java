package zhuocun.jira_vertx_server.services;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import zhuocun.jira_vertx_server.constants.TableName;
import zhuocun.jira_vertx_server.interfaces.IDBUtils;
import java.util.List;
import java.util.stream.Collectors;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UserService {

    private static final String TABLE_NAME = TableName.USER;
    private final IDBUtils dbUtils;

    @Inject
    public UserService(IDBUtils dbUtils) {
        this.dbUtils = dbUtils;
    }

    public Future<JsonObject> get(String userId) {
        return dbUtils.findById(userId, TABLE_NAME);
    }

    public Future<JsonObject> update(String userId, JsonObject updateData) {
        return dbUtils.findById(userId, TABLE_NAME).compose(user -> {
            if (user != null) {
                return dbUtils.findByIdAndUpdate(userId, updateData, TABLE_NAME);
            } else {
                return Future.failedFuture("User not found");
            }
        });
    }

    public Future<List<JsonObject>> getMembers() {
        return dbUtils.find(new JsonObject(), TABLE_NAME);
    }

    public Future<JsonObject> switchLikeStatus(String userId, String projectId) {
        return dbUtils.findById(userId, TABLE_NAME).compose(user -> {
            if (user != null) {
                List<String> likedProjects = user.getJsonArray("likedProjects").stream()
                        .map(Object::toString).collect(Collectors.toList());
                if (likedProjects.contains(projectId)) {
                    likedProjects.remove(projectId);
                } else {
                    likedProjects.add(projectId);
                }
                JsonObject updateData = new JsonObject().put("likedProjects", likedProjects);
                return dbUtils.findByIdAndUpdate(userId, updateData, TABLE_NAME);
            } else {
                return Future.failedFuture("User not found");
            }
        });
    }
}
