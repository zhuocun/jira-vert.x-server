package zhuocun.jira_vertx_server.modules;

import com.google.inject.AbstractModule;
import zhuocun.jira_vertx_server.verticles.DBVerticle;
import zhuocun.jira_vertx_server.verticles.ServerVerticle;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DBVerticle.class).toInstance(new DBVerticle());
        bind(ServerVerticle.class).toInstance(new ServerVerticle());
    }

}
