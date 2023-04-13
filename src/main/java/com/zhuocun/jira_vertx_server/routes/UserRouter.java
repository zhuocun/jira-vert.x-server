package com.zhuocun.jira_vertx_server.routes;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import com.zhuocun.jira_vertx_server.controllers.UserController;

public class UserRouter {

    public static Router create(Vertx vertx) {
        Router router = Router.router(vertx);
        UserController userController = new UserController();

        // Enable the BodyHandler for parsing request bodies
        router.route().handler(BodyHandler.create());

        // Define the routes and handlers for CRUD operations
        router.get("/").handler(userController::getAllUsers);
        router.post("/").handler(userController::createUser);
        router.get("/:id").handler(userController::getUserById);
        router.put("/:id").handler(userController::updateUserById);
        router.delete("/:id").handler(userController::deleteUserById);

        return router;
    }
}
