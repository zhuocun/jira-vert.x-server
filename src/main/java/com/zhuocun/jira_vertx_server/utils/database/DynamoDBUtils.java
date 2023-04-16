package com.zhuocun.jira_vertx_server.utils.database;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;

public class DynamoDBUtils {

    private DynamoDBUtils() {}

    private static DBInitialiser dbInitialiser = new DBInitialiser();
    private static DynamoDbClient dynamoDBClient = dbInitialiser.getDynamoDBClient();

    public static Future<Void> createItem(JsonObject item, String tableName) {
        Map<String, AttributeValue> itemValues = new HashMap<>();
        item.fieldNames().forEach(fieldName -> {
            Object fieldValue = item.getValue(fieldName);
            if (fieldValue instanceof String) {
                itemValues.put(fieldName, AttributeValue.builder().s((String) fieldValue).build());
            } else if (fieldValue instanceof Number) {
                itemValues.put(fieldName,
                        AttributeValue.builder().n(fieldValue.toString()).build());
            } else if (fieldValue instanceof Boolean) {
                itemValues.put(fieldName,
                        AttributeValue.builder().bool((Boolean) fieldValue).build());
            } else {
                itemValues.put(fieldName,
                        AttributeValue.builder().s(fieldValue.toString()).build());
            }
        });
        PutItemRequest request =
                PutItemRequest.builder().tableName(tableName).item(itemValues).build();
        try {
            dynamoDBClient.putItem(request);
            return Future.succeededFuture();
        } catch (DynamoDbException e) {
            return Future.failedFuture(e);
        }
    }

    public static Future<List<JsonObject>> find(JsonObject reqBody, String tableName) {
        // TODO: Implement the method to find items in DynamoDB
        return Future.succeededFuture();
    }

    public static Future<JsonObject> findById(String id, String tableName) {
        // TODO: Implement the method to find an item by id in DynamoDB
        return Future.succeededFuture();
    }

    public static Future<JsonObject> findByIdAndDelete(String id, String tableName) {
        // TODO: Implement the method to find and delete an item by id in DynamoDB
        return Future.succeededFuture();
    }

    public static Future<JsonObject> findByIdAndUpdate(String id, JsonObject updateFields,
            String tableName) {
        // TODO: Implement the method to find and update an item by id in DynamoDB
        return Future.succeededFuture();
    }

    public static Future<JsonObject> findOne(JsonObject reqBody, String tableName) {
        // TODO: Implement the method to find one item in DynamoDB
        return Future.succeededFuture();
    }

}
