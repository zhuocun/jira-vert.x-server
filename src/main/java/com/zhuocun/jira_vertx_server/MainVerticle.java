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
    DBInitialiser dbInitialiser = new DBInitialiser(vertx);
    MainRouter mainRouter = new MainRouter();
    dbInitialiser.initDB().compose(v -> {
      Router router = mainRouter.create(vertx);
      HttpServerOptions serverOptions = new HttpServerOptions().setCompressionSupported(true);

      return vertx.createHttpServer(serverOptions).requestHandler(router).listen(8080)
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
