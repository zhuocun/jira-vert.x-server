package com.zhuocun.jira_vertx_server;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.ext.web.Router;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;

import com.zhuocun.jira_vertx_server.router.ApiRouter;
import com.zhuocun.jira_vertx_server.utils.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import com.zhuocun.jira_vertx_server.dao.UserDao;
import io.vertx.pgclient.SslMode;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {
    Config config = new Config(".env");

    // Configure the PostgreSQL database connection
    PgConnectOptions connectOptions = new PgConnectOptions()
        .setPort(Integer.parseInt(config.getProperty("POSTGRES_PORT")))
        .setHost(config.getProperty("POSTGRES_HOST"))
        .setDatabase(config.getProperty("POSTGRES_DATABASE"))
        .setUser(config.getProperty("POSTGRES_USER"))
        .setPassword(config.getProperty("POSTGRES_PASSWORD"))
        .setSslMode(SslMode.REQUIRE)
        .setPemTrustOptions(new PemTrustOptions()
            .addCertPath("certs/global-bundle.pem"));
    PoolOptions poolOptions = new PoolOptions().setMaxSize(10);
    PgPool pgPool = PgPool.pool(vertx, connectOptions, poolOptions);
    UserDao userDao = new UserDao(pgPool);
    List<String> schemaFiles = listResourceFiles("/db/schema/");
    executeSqlScripts(pgPool, schemaFiles, startPromise, 0, userDao);
  }

  private List<String> listResourceFiles(String path) throws Exception {
    List<String> result = new ArrayList<>();

    try {
      URI uri = getClass().getResource(path).toURI();
      Path dirPath = Paths.get(uri);
      try (Stream<Path> paths = Files.list(dirPath)) {
        paths.filter(Files::isRegularFile)
            .forEach(p -> result.add(p.getFileName().toString()));
      }
    } catch (URISyntaxException | IOException e) {
      e.printStackTrace();
      throw new Exception("Failed to list resource files", e);
    }

    return result;
  }

  private void executeSqlScripts(PgPool pgPool, List<String> schemaFiles, Promise<Void> startPromise, int index,
      UserDao userDao) throws IOException {
    if (index >= schemaFiles.size()) {
      // Start the HTTP server and set up the API router after executing all SQL
      // scripts
      Router router = ApiRouter.create(vertx, userDao);
      vertx.createHttpServer().requestHandler(router).listen(8888, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          System.out.println("HTTP server started on port 8888");
        } else {
          startPromise.fail(http.cause());
        }
      });
      return;
    }

    String fileName = schemaFiles.get(index);
    InputStream inputStream = getClass().getResourceAsStream("/db/schema/" + fileName);
    Buffer sqlScriptBuffer = Buffer.buffer(inputStream.readAllBytes());
    String sqlScript = sqlScriptBuffer.toString();

    pgPool.query(sqlScript).execute(ar -> {
      if (ar.succeeded()) {
        System.out.println("Table created/checked successfully: " + fileName);
        try {
          executeSqlScripts(pgPool, schemaFiles, startPromise, index + 1, userDao);
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        System.out.println("Failed to create/check table: " + fileName + ", " + ar.cause().getMessage());
        startPromise.fail(ar.cause());
      }
    });
  }
}
