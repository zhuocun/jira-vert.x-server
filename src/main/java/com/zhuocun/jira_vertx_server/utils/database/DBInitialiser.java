package com.zhuocun.jira_vertx_server.utils.database;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.pgclient.SslMode;
import io.vertx.sqlclient.PoolOptions;

import com.zhuocun.jira_vertx_server.config.EnvConfig;
import com.zhuocun.jira_vertx_server.constants.EDatabase;

public class DBInitialiser {

    private final Vertx vertx;
    private static PgPool postgresPool;

    public static PgPool getPostgresPool() {
        return postgresPool;
    }

    public DBInitialiser(Vertx vertx) {
        this.vertx = vertx;
    }

    public Future<Object> initDB() {
        EnvConfig config = new EnvConfig(".env");
        String database = config.getProperty("DATABASE").toUpperCase();
        switch (EDatabase.valueOf(database)) {
            case POSTGRESQL:
                return initPostgreSQL(config);
            case MONGODB:
                return initMongoDB(config);
            default:
                return Future.failedFuture("Unknown database");
        }
    }

    private Future<Object> initPostgreSQL(EnvConfig config) {
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
        postgresPool = PgPool.pool(vertx, connectOptions, poolOptions);
        return postgresPool.getConnection().compose(conn -> {
            System.out.println("Connected to PostgreSQL database");
            return Future.succeededFuture();
        }).onFailure(cause -> {
            System.out.printf("Failed to connect to PostgreSQL database", cause);
            postgresPool.close();
        });
    }

    private Future<Object> initMongoDB(EnvConfig config) {
        // TODO: Implement MongoDB initialization logic
        return Future.failedFuture("MongoDB initialization not implemented");
    }

}
