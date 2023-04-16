package com.zhuocun.jira_vertx_server.routes;

import io.vertx.ext.web.handler.JWTAuthHandler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.Router;

public class MainRouter {

    private static UserRouter userRouter = new UserRouter();

    public Router create(Vertx vertx) {
        Router mainRouter = Router.router(vertx);

        JWTAuthOptions jwtAuthOptions = new JWTAuthOptions().addPubSecKey(new PubSecKeyOptions()
                .setAlgorithm("HS256").setBuffer(Buffer.buffer("your_secret_key_here")));

        JWTAuth authProvider = JWTAuth.create(vertx, jwtAuthOptions);
        JWTAuthHandler jwtAuthHandler = JWTAuthHandler.create(authProvider);

        mainRouter.route("/users*").handler(jwtAuthHandler).subRouter(userRouter.create(vertx));

        return mainRouter;
    }
}
