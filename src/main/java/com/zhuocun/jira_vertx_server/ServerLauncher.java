package com.zhuocun.jira_vertx_server;

import io.vertx.core.Vertx;
import java.util.logging.Logger;

public class ServerLauncher {
    private static final Logger logger = Logger.getLogger(ServerLauncher.class.getName());

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle(), ar -> {
            if (ar.succeeded()) {
                logger.info("MainVerticle deployment successful.");
            } else {
                logger.warning("MainVerticle deployment failed.");
            }
        });
    }
}
