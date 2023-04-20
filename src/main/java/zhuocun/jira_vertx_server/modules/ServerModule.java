package zhuocun.jira_vertx_server.modules;

import com.google.inject.AbstractModule;
import io.vertx.sqlclient.Pool;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import zhuocun.jira_vertx_server.config.EnvConfig;
import zhuocun.jira_vertx_server.constants.DatabaseType;
import zhuocun.jira_vertx_server.constants.MyError;
import zhuocun.jira_vertx_server.controllers.UserController;
import zhuocun.jira_vertx_server.database.DBInitialiser;
import zhuocun.jira_vertx_server.database.DBOperation;
import zhuocun.jira_vertx_server.database.crud.DynamoDBUtils;
import zhuocun.jira_vertx_server.database.crud.IDBUtils;
import zhuocun.jira_vertx_server.database.crud.MongoDBUtils;
import zhuocun.jira_vertx_server.database.crud.PostgresUtils;
import zhuocun.jira_vertx_server.database.providers.DynamoClientProvider;
import zhuocun.jira_vertx_server.database.providers.PoolProvider;
import zhuocun.jira_vertx_server.routes.MainRouter;
import zhuocun.jira_vertx_server.routes.UserRouter;
import zhuocun.jira_vertx_server.services.UserService;
import zhuocun.jira_vertx_server.utils.exceptions.MyRuntimeException;

public class ServerModule extends AbstractModule {

    private final EnvConfig envConfig;

    private final DBInitialiser dbInitialiser;

    public ServerModule(EnvConfig envConfig, DBInitialiser dbInitialiser) {
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
                bind(IDBUtils.class).to(PostgresUtils.class);
                break;
            case DatabaseType.MONGODB:
                bind(IDBUtils.class).to(MongoDBUtils.class);
                break;
            case DatabaseType.DYNAMODB:
                bind(DynamoDbClient.class).toProvider(DynamoClientProvider.class);
                bind(IDBUtils.class).to(DynamoDBUtils.class);
                break;
            default:
                throw new MyRuntimeException(MyError.INVALID_DB);
        }
    }

}
