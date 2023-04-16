package com.zhuocun.jira_vertx_server.utils.database.dynamoDbUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.vertx.core.json.JsonObject;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

public class Expression {
    private final Map<String, String> expressionAttributeNames;
    private final Map<String, AttributeValue> expressionAttributeValues;
    private final String filterExpression;

    public Expression(Map<String, String> expressionAttributeNames,
            Map<String, AttributeValue> expressionAttributeValues, String filterExpression) {
        this.expressionAttributeNames = expressionAttributeNames;
        this.expressionAttributeValues = expressionAttributeValues;
        this.filterExpression = filterExpression;
    }

    public static Expression buildExpression(JsonObject attributeFields) {
        Map<String, String> expressionAttributeNames = new HashMap<>();
        Map<String, AttributeValue> expressionAttributeValues = new HashMap<>();
        List<String> expressions = new ArrayList<>();

        int index = 0;
        for (String attributeName : attributeFields.fieldNames()) {
            String nameKey = "#attrName" + index;
            String valueKey = ":attrValue" + index;

            expressionAttributeNames.put(nameKey, attributeName);
            expressionAttributeValues.put(valueKey,
                    AttributeValue.builder().s(attributeFields.getString(attributeName)).build());
            expressions.add(nameKey + " = " + valueKey);

            index++;
        }

        return new Expression(expressionAttributeNames, expressionAttributeValues,
                String.join(" AND ", expressions));
    }

    public Map<String, String> getExpressionAttributeNames() {
        return expressionAttributeNames;
    }

    public Map<String, AttributeValue> getExpressionAttributeValues() {
        return expressionAttributeValues;
    }

    public String getFilterExpression() {
        return filterExpression;
    }
}
