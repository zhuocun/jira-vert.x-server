package zhuocun.jira_vertx_server.utils.database.crud;

import lombok.Data;
import zhuocun.jira_vertx_server.config.EnvConfig;
import zhuocun.jira_vertx_server.constants.DatabaseType;
import zhuocun.jira_vertx_server.constants.MyError;
import zhuocun.jira_vertx_server.utils.database.DBInitialiser;
import zhuocun.jira_vertx_server.utils.exceptions.MyRuntimeException;

@Data
public class DBUtilsFactory {

    private DBInitialiser dbInitialiser = DBInitialiser.getDbInitialiser();
    private static DBUtilsFactory dbUtilsFactory;

    private DBUtilsFactory() {}

    public static DBUtilsFactory getDBUtilsFactory() {
        if (dbUtilsFactory == null) {
            dbUtilsFactory = new DBUtilsFactory();
        }
        return dbUtilsFactory;
    }

    public AbstractDbUtils createDBUtils() {
        EnvConfig config = new EnvConfig();
        String dbType = config.getProperty("DATABASE");
        try {
            switch (dbType) {
                case DatabaseType.POSTGRESQL:
                    PostgresUtils postgresUtils = new PostgresUtils(dbInitialiser.getDbPool());
                    return postgresUtils;
                case DatabaseType.MONGO_DB:
                    MongoDBUtils mongoDBUtils = new MongoDBUtils();
                    return mongoDBUtils;
                case DatabaseType.DYNAMO_DB:
                    DynamoDbUtils dynamoDbUtils =
                            new DynamoDbUtils(dbInitialiser.getDynamoDBClient());
                    return dynamoDbUtils;
                default:
                    throw new MyRuntimeException(MyError.INVALID_DB);
            }
        } catch (Exception e) {
            throw new MyRuntimeException(MyError.INVALID_DB);
        }
    }
}
