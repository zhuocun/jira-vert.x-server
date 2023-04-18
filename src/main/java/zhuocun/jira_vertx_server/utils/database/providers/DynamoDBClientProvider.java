package zhuocun.jira_vertx_server.utils.database.providers;

import com.google.inject.Inject;
import com.google.inject.Provider;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import zhuocun.jira_vertx_server.utils.database.DBInitialiser;

public class DynamoDBClientProvider implements Provider<DynamoDbClient> {

    private final DBInitialiser dbInitialiser;

    @Inject
    public DynamoDBClientProvider(DBInitialiser dbInitialiser) {
        this.dbInitialiser = dbInitialiser;
    }

    @Override
    public DynamoDbClient get() {
        System.out.println(dbInitialiser.getDynamoDBClient());
        return dbInitialiser.getDynamoDBClient();
    }
}
