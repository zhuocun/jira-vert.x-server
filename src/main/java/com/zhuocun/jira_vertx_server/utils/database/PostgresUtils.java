package com.zhuocun.jira_vertx_server.utils.database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Tuple;

public class PostgresUtils {

    static PgPool postgresPool = DBInitialiser.getPostgresPool();

    public static Future<Void> createItem(JsonObject item, String tableName) {
        Tuple params = Tuple.tuple();
        item.stream().forEach(entry -> params.addValue(entry.getValue()));
        String query = String.format("INSERT INTO %s (%s) VALUES (%s)", tableName,
                String.join(",", item.fieldNames()),
                String.join(",", Collections.nCopies(item.size(), "?")));
        return postgresPool.preparedQuery(query).execute(params)
                .compose(res -> Future.succeededFuture());
    }

    public static Future<List<JsonObject>> find(JsonObject reqBody, String tableName) {
        Tuple params = Tuple.tuple();
        String query = String.format("SELECT * FROM %s", tableName);

        if (!reqBody.isEmpty()) {
            StringBuilder sb = new StringBuilder(" WHERE ");
            reqBody.stream().forEach(entry -> {
                sb.append(String.format("%s = ? AND ", entry.getKey()));
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

    public static Future<JsonObject> findById(String id, String tableName) {
        String query = String.format("SELECT * FROM %s WHERE _id = $1", tableName);
        Tuple params = Tuple.of(id);
        return postgresPool.preparedQuery(query).execute(params)
                .map(rows -> rows.iterator().hasNext() ? rows.iterator().next().toJson() : null);
    }

    public static Future<JsonObject> findByIdAndDelete(String id, String tableName) {
        Tuple params = Tuple.of(id);
        String query = String.format("DELETE FROM %s WHERE _id = $1 RETURNING *", tableName);
        return postgresPool.preparedQuery(query).execute(params).compose(res -> {
            if (res.rowCount() == 0) {
                return Future.succeededFuture(null);
            }
            return Future.succeededFuture(res.iterator().next().toJson());
        });
    }

    public static Future<JsonObject> findByIdAndUpdate(String id, JsonObject updateFields,
            String tableName) {
        Tuple params = Tuple.tuple();
        updateFields.stream().forEach(entry -> params.addValue(entry.getValue()));
        params.addValue(id);
        String setValues = updateFields.fieldNames().stream().map(key -> "\"" + key + "\" = ?")
                .collect(Collectors.joining(", "));
        String query =
                String.format("UPDATE %s SET %s WHERE _id = ? RETURNING *", tableName, setValues);
        return postgresPool.preparedQuery(query).execute(params)
                .map(res -> res.iterator().next().toJson());
    }

    public static Future<JsonObject> findOne(JsonObject reqBody, String tableName) {
        String whereClauses = reqBody.fieldNames().stream().map(key -> String.format("%s = ?", key))
                .collect(Collectors.joining(" AND "));
        String query = String.format("SELECT * FROM %s WHERE %s LIMIT 1", tableName, whereClauses);

        Tuple params = Tuple.tuple();
        reqBody.stream().forEach(entry -> params.addValue(entry.getValue()));

        return postgresPool.preparedQuery(query).execute(params)
                .map(res -> res.iterator().hasNext() ? res.iterator().next().toJson() : null);
    }

}
