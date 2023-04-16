package com.zhuocun.jira_vertx_server.utils.auth;

import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.auth.PubSecKeyOptions;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.jwt.JWTAuth;
import io.vertx.ext.auth.authentication.TokenCredentials;
import io.vertx.ext.auth.jwt.JWTAuthOptions;
import io.vertx.ext.web.RoutingContext;

public class JWTAuthHandler implements Handler<RoutingContext> {

    private JWTAuth jwtAuth;

    public JWTAuthHandler(Vertx vertx) {
        JWTAuthOptions jwtAuthOptions = new JWTAuthOptions().addPubSecKey(new PubSecKeyOptions()
                .setAlgorithm("HS256").setBuffer(Buffer.buffer("your_secret_key_here")));

        jwtAuth = JWTAuth.create(vertx, jwtAuthOptions);
    }

    @Override
    public void handle(RoutingContext ctx) {
        String token = ctx.request().getHeader("Authorization");
        if (token == null) {
            ctx.fail(401);
            return;
        }

        TokenCredentials credentials = new TokenCredentials(token);
        jwtAuth.authenticate(credentials, res -> {
            if (res.succeeded()) {
                User user = res.result();
                ctx.setUser(user);
                ctx.next();
            } else {
                ctx.fail(401);
            }
        });
    }
}
