package com.zhuocun.jira_vertx_server.verticles;

import com.zhuocun.jira_vertx_server.utils.database.DBInitialiser;
import com.zhuocun.jira_vertx_server.utils.database.DBUtils;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.sqlclient.Pool;
import com.zhuocun.jira_vertx_server.constants.DatabaseType;

public class DBVerticle extends AbstractVerticle {

    private Pool dbPool;

    @Override
    public void start(Promise<Void> startFuture) throws Exception {
        DBInitialiser dbInitialiser = new DBInitialiser();

        dbInitialiser.initDB(vertx).onSuccess(result -> {
            switch (DBUtils.getDBType()) {
                case DatabaseType.POSTGRESQL:
                    dbPool = dbInitialiser.getPostgresPool();
                    break;
                case DatabaseType.MONGO_DB:
                    break;
                default:
                    break;
            }
            startFuture.complete();
        }).onFailure(startFuture::fail);
    }

    @Override
    public void stop() throws Exception {
        if (dbPool != null) {
            dbPool.close();
        }
    }
}

