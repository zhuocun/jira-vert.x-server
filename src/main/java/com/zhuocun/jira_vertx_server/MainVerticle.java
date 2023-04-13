package com.zhuocun.jira_vertx_server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import com.zhuocun.jira_vertx_server.routes.MainRouter;
import com.zhuocun.jira_vertx_server.utils.database.DBInitialiser;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) {
    // Initialize the database
    DBInitialiser dbInitialiser = new DBInitialiser(vertx);
    dbInitialiser.initDB(startPromise);

    startPromise.future().compose(v -> {
      // Create the main router
      Router mainRouter = MainRouter.create(vertx);

      // Configure the HTTP server options
      HttpServerOptions serverOptions = new HttpServerOptions()
          .setCompressionSupported(true);

      // Start the HTTP server
      return vertx.createHttpServer(serverOptions)
          .requestHandler(mainRouter)
          .listen(8080)
          .compose(server -> {
            System.out.printf("HTTP server running on port %d%n", server.actualPort());
            return Future.succeededFuture();
          });
    }).onComplete(ar -> {
      if (ar.succeeded()) {
        startPromise.complete();
      } else {
        startPromise.fail(ar.cause());
      }
    });
  }
}
