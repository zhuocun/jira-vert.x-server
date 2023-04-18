package zhuocun.jira_vertx_server.verticles;

import zhuocun.jira_vertx_server.utils.database.DBInitialiser;
import com.google.inject.Inject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import zhuocun.jira_vertx_server.config.EnvConfig;
import zhuocun.jira_vertx_server.constants.DatabaseType;

public class DBVerticle extends AbstractVerticle {

    private Pool dbPool;

    private DynamoDbClient dynamoDBClient;

    private final DBInitialiser dbInitialiser;

    private final EnvConfig envConfig;

    @Inject
    public DBVerticle(DBInitialiser dbInitialiser, EnvConfig envConfig) {
        this.dbInitialiser = dbInitialiser;
        this.envConfig = envConfig;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        dbInitialiser.initDB(vertx).onSuccess(result -> {
            switch (envConfig.getDBType()) {
                case DatabaseType.POSTGRESQL:
                    dbPool = dbInitialiser.getDbPool();
                    break;
                case DatabaseType.MONGO_DB:
                    break;
                case DatabaseType.DYNAMO_DB:
                    dynamoDBClient = dbInitialiser.getDynamoDBClient();
                    break;
                default:
                    break;
            }
            startPromise.complete();
        }).onFailure(startPromise::fail);
    }

    @Override
    public void stop() throws Exception {
        if (dbPool != null) {
            dbPool.close();
        }
        if (dynamoDBClient != null) {
            dynamoDBClient.close();
        }
    }
}

