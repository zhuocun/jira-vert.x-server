// package zhuocun.jira_vertx_server.utils.database.crud;

// import io.vertx.sqlclient.Pool;
// import lombok.experimental.UtilityClass;
// import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
// import zhuocun.jira_vertx_server.constants.DatabaseType;
// import zhuocun.jira_vertx_server.constants.MyError;
// import zhuocun.jira_vertx_server.utils.database.DBOperation;
// import zhuocun.jira_vertx_server.utils.exceptions.MyRuntimeException;

// @UtilityClass
// public final class DBUtilsFactory {

// public static AbstractDbUtils createDBUtils(Pool postgresPool, DynamoDbClient dynamoDBClient) {
// String dbType = DBOperation.getDBType();
// switch (dbType) {
// case DatabaseType.POSTGRESQL:
// PostgresUtils postgresUtils = new PostgresUtils(postgresPool);
// return postgresUtils;
// case DatabaseType.MONGO_DB:
// MongoDBUtils mongoDBUtils = new MongoDBUtils();
// return mongoDBUtils;
// case DatabaseType.DYNAMO_DB:
// DynamoDBUtils dynamoDbUtils = new DynamoDBUtils(dynamoDBClient);
// return dynamoDbUtils;
// default:
// throw new MyRuntimeException(MyError.INVALID_DB);
// }
// }
// }
