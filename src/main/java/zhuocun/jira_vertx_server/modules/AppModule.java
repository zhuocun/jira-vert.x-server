package zhuocun.jira_vertx_server.modules;

import com.google.inject.AbstractModule;
import zhuocun.jira_vertx_server.config.EnvConfig;
import zhuocun.jira_vertx_server.utils.database.DBInitialiser;

public class AppModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(DBInitialiser.class).asEagerSingleton();
        bind(EnvConfig.class).asEagerSingleton();
    }

}
