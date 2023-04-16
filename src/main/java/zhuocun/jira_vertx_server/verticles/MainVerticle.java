package zhuocun.jira_vertx_server.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        // Deploy the DBVerticle
        vertx.deployVerticle(new DBVerticle(), dbDeployment -> {
            if (dbDeployment.failed()) {
                startPromise.fail(dbDeployment.cause());
                return;
            }

            // Deploy the ServerVerticle
            vertx.deployVerticle(new ServerVerticle(), routerDeployment -> {
                if (routerDeployment.failed()) {
                    startPromise.fail(routerDeployment.cause());
                    return;
                }

                startPromise.complete();
            });
        });
    }
}
