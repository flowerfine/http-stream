package cn.sliew.http.stream.arrow.json;

import cn.sliew.http.stream.arrow.json.consumer.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.*;
import org.apache.arrow.vector.complex.ListVector;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;
import org.apache.arrow.vector.types.pojo.Schema;

import java.util.*;

import static org.apache.arrow.vector.types.FloatingPointPrecision.DOUBLE;

/**
 * https://json-schema.org/implementations.html
 */
public enum JsonToArrowUtils {
    ;

    private static final ObjectMapper mapper = new ObjectMapper();
//    private static final JsonSchemaInferrer inferrer = JsonSchemaInferrer.newBuilder()
//            .setSpecVersion(SpecVersion.DRAFT_06)
//            // Requires commons-validator
//            .addFormatInferrers(FormatInferrers.email(), FormatInferrers.ip())
//            .setAdditionalPropertiesPolicy(AdditionalPropertiesPolicies.notAllowed())
//            .setRequiredPolicy(RequiredPolicies.nonNullCommonFields())
//            .addEnumExtractors(EnumExtractors.validEnum(java.time.Month.class),
//                    EnumExtractors.validEnum(java.time.DayOfWeek.class))
//            .build();

    private static final SkipConsumer skipConsumer = new SkipConsumer(parser -> parser.nextToken());

    /**
     * https://github.com/saasquatch/json-schema-inferrer
     */
//    public static JsonNode inferJsonSchema(String json) throws JsonProcessingException {
//        JsonNode data = mapper.readTree(json);
//        return inferrer.inferForSample(data);
//    }

    static CompositeJsonConsumer createCompositeConsumer(ObjectNode schema, JsonToArrowConfig config) {
        List<Consumer> consumers = new ArrayList<>();
        final Set<String> skipFieldNames = config.getSkipFieldNames();
        JsonNode properties = schema.get("properties");
        Iterator<Map.Entry<String, JsonNode>> iterator = properties.fields();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> property = iterator.next();
            String name = property.getKey();
            JsonNode type = property.getValue();
            if (skipFieldNames.contains(name)) {
                consumers.add(skipConsumer);
            } else {
                Consumer consumer = createConsumer(name, type, config);
                consumers.add(consumer);
            }
        }
        return new CompositeJsonConsumer(consumers);
    }

    private static Consumer createConsumer(String name, JsonNode node, JsonToArrowConfig config) {
        return createConsumer(name, node, config, null);
    }

    /**
     * @see Consts.Types
     */
    private static Consumer createConsumer(String name,
                                           JsonNode node,
                                           JsonToArrowConfig config,
                                           FieldVector consumerVector) {
        final BufferAllocator allocator = config.getAllocator();

        final ArrowType arrowType;
        final FieldType fieldType;
        final FieldVector vector;
        final Consumer consumer;

        String type = node.get("type").asText();
        switch (type) {
            case "string":
                arrowType = new ArrowType.Utf8();
                fieldType = FieldType.nullable(arrowType);
                vector = createVector(consumerVector, fieldType, name, allocator);
                consumer = new StringJsonConsumer((VarCharVector) vector);
                break;
            case "boolean":
                arrowType = new ArrowType.Bool();
                fieldType = FieldType.nullable(arrowType);
                vector = createVector(consumerVector, fieldType, name, allocator);
                consumer = new BooleanJsonConsumer((BitVector) vector);
                break;
            case "integer":
                arrowType = new ArrowType.Int(32, /*signed=*/true);
                fieldType = FieldType.nullable(arrowType);
                vector = createVector(consumerVector, fieldType, name, allocator);
                consumer = new IntegerJsonConsumer((IntVector) vector);
                break;
            case "number":
                arrowType = new ArrowType.FloatingPoint(DOUBLE);
                fieldType = FieldType.nullable(arrowType);
                vector = createVector(consumerVector, fieldType, name, allocator);
                consumer = new NumberJsonConsumer((Float8Vector) vector);
            case "array":

                break;
            case "object":
                break;
            case "null":
                arrowType = new ArrowType.Null();
                fieldType = FieldType.nullable(arrowType);
                vector = fieldType.createNewSingleVector(name, allocator, /*schemaCallback=*/null);
                consumer = new NullJsonConsumer((NullVector) vector);
        }

        return null;
    }

    private static FieldVector createVector(FieldVector consumerVector,
                                            FieldType fieldType,
                                            String name,
                                            BufferAllocator allocator) {
        return consumerVector != null ? consumerVector : fieldType.createNewSingleVector(name, allocator, null);
    }

    private static Consumer createArrayConsumer(String name,
                                                JsonNode node,
                                                JsonToArrowConfig config,
                                                FieldVector consumerVector) {

        ListVector listVector;
        if (consumerVector == null) {
            FieldType fieldType = FieldType.nullable(getFieldType(node));
            final Field field = new Field(name, fieldType, null);
            listVector = (ListVector) field.createVector(config.getAllocator());
        } else {
            listVector = (ListVector) consumerVector;
        }

        FieldVector dataVector = listVector.getDataVector();

//        // create delegate
//        Schema childSchema = schema.getElementType();
//        Consumer delegate = createConsumer(childSchema, childSchema.getName(), config, dataVector);
//
//        return new AvroArraysConsumer(listVector, delegate);
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
     * @param node
     * @return
     * @see Consts.Types
     */
    private static ArrowType getFieldType(JsonNode node) {
        String type = node.get("type").asText();
        switch (type) {
            case "string":
                return ArrowType.Utf8.INSTANCE;
            case "boolean":
                return ArrowType.Bool.INSTANCE;
            case "integer":
                return new ArrowType.Int(32, true);
            case "number":
                return new ArrowType.FloatingPoint(DOUBLE);
            case "array":
                break;
            case "object":
                break;
            case "null":
                return ArrowType.Null.INSTANCE;
        }
        return null;
    }

}
