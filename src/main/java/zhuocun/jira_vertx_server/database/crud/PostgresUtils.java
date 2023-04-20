package zhuocun.jira_vertx_server.database.crud;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.sqlclient.Pool;
import io.vertx.sqlclient.Tuple;
import zhuocun.jira_vertx_server.interfaces.IDBUtils;

public class PostgresUtils implements IDBUtils {

    private final Pool postgresPool;

    @Inject
    public PostgresUtils(Pool postgresPool) {
        this.postgresPool = postgresPool;
    }

    @Override
    public Future<Void> createItem(JsonObject item, String tableName) {
        Tuple params = Tuple.tuple();
        item.stream().forEach(entry -> params.addValue(entry.getValue()));
        String placeholders = IntStream.rangeClosed(1, item.size()).mapToObj(i -> "$" + i)
                .collect(Collectors.joining(","));
        String query = String.format("INSERT INTO %s (\"%s\") VALUES (%s)", tableName,
                String.join("\",\"", item.fieldNames()), placeholders);
        return postgresPool.preparedQuery(query).execute(params)
                .compose(res -> Future.succeededFuture());
    }

    @Override
    public Future<List<JsonObject>> find(JsonObject reqBody, String tableName) {
        Tuple params = Tuple.tuple();
        String query = String.format("SELECT * FROM %s", tableName);

        if (!reqBody.isEmpty()) {
            StringBuilder sb = new StringBuilder(" WHERE ");
            reqBody.stream().forEach(entry -> {
                sb.append(String.format("\"%s\" = $%d AND ", entry.getKey(), params.size() + 1));
                params.addValue(entry.getValue());
            });
            sb.delete(sb.length() - 5, sb.length());
            query += sb.toString();
        }

        return postgresPool.preparedQuery(query).execute(params).map(rows -> {
            List<JsonObject> resultList = new ArrayList<>();
            rows.forEach(row -> resultList.add(row.toJson()));
            return resultList;
        });
    }

    @Override
    public Future<JsonObject> findById(String id, String tableName) {
        String query = String.format("SELECT * FROM %s WHERE _id = $1", tableName);
        Tuple params = Tuple.of(id);
        return postgresPool.preparedQuery(query).execute(params)
                .map(rows -> rows.iterator().hasNext() ? rows.iterator().next().toJson() : null);
    }

    @Override
    public Future<JsonObject> findByIdAndDelete(String id, String tableName) {
        Tuple params = Tuple.of(id);
        String query = String.format("DELETE FROM %s WHERE _id = $1 RETURNING *", tableName);
        return postgresPool.preparedQuery(query).execute(params).compose(res -> {
            if (res.rowCount() == 0) {
                return Future.succeededFuture(null);
            }
            return Future.succeededFuture(res.iterator().next().toJson());
        });
    }

    @Override
    public Future<JsonObject> findByIdAndUpdate(String id, JsonObject updateFields,
            String tableName) {
        Tuple params = Tuple.tuple();
        updateFields.stream().forEach(entry -> params.addValue(entry.getValue()));
        params.addValue(id);
        String setValues = IntStream.rangeClosed(1, updateFields.size()).mapToObj(
                i -> "\"" + new ArrayList<>(updateFields.fieldNames()).get(i - 1) + "\" = $" + i)
                .collect(Collectors.joining(", "));
        String query = String.format("UPDATE %s SET %s WHERE _id = $%d RETURNING *", tableName,
                setValues, params.size());
        return postgresPool.preparedQuery(query).execute(params)
                .map(res -> res.iterator().next().toJson());
    }

    @Override
    public Future<JsonObject> findOne(JsonObject reqBody, String tableName) {
        String whereClauses = IntStream.rangeClosed(1, reqBody.size())
                .mapToObj(
                        i -> "\"" + new ArrayList<>(reqBody.fieldNames()).get(i - 1) + "\" = $" + i)
                .collect(Collectors.joining(" AND "));
        String query = String.format("SELECT * FROM %s WHERE %s LIMIT 1", tableName, whereClauses);

        Tuple params = Tuple.tuple();
        reqBody.stream().forEach(entry -> params.addValue(entry.getValue()));

        return postgresPool.preparedQuery(query).execute(params)
                .map(res -> res.iterator().hasNext() ? res.iterator().next().toJson() : null);
    }

}
