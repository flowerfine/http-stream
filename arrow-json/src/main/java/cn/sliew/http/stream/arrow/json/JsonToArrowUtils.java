package cn.sliew.http.stream.arrow.json;

import cn.sliew.http.stream.arrow.json.consumer.CompositeJsonConsumer;
import cn.sliew.http.stream.arrow.json.consumer.Consumer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.saasquatch.jsonschemainferrer.*;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.Schema;

import java.util.*;

/**
 * https://json-schema.org/implementations.html
 */
public enum JsonToArrowUtils {
    ;

    private static final ObjectMapper mapper = new ObjectMapper();
    private static final JsonSchemaInferrer inferrer = JsonSchemaInferrer.newBuilder()
            .setSpecVersion(SpecVersion.DRAFT_06)
            // Requires commons-validator
            .addFormatInferrers(FormatInferrers.email(), FormatInferrers.ip())
            .setAdditionalPropertiesPolicy(AdditionalPropertiesPolicies.notAllowed())
            .setRequiredPolicy(RequiredPolicies.nonNullCommonFields())
            .addEnumExtractors(EnumExtractors.validEnum(java.time.Month.class),
                    EnumExtractors.validEnum(java.time.DayOfWeek.class))
            .build();

    public static JsonNode inferJsonSchema(String json) throws JsonProcessingException {
        JsonNode data = mapper.readTree(json);
        return inferrer.inferForSample(data);
    }

    static CompositeJsonConsumer createCompositeConsumer(ObjectNode schema, JsonToArrowConfig config) {

        List<Consumer> consumers = new ArrayList<>();
        final Set<String> skipFieldNames = config.getSkipFieldNames();

        return null;
    }

    /**
     * https://github.com/saasquatch/json-schema-inferrer
     */
    public static Schema getSchema(JsonNode schema) {
        JsonNode properties = schema.get("properties");
        Iterator<Map.Entry<String, JsonNode>> iterator = properties.fields();
        List<Field> fields = new ArrayList<>(properties.size());
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> property = iterator.next();
            String name = property.getKey();
            JsonNode type = property.getValue();
            Field field = Field.notNullable(name, getFieldType(type));
            fields.add(field);
        }

        return new Schema(fields);
    }

    /**
     *
     * @param node
     * @return
     * @see Consts.Types
     */
    private static ArrowType getFieldType(JsonNode node) {
        String type = node.get("type").asText();
        switch (type) {
            case "string":
                break;
            case "boolean":
                break;
            case "integer":
                break;
            case "number":
                break;
            case "array":
                break;
            case "object":
                break;
            case "null":
                break;
        }
        return null;
    }
}
