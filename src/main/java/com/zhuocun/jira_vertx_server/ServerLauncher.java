package com.zhuocun.jira_vertx_server;

import io.vertx.core.Vertx;

public class ServerLauncher {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle(), ar -> {
            if (ar.succeeded()) {
                System.out.println("MainVerticle deployment successful.");
            } else {
                System.out.println("MainVerticle deployment failed.");
            }
        });
    }
}
