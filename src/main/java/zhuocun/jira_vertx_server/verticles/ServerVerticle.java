package zhuocun.jira_vertx_server.verticles;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import java.util.logging.Logger;
import zhuocun.jira_vertx_server.routes.MainRouter;

public class ServerVerticle extends AbstractVerticle {

    private static final Logger logger = Logger.getLogger(ServerVerticle.class.getName());

    @Override
    public void start() {
        // Create the router
        MainRouter mainRouter = new MainRouter();
        Router router = mainRouter.create(vertx);

        // Start the server
        HttpServerOptions serverOptions = new HttpServerOptions().setCompressionSupported(true);
        vertx.createHttpServer(serverOptions).requestHandler(router).listen(8080)
                .onSuccess(
                        server -> logger.info("HTTP server running on port " + server.actualPort()))
                .onFailure(throwable -> logger
                        .severe("Failed to start HTTP server: " + throwable.getMessage()));
    }
}

