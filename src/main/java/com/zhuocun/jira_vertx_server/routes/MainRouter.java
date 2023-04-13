package com.zhuocun.jira_vertx_server.routes;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class MainRouter {

    public static Router create(Vertx vertx) {
        Router mainRouter = Router.router(vertx);

        // Include the UserRouter
        Router userRouter = UserRouter.create(vertx);
        mainRouter.route("/users*").handler(ctx -> ctx.next()).subRouter(userRouter);

        // Add other model routers here as needed
        // For example:
        // Router projectRouter = ProjectRouter.create(vertx);
        // mainRouter.route("/projects*").handler(ctx ->
        // ctx.next()).subRouter(projectRouter);

        return mainRouter;
    }
}
