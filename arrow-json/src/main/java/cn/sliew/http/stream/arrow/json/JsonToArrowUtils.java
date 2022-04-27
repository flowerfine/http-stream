package cn.sliew.http.stream.arrow.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;
import org.apache.arrow.vector.types.pojo.Schema;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
                    fieldType = FieldType.notNullable(ArrowType.Utf8.INSTANCE);
                    fields.add(new Field(fieldName, fieldType, null));
                    break;
                case BOOLEAN:
                    fieldType = FieldType.notNullable(ArrowType.Bool.INSTANCE);
                    fields.add(new Field(fieldName, fieldType, null));
                    break;
                case NUMBER:
                    fieldType = FieldType.notNullable(new ArrowType.FloatingPoint(DOUBLE));
                    fields.add(new Field(fieldName, fieldType, null));
                    break;
                case NULL:
                    throw new IllegalStateException("can't infer fieldType from null for " + fieldName);
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
                    if (childFieldValue.isContainerNode()) {
                        Schema childArraySchema = inferSchema(childFieldValue);
                        fields.add(new Field(fieldName, fieldType, childArraySchema.getFields()));
                    } else {
                        fields.add(new Field(fieldName, fieldType, null));
                    }
                    break;
                case BINARY:
                case POJO:
                case MISSING:
                    throw new IllegalStateException("can't infer fieldType with Jackson-specific types: binary, missing and POJO for " + fieldName);
                default:
            }
        }
        return new Schema(fields);
    }


}
