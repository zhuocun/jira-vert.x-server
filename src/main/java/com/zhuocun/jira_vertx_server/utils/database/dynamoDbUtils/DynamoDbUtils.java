package com.zhuocun.jira_vertx_server.utils.database.dynamoDbUtils;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.zhuocun.jira_vertx_server.utils.database.DBInitialiser;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemRequest;
import software.amazon.awssdk.services.dynamodb.model.DeleteItemResponse;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.GetItemResponse;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ReturnValue;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemRequest;
import software.amazon.awssdk.services.dynamodb.model.UpdateItemResponse;
import com.fasterxml.uuid.Generators;

public class DynamoDbUtils {

    private DynamoDbUtils() {}

    private static DBInitialiser dbInitialiser = new DBInitialiser();
    private static DynamoDbClient dynamoDBClient = dbInitialiser.getDynamoDBClient();

    public static Future<Void> createItem(JsonObject reqBody, String tableName) {
        Promise<Void> resultPromise = Promise.promise();

        String id = Generators.randomBasedGenerator().generate().toString();
        String createdAt = ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT);

        reqBody.put("_id", id);
        reqBody.put("createdAt", createdAt);

        Map<String, AttributeValue> item =
                reqBody.getMap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey,
                        e -> AttributeValue.builder().s(e.getValue().toString()).build()));

        PutItemRequest putItemRequest =
                PutItemRequest.builder().tableName(tableName).item(item).build();

        try {
            dynamoDBClient.putItem(putItemRequest);
            resultPromise.complete();
        } catch (DynamoDbException e) {
            resultPromise.fail(e);
        }

        return resultPromise.future();
    }

    public static Future<List<JsonObject>> find(JsonObject reqBody, String tableName) {

        Promise<List<JsonObject>> resultPromise = Promise.promise();

        ScanRequest.Builder scanRequestBuilder = ScanRequest.builder().tableName(tableName);

        if (!reqBody.fieldNames().isEmpty()) {
            Expression expression = Expression.buildExpression(reqBody);
            scanRequestBuilder.expressionAttributeNames(expression.getExpressionAttributeNames())
                    .expressionAttributeValues(expression.getExpressionAttributeValues())
                    .filterExpression(expression.getFilterExpression());
        }

        ScanRequest scanRequest = scanRequestBuilder.build();

        try {
            ScanResponse response = dynamoDBClient.scan(scanRequest);
            List<JsonObject> items = response.items().stream().map(item -> {
                JsonObject jsonObject = new JsonObject();
                item.forEach((key, value) -> jsonObject.put(key, value.s()));
                return jsonObject;
            }).collect(Collectors.toList());

            resultPromise.complete(items);
        } catch (DynamoDbException e) {
            resultPromise.fail(e);
        }

        return resultPromise.future();
    }

    public static Future<JsonObject> findById(String id, String tableName) {
        Promise<JsonObject> resultPromise = Promise.promise();

        GetItemRequest getItemRequest = GetItemRequest.builder().tableName(tableName)
                .key(Collections.singletonMap("_id", AttributeValue.builder().s(id).build()))
                .build();

        try {
            GetItemResponse response = dynamoDBClient.getItem(getItemRequest);

            if (response.item() != null) {
                JsonObject foundItem = new JsonObject();
                response.item().forEach((key, value) -> foundItem.put(key, value.s()));
                resultPromise.complete(foundItem);
            } else {
                resultPromise.complete(null);
            }
        } catch (Exception e) {
            resultPromise.fail(e);
        }

        return resultPromise.future();
    }

    public static Future<JsonObject> findByIdAndDelete(String id, String tableName) {
        Promise<JsonObject> resultPromise = Promise.promise();

        DeleteItemRequest deleteItemRequest = DeleteItemRequest.builder().tableName(tableName)
                .key(Collections.singletonMap("_id", AttributeValue.builder().s(id).build()))
                .returnValues(ReturnValue.ALL_OLD).build();

        try {
            DeleteItemResponse response = dynamoDBClient.deleteItem(deleteItemRequest);

            if (response.attributes() != null) {
                JsonObject deletedItem = new JsonObject();
                response.attributes().forEach((key, value) -> deletedItem.put(key, value.s()));
                resultPromise.complete(deletedItem);
            } else {
                resultPromise.complete(null);
            }
        } catch (Exception e) {
            resultPromise.fail(e);
        }

        return resultPromise.future();
    }

    public static Future<JsonObject> findByIdAndUpdate(String id, JsonObject updateFields,
            String tableName) {
        Promise<JsonObject> resultPromise = Promise.promise();

        updateFields.remove("_id");

        Expression expression = Expression.buildExpression(updateFields);
        String updateExpression = expression.getFilterExpression().replace(" AND ", ", ");

        UpdateItemRequest updateItemRequest = UpdateItemRequest.builder().tableName(tableName)
                .key(Collections.singletonMap("_id", AttributeValue.builder().s(id).build()))
                .updateExpression("SET " + updateExpression)
                .expressionAttributeNames(expression.getExpressionAttributeNames())
                .expressionAttributeValues(expression.getExpressionAttributeValues())
                .returnValues("ALL_NEW").build();

        try {
            UpdateItemResponse response = dynamoDBClient.updateItem(updateItemRequest);

            if (response.attributes() != null) {
                JsonObject updatedItem = new JsonObject();
                response.attributes().forEach((key, value) -> updatedItem.put(key, value.s()));
                resultPromise.complete(updatedItem);
            } else {
                resultPromise.complete(null);
            }
        } catch (Exception e) {
            resultPromise.fail(e);
        }

        return resultPromise.future();
    }

    public static Future<JsonObject> findOne(JsonObject reqBody, String tableName) {

        Promise<JsonObject> resultPromise = Promise.promise();

        ScanRequest.Builder scanRequestBuilder = ScanRequest.builder().tableName(tableName);

        if (!reqBody.fieldNames().isEmpty()) {
            Expression expression = Expression.buildExpression(reqBody);
            scanRequestBuilder.expressionAttributeNames(expression.getExpressionAttributeNames())
                    .expressionAttributeValues(expression.getExpressionAttributeValues())
                    .filterExpression(expression.getFilterExpression());
        }

        ScanRequest scanRequest = scanRequestBuilder.build();

        try {
            ScanResponse response = dynamoDBClient.scan(scanRequest);
            JsonObject item = new JsonObject();
            response.items().get(0).forEach((key, value) -> item.put(key, value.s()));
            resultPromise.complete(item);
        } catch (DynamoDbException e) {
            resultPromise.fail(e);
        }

        return resultPromise.future();
    }

}
