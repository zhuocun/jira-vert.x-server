package zhuocun.jira_vertx_server.utils.database.initialiser;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.net.PemTrustOptions;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.pgclient.SslMode;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.PoolOptions;
import lombok.extern.slf4j.Slf4j;
import zhuocun.jira_vertx_server.config.EnvConfig;
import zhuocun.jira_vertx_server.constants.DatabaseType;
import zhuocun.jira_vertx_server.constants.MyError;
import zhuocun.jira_vertx_server.utils.database.DBOperation;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;

@Slf4j
public class DBInitialiserImpl implements DBInitialiser {

    private PgPool postgresPool;
    private DynamoDbClient dynamoDBClient;
    private static DBInitialiserImpl dbInitialiser;

    private DBInitialiserImpl() {}

    public static DBInitialiserImpl getDbInitialiser() {
        if (dbInitialiser == null) {
            dbInitialiser = new DBInitialiserImpl();
        }
        return dbInitialiser;
    }

    @Override
    public Pool getDbPool() {
        switch (DBOperation.getDBType()) {
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

    @Override
    public DynamoDbClient getDynamoDBClient() {
        return dynamoDBClient;
    }

    @Override
    public Future<Object> initDB(Vertx vertx) {
        EnvConfig config = new EnvConfig();
        String dbType = DBOperation.getDBType();
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
                .setPassword(config.getProperty("POSTGRES_PASSWORD")).setSslMode(SslMode.REQUIRE)
                .setPemTrustOptions(new PemTrustOptions()
                        .addCertPath("src/main/resources/certs/global-bundle.pem"));
        PoolOptions poolOptions = new PoolOptions().setMaxSize(10);
        postgresPool = PgPool.pool(vertx, connectOptions, poolOptions);
        return postgresPool.getConnection().compose(conn -> {
            log.info("Connected to PostgreSQL database");
            return Future.succeededFuture();
        }).onFailure(cause -> {
            log.error("Failed to connect to PostgreSQL database" + cause);
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
            log.info("Connected to DynamoDB");
            return Future.succeededFuture();
        } catch (DynamoDbException e) {
            log.error("Failed to connect to DynamoDB: " + e);
            return Future.failedFuture("Failed to connect to DynamoDB: " + e);
        }
    }

}
