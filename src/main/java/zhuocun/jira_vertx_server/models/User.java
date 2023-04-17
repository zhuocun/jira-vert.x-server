package zhuocun.jira_vertx_server.models;

import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class User {
    private String id;
    private String username;
    private String email;
    private String password;
    private String[] likedProjects;
    private String createdAt;
    private String updatedAt;

    public User(JsonObject json) {
        this(json.getString("_id"), json.getString("username"), json.getString("email"),
                json.getString("password"), json.getJsonArray("likedProjects").stream()
                        .map(Object::toString).toArray(String[]::new),
                json.getString("createdAt"), json.getString("updatedAt"));
    }
}
