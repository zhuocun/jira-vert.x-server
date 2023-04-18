package zhuocun.jira_vertx_server.verticles;

import com.google.inject.Inject;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import zhuocun.jira_vertx_server.utils.database.DBInitialiser;

public class DBVerticle extends AbstractVerticle {

    private final DBInitialiser dbInitialiser;

    @Inject
    public DBVerticle(DBInitialiser dbInitialiser) {
        this.dbInitialiser = dbInitialiser;
    }

    @Override
    public void start(Promise<Void> startPromise) {
        dbInitialiser.initDB(vertx).onSuccess(v -> startPromise.complete())
                .onFailure(startPromise::fail);
    }
}
