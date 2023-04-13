package com.zhuocun.jira_vertx_server.models;

import io.vertx.core.json.JsonObject;

public class User {
    private String _id;
    private String username;
    private String email;
    private String password;
    private String[] likedProjects;
    private String createdAt;
    private String updatedAt;

    public User(String _id, String username, String email, String password, String[] likedProjects, String createdAt,
            String updatedAt) {
        this._id = _id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.likedProjects = likedProjects;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public User(JsonObject json) {
        this(
                json.getString("_id"),
                json.getString("username"),
                json.getString("email"),
                json.getString("password"),
                json.getJsonArray("likedProjects").stream().map(Object::toString).toArray(String[]::new),
                json.getString("createdAt"),
                json.getString("updatedAt"));
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.put("_id", this._id);
        json.put("username", this.username);
        json.put("email", this.email);
        json.put("password", this.password);
        json.put("likedProjects", this.likedProjects);
        json.put("createdAt", this.createdAt);
        json.put("updatedAt", this.updatedAt);
        return json;
    }

    public String get_id() {
        return this._id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getLikedProjects() {
        return this.likedProjects;
    }

    public void setLikedProjects(String[] likedProjects) {
        this.likedProjects = likedProjects;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return this.updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
