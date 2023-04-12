package com.zhuocun.jira_vertx_server.dao;

import com.zhuocun.jira_vertx_server.model.User;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

public class UserDao {
    private PgPool pgPool;

    public UserDao(PgPool pgPool) {
        this.pgPool = pgPool;
    }

    // Update CRUD methods to use the PostgreSQL client
    public void createUser(User user, Handler<AsyncResult<Integer>> resultHandler) {
        String query = "INSERT INTO users (name, email) VALUES ($1, $2) RETURNING id";
        Tuple params = Tuple.of(user.getName(), user.getEmail());
        pgPool.preparedQuery(query)
                .execute(params, ar -> {
                    if (ar.succeeded()) {
                        RowSet<Row> rows = ar.result();
                        if (rows.size() == 1) {
                            resultHandler.handle(
                                    io.vertx.core.Future.succeededFuture(rows.iterator().next().getInteger("id")));
                        } else {
                            resultHandler.handle(io.vertx.core.Future.failedFuture("Failed to create user"));
                        }
                    } else {
                        resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
                    }
                });
    }

    public void getUserById(int id, Handler<AsyncResult<User>> resultHandler) {
        String query = "SELECT id, name, email FROM users WHERE id = $1";
        pgPool.preparedQuery(query)
                .execute(Tuple.of(id), ar -> {
                    if (ar.succeeded()) {
                        RowSet<Row> rows = ar.result();
                        if (rows.size() == 1) {
                            Row row = rows.iterator().next();
                            User user = new User(row.getInteger("id"), row.getString("name"), row.getString("email"));
                            resultHandler.handle(io.vertx.core.Future.succeededFuture(user));
                        } else {
                            resultHandler.handle(io.vertx.core.Future.succeededFuture(null));
                        }
                    } else {
                        resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
                    }
                });
    }

    public void updateUser(int id, User updatedUser, Handler<AsyncResult<Void>> resultHandler) {
        String query = "UPDATE users SET name = $1, email = $2 WHERE id = $3";
        Tuple params = Tuple.of(updatedUser.getName(), updatedUser.getEmail(), id);
        pgPool.preparedQuery(query)
                .execute(params, ar -> {
                    if (ar.succeeded()) {
                        resultHandler.handle(io.vertx.core.Future.succeededFuture());
                    } else {
                        resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
                    }
                });
    }

    public void deleteUser(int id, Handler<AsyncResult<Void>> resultHandler) {
        String query = "DELETE FROM users WHERE id = $1";
        pgPool.preparedQuery(query)
                .execute(Tuple.of(id), ar -> {
                    if (ar.succeeded()) {
                        resultHandler.handle(io.vertx.core.Future.succeededFuture());
                    } else {
                        resultHandler.handle(io.vertx.core.Future.failedFuture(ar.cause()));
                    }
                });
    }
}
