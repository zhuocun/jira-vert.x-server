package zhuocun.jira_vertx_server.verticles;

import zhuocun.jira_vertx_server.utils.database.DBOperation;
import zhuocun.jira_vertx_server.utils.database.crud.AbstractDbUtils;
import zhuocun.jira_vertx_server.utils.database.crud.DBUtilsFactory;
import zhuocun.jira_vertx_server.utils.database.initialiser.DBInitialiserImpl;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import zhuocun.jira_vertx_server.constants.DatabaseType;

public class DBVerticle extends AbstractVerticle {

    private Pool dbPool;

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        DBInitialiserImpl dbInitialiser = DBInitialiserImpl.getDbInitialiser();
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
            DBUtilsFactory dbUtilsFactory = DBUtilsFactory.getDBUtilsFactory();
            dbUtilsFactory.setDbInitialiser(dbInitialiser);
            AbstractDbUtils m = dbUtilsFactory.createDBUtils();
            DBOperation.setDbUtils(m);
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

