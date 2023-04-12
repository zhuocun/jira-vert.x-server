package com.zhuocun.jira_vertx_server.router;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import com.zhuocun.jira_vertx_server.dao.UserDao;
import com.zhuocun.jira_vertx_server.model.User;

public class ApiRouter {

    public static Router create(Vertx vertx, UserDao userDao) {
        Router router = Router.router(vertx);

        // Enable the BodyHandler for parsing request bodies
        router.route().handler(BodyHandler.create());

        // Define the routes and handlers for CRUD operations
        router.get("/users/:id").handler(ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            userDao.getUserById(id, ar -> {
                if (ar.succeeded()) {
                    User user = ar.result();
                    if (user != null) {
                        ctx.response()
                                .putHeader("content-type", "application/json")
                                .setStatusCode(200)
                                .end(JsonObject.mapFrom(user).encodePrettily());
                    } else {
                        ctx.response().setStatusCode(404).end();
                    }
                } else {
                    ctx.response().setStatusCode(500).end();
                }
            });
        });

        router.post("/users").handler(ctx -> {
            JsonObject json = ctx.body().asJsonObject();
            User user = new User(json.getString("name"), json.getString("email"));
            userDao.createUser(user, ar -> {
                if (ar.succeeded()) {
                    int id = ar.result();
                    ctx.response()
                            .putHeader("content-type", "application/json")
                            .setStatusCode(201)
                            .end(new JsonObject().put("id", id).encodePrettily());
                } else {
                    ctx.response().setStatusCode(500).end();
                }
            });
        });

        router.put("/users/:id").handler(ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            JsonObject json = ctx.body().asJsonObject();
            User updatedUser = new User(json.getString("name"), json.getString("email"));
            userDao.updateUser(id, updatedUser, ar -> {
                if (ar.succeeded()) {
                    ctx.response().setStatusCode(204).end();
                } else {
                    ctx.response().setStatusCode(500).end();
                }
            });
        });

        router.delete("/users/:id").handler(ctx -> {
            int id = Integer.parseInt(ctx.pathParam("id"));
            userDao.deleteUser(id, ar -> {
                if (ar.succeeded()) {
                    ctx.response().setStatusCode(204).end();
                } else {
                    ctx.response().setStatusCode(500).end();
                }
            });
        });

        return router;
    }
}
