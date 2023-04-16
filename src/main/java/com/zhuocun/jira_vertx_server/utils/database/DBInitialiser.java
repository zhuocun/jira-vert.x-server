package com.zhuocun.jira_vertx_server.utils.database;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.pgclient.SslMode;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import java.util.logging.Logger;
import com.zhuocun.jira_vertx_server.config.EnvConfig;
import com.zhuocun.jira_vertx_server.constants.DatabaseType;
import com.zhuocun.jira_vertx_server.constants.MyError;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

public class DBInitialiser {

    private static final Logger logger = Logger.getLogger(DBInitialiser.class.getName());
    private PgPool postgresPool;
    private DynamoDbClient dynamoDBClient;

    public Pool getDbPool() {
        switch (DBUtils.getDBType()) {
            case DatabaseType.POSTGRESQL:
                return postgresPool;
            case DatabaseType.MONGO_DB:
                return null;
            case DatabaseType.DYNAMO_DB:
                return null;
            default:
                return null;
        }
    }

    public DynamoDbClient getDynamoDBClient() {
        return dynamoDBClient;
    }

    public Future<Object> initDB(Vertx vertx) {
        EnvConfig config = new EnvConfig(".env");
        String dbType = DBUtils.getDBType();
        switch (dbType) {
            case DatabaseType.POSTGRESQL:
                return initPostgreSQL(config, vertx);
            case DatabaseType.MONGO_DB:
                return initMongoDB(config);
            case DatabaseType.DYNAMO_DB:
                return initDynamoDB(config);
            default:
                return Future.failedFuture(MyError.INVALID_DB + dbType);
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

    private Future<Object> initDynamoDB(EnvConfig config) {
        String accessKeyId = config.getProperty("AWS_ACCESS_KEY_ID");
        String secretAccessKey = config.getProperty("AWS_SECRET_ACCESS_KEY");
        String region = config.getProperty("AWS_REGION");

        try {
            AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKeyId, secretAccessKey);
            StaticCredentialsProvider credentialsProvider =
                    StaticCredentialsProvider.create(awsCreds);

            dynamoDBClient = DynamoDbClient.builder().region(Region.of(region))
                    .credentialsProvider(credentialsProvider).build();

            logger.info("Connected to DynamoDB");
            return Future.succeededFuture();
        } catch (Exception e) {
            logger.warning("Failed to connect to DynamoDB: " + e);
            return Future.failedFuture("Failed to connect to DynamoDB: " + e);
        }
    }

}
