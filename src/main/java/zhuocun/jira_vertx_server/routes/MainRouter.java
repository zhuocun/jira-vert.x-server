package zhuocun.jira_vertx_server.routes;

import com.google.inject.Inject;
// import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.core.Vertx;
// import io.vertx.core.buffer.Buffer;
// import io.vertx.ext.auth.PubSecKeyOptions;
// import io.vertx.ext.auth.jwt.JWTAuth;
// import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;

public class MainRouter {

    private final UserRouter userRouter;

    @Inject
    public MainRouter(UserRouter userRouter) {
        this.userRouter = userRouter;
    }

    public Router create(Vertx vertx) {
        Router mainRouter = Router.router(vertx);

        // JWTAuthOptions jwtAuthOptions = new JWTAuthOptions().addPubSecKey(new PubSecKeyOptions()
        // .setAlgorithm("HS256").setBuffer(Buffer.buffer("your_secret_key_here")));

        // JWTAuth authProvider = JWTAuth.create(vertx, jwtAuthOptions);
        // JWTAuthHandler jwtAuthHandler = JWTAuthHandler.create(authProvider);

        mainRouter.route("/users*").subRouter(userRouter.create(vertx));

        return mainRouter;
    }
}
