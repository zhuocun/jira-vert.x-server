package zhuocun.jira_vertx_server.modules;

import com.google.inject.AbstractModule;
import io.vertx.sqlclient.Pool;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import zhuocun.jira_vertx_server.config.EnvConfig;
import zhuocun.jira_vertx_server.constants.DatabaseType;
import zhuocun.jira_vertx_server.constants.MyError;
import zhuocun.jira_vertx_server.controllers.UserController;
import zhuocun.jira_vertx_server.routes.MainRouter;
import zhuocun.jira_vertx_server.routes.UserRouter;
import zhuocun.jira_vertx_server.services.UserService;
import zhuocun.jira_vertx_server.utils.database.DBInitialiser;
import zhuocun.jira_vertx_server.utils.database.DBOperation;
import zhuocun.jira_vertx_server.utils.database.crud.AbstractDbUtils;
import zhuocun.jira_vertx_server.utils.database.crud.DynamoDBUtils;
import zhuocun.jira_vertx_server.utils.database.crud.MongoDBUtils;
import zhuocun.jira_vertx_server.utils.database.crud.PostgresUtils;
import zhuocun.jira_vertx_server.utils.database.providers.DynamoDBClientProvider;
import zhuocun.jira_vertx_server.utils.database.providers.PoolProvider;
import zhuocun.jira_vertx_server.utils.exceptions.MyRuntimeException;

public class DBModule extends AbstractModule {

    private final EnvConfig envConfig;

    private final DBInitialiser dbInitialiser;

    public DBModule(EnvConfig envConfig, DBInitialiser dbInitialiser) {
        this.envConfig = envConfig;
        this.dbInitialiser = dbInitialiser;
    }

    @Override
    protected void configure() {
        bind(DBInitialiser.class).toInstance(dbInitialiser);
        bind(DBOperation.class).asEagerSingleton();
        bind(UserService.class).asEagerSingleton();
        bind(UserController.class).asEagerSingleton();
        bind(UserRouter.class).asEagerSingleton();
        bind(MainRouter.class).asEagerSingleton();

        // factory design pattern
        switch (envConfig.getDBType()) {
            case DatabaseType.POSTGRESQL:
                bind(Pool.class).toProvider(PoolProvider.class);
                bind(AbstractDbUtils.class).to(PostgresUtils.class);
                break;
            case DatabaseType.MONGO_DB:
                bind(AbstractDbUtils.class).to(MongoDBUtils.class);
                break;
            case DatabaseType.DYNAMO_DB:
                bind(DynamoDbClient.class).toProvider(DynamoDBClientProvider.class);
                bind(AbstractDbUtils.class).to(DynamoDBUtils.class);
                break;
            default:
                throw new MyRuntimeException(MyError.INVALID_DB);
        }
    }

}
