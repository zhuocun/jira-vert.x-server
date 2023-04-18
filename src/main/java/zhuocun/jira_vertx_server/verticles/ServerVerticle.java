package zhuocun.jira_vertx_server.verticles;

import com.google.inject.Inject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import lombok.extern.slf4j.Slf4j;
import zhuocun.jira_vertx_server.routes.MainRouter;

@Slf4j
public class ServerVerticle extends AbstractVerticle {

    private final MainRouter mainRouter;

    @Inject
    public ServerVerticle(MainRouter mainRouter) {
        this.mainRouter = mainRouter;
    }

    @Override
    public void start() {
        Router router = mainRouter.create(vertx);

        // Start the server
        HttpServerOptions serverOptions = new HttpServerOptions().setCompressionSupported(true);
        vertx.createHttpServer(serverOptions).requestHandler(router).listen(8080)
                .onSuccess(server -> log.info("HTTP server running on port " + server.actualPort()))
                .onFailure(throwable -> log
                        .error("Failed to start HTTP server: " + throwable.getMessage()));
    }
}

