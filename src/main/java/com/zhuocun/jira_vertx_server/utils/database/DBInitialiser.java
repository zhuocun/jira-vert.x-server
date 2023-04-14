package com.zhuocun.jira_vertx_server.utils.database;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.pgclient.SslMode;
import io.vertx.sqlclient.PoolOptions;
import java.util.logging.Logger;
import com.zhuocun.jira_vertx_server.config.EnvConfig;
import com.zhuocun.jira_vertx_server.constants.DatabaseType;

public class DBInitialiser {

    private static final Logger logger = Logger.getLogger(DBInitialiser.class.getName());
    private PgPool postgresPool;

    public PgPool getPostgresPool() {
        return postgresPool;
    }

    public Future<Object> initDB(Vertx vertx) {
        EnvConfig config = new EnvConfig(".env");
        switch (DBUtils.getDBType()) {
            case DatabaseType.POSTGRESQL:
                return initPostgreSQL(config, vertx);
            case DatabaseType.MONGO_DB:
                return initMongoDB(config);
            default:
                return Future.failedFuture("Unknown database");
        }
    }

    private Future<Object> initPostgreSQL(EnvConfig config, Vertx vertx) {
        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(Integer.parseInt(config.getProperty("POSTGRES_PORT")))
                .setHost(config.getProperty("POSTGRES_HOST"))
                .setDatabase(config.getProperty("POSTGRES_DATABASE"))
                .setUser(config.getProperty("POSTGRES_USER"))
                .setPassword(config.getProperty("POSTGRES_PASSWORD"))
                .setSslMode(SslMode.REQUIRE)
                .setPemTrustOptions(new PemTrustOptions()
                        .addCertPath("src/main/resources/certs/global-bundle.pem"));
        PoolOptions poolOptions = new PoolOptions().setMaxSize(10);
        postgresPool = PgPool.pool(vertx, connectOptions, poolOptions);
        return postgresPool.getConnection().compose(conn -> {
            logger.info("Connected to PostgreSQL database");
            return Future.succeededFuture();
        }).onFailure(cause -> {
            logger.warning("Failed to connect to PostgreSQL database" + cause);
            postgresPool.close();
        });
    }

    private Future<Object> initMongoDB(EnvConfig config) {
        // TODO: Implement MongoDB initialization logic
        return Future.failedFuture("MongoDB initialization not implemented");
    }

}
