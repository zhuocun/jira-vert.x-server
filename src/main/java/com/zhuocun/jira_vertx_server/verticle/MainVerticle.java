package com.zhuocun.jira_vertx_server.verticle;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.ext.web.Router;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;

import com.zhuocun.jira_vertx_server.router.ApiRouter;
import com.zhuocun.jira_vertx_server.dao.UserDao;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    PgConnectOptions connectOptions = new PgConnectOptions()
        .setPort(5432)
        .setHost("localhost")
        .setDatabase("postgres")
        .setUser("postgres")
        .setPassword("postgres");

    PoolOptions poolOptions = new PoolOptions().setMaxSize(10);
    PgPool pgPool = PgPool.pool(vertx, connectOptions, poolOptions);
    UserDao userDao = new UserDao(pgPool);
    Router router = ApiRouter.create(vertx, userDao);
    vertx.createHttpServer().requestHandler(router).listen(8888, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTP server started on port 8888");
      } else {
        startPromise.fail(http.cause());
      }
    });
  }
}
