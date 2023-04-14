package com.zhuocun.jira_vertx_server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import java.util.logging.Logger;
import com.zhuocun.jira_vertx_server.routes.MainRouter;
import com.zhuocun.jira_vertx_server.utils.database.DBInitialiser;

public class MainVerticle extends AbstractVerticle {

  private static final Logger logger = Logger.getLogger(MainVerticle.class.getName());

  @Override
  public void start(Promise<Void> startPromise) {
    DBInitialiser dbInitialiser = new DBInitialiser();
    MainRouter mainRouter = new MainRouter();
    dbInitialiser.initDB(vertx).compose(v -> {
      Router router = mainRouter.create(vertx);
      HttpServerOptions serverOptions = new HttpServerOptions().setCompressionSupported(true);

      return vertx.createHttpServer(serverOptions).requestHandler(router).listen(8080)
          .compose(server -> {
            logger.info("HTTP server running on port %d%n" + server.actualPort());
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
