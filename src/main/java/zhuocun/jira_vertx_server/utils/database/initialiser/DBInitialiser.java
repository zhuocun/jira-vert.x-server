package zhuocun.jira_vertx_server.utils.database.initialiser;

import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.sqlclient.Pool;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public interface DBInitialiser {

    Pool getDbPool();

    DynamoDbClient getDynamoDBClient();

    Future<Object> initDB(Vertx vertx);

}

