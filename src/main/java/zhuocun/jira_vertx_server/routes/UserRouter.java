package zhuocun.jira_vertx_server.routes;

import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import zhuocun.jira_vertx_server.controllers.UserController;

public class UserRouter {

    public Router create(Vertx vertx) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/").handler(UserController::get);
        router.put("/").handler(UserController::update);
        router.get("/members").handler(UserController::getMembers);
        router.post("/switch-like-status").handler(UserController::switchLikeStatus);

        return router;
    }
}
