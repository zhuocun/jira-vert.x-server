package zhuocun.jira_vertx_server.verticles;

import zhuocun.jira_vertx_server.utils.database.DBInitialiser;
import zhuocun.jira_vertx_server.utils.database.DBOperation;
import zhuocun.jira_vertx_server.utils.database.crud.AbstractDbUtils;
import zhuocun.jira_vertx_server.utils.database.crud.DBUtilsFactory;
import com.google.inject.Inject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import zhuocun.jira_vertx_server.constants.DatabaseType;

public class DBVerticle extends AbstractVerticle {

    private Pool dbPool;

    private final DBInitialiser dbInitialiser;

    @Inject
    public DBVerticle(DBInitialiser dbInitialiser) {
        this.dbInitialiser = dbInitialiser;
    }

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        dbInitialiser.initDB(vertx).onSuccess(result -> {
            switch (DBOperation.getDBType()) {
                case DatabaseType.POSTGRESQL:
                    dbPool = dbInitialiser.getDbPool();
                    break;
                case DatabaseType.MONGO_DB:
                    break;
                case DatabaseType.DYNAMO_DB:
                    break;
                default:
                    break;
            }
            AbstractDbUtils dbUtils = DBUtilsFactory.createDBUtils(dbInitialiser.getDbPool(),
                    dbInitialiser.getDynamoDBClient());
            DBOperation.setDbUtils(dbUtils);
            startPromise.complete();
        }).onFailure(startPromise::fail);
    }

    @Override
    public void stop() throws Exception {
        if (dbPool != null) {
            dbPool.close();
        }
    }
}

