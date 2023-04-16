package com.zhuocun.jira_vertx_server.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import java.util.logging.Logger;
import com.zhuocun.jira_vertx_server.routes.MainRouter;

public class MainVerticle extends AbstractVerticle {

    private static final Logger logger = Logger.getLogger(MainVerticle.class.getName());

    @Override
    public void start(Promise<Void> startPromise) {
        MainRouter mainRouter = new MainRouter();
        vertx.deployVerticle(new DBVerticle(), databaseDeployment -> {
            if (databaseDeployment.failed()) {
                startPromise.fail(databaseDeployment.cause());
                return;
            }

            Router router = mainRouter.create(vertx);
            HttpServerOptions serverOptions = new HttpServerOptions().setCompressionSupported(true);

            vertx.createHttpServer(serverOptions).requestHandler(router).listen(8080)
                    .onSuccess(server -> {
                        logger.info("HTTP server running on port " + server.actualPort());
                        startPromise.complete();
                    }).onFailure(startPromise::fail);
        });
    }
}
