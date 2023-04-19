package zhuocun.jira_vertx_server;

import io.vertx.core.Vertx;
import lombok.extern.slf4j.Slf4j;
import zhuocun.jira_vertx_server.verticles.MainVerticle;

@Slf4j
public class ServerLauncher {

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new MainVerticle(), ar -> {
            if (ar.succeeded()) {
                log.info("MainVerticle deployment successful.");
            } else {
                log.error("MainVerticle deployment failed.");
                ar.cause().printStackTrace();
            }
        });
    }
}
