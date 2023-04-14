package com.zhuocun.jira_vertx_server.routes;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;

public class MainRouter {

    private static UserRouter userRouter = new UserRouter();

    public Router create(Vertx vertx) {
        Router mainRouter = Router.router(vertx);

        mainRouter.route("/users*").handler(ctx -> ctx.next()).subRouter(userRouter.create(vertx));

        return mainRouter;
    }
}
