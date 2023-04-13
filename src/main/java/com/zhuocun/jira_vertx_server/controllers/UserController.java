package com.zhuocun.jira_vertx_server.controllers;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import com.zhuocun.jira_vertx_server.services.UserService;

public class UserController {

    private UserService userService;

    public UserController() {
        this.userService = new UserService();
    }

    public void getAllUsers(RoutingContext ctx) {
        userService.getAllUsers()
                .onSuccess(users -> ctx.json(users))
                .onFailure(ctx::fail);
    }

    public void createUser(RoutingContext ctx) {
        JsonObject body = ctx.body().asJsonObject();
        userService.createUser(body)
                .onSuccess(v -> ctx.response().setStatusCode(201).end())
                .onFailure(ctx::fail);
    }

    public void getUserById(RoutingContext ctx) {
        String id = ctx.pathParam("id");
        userService.getUserById(id)
                .onSuccess(user -> {
                    if (user != null) {
                        ctx.json(user);
                    } else {
                        ctx.response().setStatusCode(404).end();
                    }
                })
                .onFailure(ctx::fail);
    }

    public void updateUserById(RoutingContext ctx) {
        String id = ctx.pathParam("id");
        JsonObject update = ctx.body().asJsonObject();
        userService.updateUserById(id, update)
                .onSuccess(user -> {
                    if (user != null) {
                        ctx.json(user);
                    } else {
                        ctx.response().setStatusCode(404).end();
                    }
                })
                .onFailure(ctx::fail);
    }

    public void deleteUserById(RoutingContext ctx) {
        String id = ctx.pathParam("id");
        userService.deleteUserById(id)
                .onSuccess(user -> {
                    if (user != null) {
                        ctx.json(user);
                    } else {
                        ctx.response().setStatusCode(404).end();
                    }
                })
                .onFailure(ctx::fail);
    }
}
