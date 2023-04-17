package zhuocun.jira_vertx_server.modules;

import com.google.inject.AbstractModule;
import zhuocun.jira_vertx_server.utils.database.DBInitialiser;
import zhuocun.jira_vertx_server.verticles.ServerVerticle;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DBInitialiser.class).asEagerSingleton();
        bind(ServerVerticle.class).toInstance(new ServerVerticle());
    }

}
