package cn.sliew.http.stream.arrow.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;
import org.apache.arrow.vector.types.pojo.Schema;

import java.io.IOException;
import java.util.*;

import static org.apache.arrow.vector.types.FloatingPointPrecision.DOUBLE;

/**
 * https://json-schema.org/implementations.html
 */
public enum JsonToArrowUtils {
    ;

    private static final ObjectMapper mapper = new ObjectMapper();

    public static Schema inferSchema(String originalJsonData) throws IOException {
        final JsonNode jsonNode = mapper.readTree(originalJsonData);
        return inferSchema(jsonNode);
    }

    public static Schema inferSchema(JsonNode jsonNode) {
        final Iterator<Map.Entry<String, JsonNode>> iterator = jsonNode.fields();
        List<Field> fields = new ArrayList<>();
        while (iterator.hasNext()) {
            Map.Entry<String, JsonNode> entry = iterator.next();
            final String fieldName = entry.getKey();
            final JsonNode fieldValue = entry.getValue();
            FieldType fieldType = null;
            final JsonNodeType nodeType = fieldValue.getNodeType();
            switch (nodeType) {
                case STRING:
                case BOOLEAN:
                case NUMBER:
                case NULL:
                case BINARY:
                    fields.add(inferValueField(fieldName, fieldValue));
                    break;
                case OBJECT:
                    fieldType = FieldType.notNullable(ArrowType.Struct.INSTANCE);
                    final Schema childObjectSchema = inferSchema(fieldValue);
                    fields.add(new Field(fieldName, fieldType, childObjectSchema.getFields()));
                    break;
                case ARRAY:
                    fieldType = FieldType.notNullable(ArrowType.List.INSTANCE);
                    if (fieldValue.size() == 0) {
                        throw new IllegalStateException("can't infer fieldType from empty array for " + fieldName);
                    }
                    final JsonNode childFieldValue = fieldValue.get(0);
                    if (childFieldValue.isObject()) {
                        FieldType objectFieldType = FieldType.notNullable(ArrowType.Struct.INSTANCE);
                        Schema childArraySchema = inferSchema(childFieldValue);
                        Field objectField = new Field(null, objectFieldType, childArraySchema.getFields());
                        fields.add(new Field(fieldName, fieldType, Collections.singletonList(objectField)));
                    } else {
                        Field field = inferValueField(null, childFieldValue);
                        fields.add(new Field(fieldName, fieldType, Collections.singletonList(field)));
                    }
                    break;
                case POJO:
                case MISSING:
                    throw new IllegalStateException("can't infer fieldType with Jackson-specific types: binary, missing and POJO for " + fieldName);
                default:
            }
        }
        return new Schema(fields);
    }

    private static Field inferValueField(String fieldName, JsonNode fieldValue) {
        final JsonNodeType nodeType = fieldValue.getNodeType();
        FieldType fieldType = null;
        switch (nodeType) {
            case STRING:
                fieldType = FieldType.notNullable(ArrowType.Utf8.INSTANCE);
                return new Field(fieldName, fieldType, null);
            case BOOLEAN:
                fieldType = FieldType.notNullable(ArrowType.Bool.INSTANCE);
                return new Field(fieldName, fieldType, null);
            case NUMBER:
                fieldType = FieldType.notNullable(new ArrowType.FloatingPoint(DOUBLE));
                return new Field(fieldName, fieldType, null);
            case BINARY:
                fieldType = FieldType.notNullable(ArrowType.Binary.INSTANCE);
                return new Field(fieldName, fieldType, null);
            case NULL:
                throw new IllegalStateException("can't infer fieldType from null for " + fieldName);
            case ARRAY:
            case OBJECT:
            case POJO:
            case MISSING:
                throw new IllegalStateException("can't infer fieldType with Jackson-specific types: ARRAY, OBJECT, POJO, MISSING for " + fieldName);
            default:
                throw new IllegalStateException("unknown json node type for " + fieldName + " with " + nodeType);
        }
    }
}
