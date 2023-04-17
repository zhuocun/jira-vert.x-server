package zhuocun.jira_vertx_server.verticles;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import zhuocun.jira_vertx_server.modules.AppModule;

public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) {

        Injector injector = Guice.createInjector(new AppModule());

        DBVerticle dbVerticle = injector.getInstance(DBVerticle.class);

        ServerVerticle serverVerticle = injector.getInstance(ServerVerticle.class);

        // Deploy the DBVerticle
        vertx.deployVerticle(dbVerticle, dbDeployment -> {
            if (dbDeployment.failed()) {
                startPromise.fail(dbDeployment.cause());
                return;
            }

            // Deploy the ServerVerticle
            vertx.deployVerticle(serverVerticle, routerDeployment -> {
                if (routerDeployment.failed()) {
                    startPromise.fail(routerDeployment.cause());
                    return;
                }

                startPromise.complete();
            });
        });
    }
}
