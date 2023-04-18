package zhuocun.jira_vertx_server.verticles;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import zhuocun.jira_vertx_server.config.EnvConfig;
import zhuocun.jira_vertx_server.modules.DBModule;
import zhuocun.jira_vertx_server.modules.AppModule;
import zhuocun.jira_vertx_server.utils.database.DBInitialiser;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {
        Injector appInjector = Guice.createInjector(new AppModule());
        DBInitialiser dbInitialiser = appInjector.getInstance(DBInitialiser.class);
        EnvConfig envConfig = appInjector.getInstance(EnvConfig.class);

        dbInitialiser.initDB(vertx).onSuccess(v -> {
            Injector injector = Guice.createInjector(new DBModule(envConfig, dbInitialiser));
            ServerVerticle serverVerticle = injector.getInstance(ServerVerticle.class);

            // Deploy the DBVerticle
            vertx.deployVerticle(serverVerticle).onSuccess(res -> startPromise.complete())
                    .onFailure(startPromise::fail);
        }).onFailure(startPromise::fail);
    }
}
