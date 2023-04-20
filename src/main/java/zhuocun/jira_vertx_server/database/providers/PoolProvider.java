package zhuocun.jira_vertx_server.database.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import io.vertx.sqlclient.Pool;
import zhuocun.jira_vertx_server.database.DBInitialiser;

public class PoolProvider implements Provider<Pool> {

    private final DBInitialiser dbInitialiser;

    @Inject
    public PoolProvider(DBInitialiser dbInitialiser) {
        this.dbInitialiser = dbInitialiser;
    }

    @Override
    public Pool get() {
        return dbInitialiser.getDbPool();
    }
}

