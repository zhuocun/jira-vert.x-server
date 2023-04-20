package zhuocun.jira_vertx_server.database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import io.vertx.core.json.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

@AllArgsConstructor
@Getter
public class Expression {
    private final Map<String, String> expressionAttributeNames;
    private final Map<String, AttributeValue> expressionAttributeValues;
    private final String filterExpression;

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
}
