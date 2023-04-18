package zhuocun.jira_vertx_server.routes;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import io.vertx.core.Vertx;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;
import zhuocun.jira_vertx_server.controllers.UserController;

@Singleton
public class UserRouter {

    private final UserController userController;

    @Inject
    public UserRouter(UserController userController) {
        this.userController = userController;
    }

    public Router create(Vertx vertx) {
        Router router = Router.router(vertx);
        router.route().handler(BodyHandler.create());

        router.get("/").handler(userController::get);
        router.put("/").handler(userController::update);
        router.get("/members").handler(userController::getMembers);
        router.post("/switch-like-status").handler(userController::switchLikeStatus);

        return router;
    }
}
