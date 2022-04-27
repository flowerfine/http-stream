package cn.sliew.http.stream.arrow.json;

import cn.sliew.http.stream.arrow.json.consumer.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.BitVector;
import org.apache.arrow.vector.FieldVector;
import org.apache.arrow.vector.Float8Vector;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.complex.ListVector;
import org.apache.arrow.vector.complex.StructVector;
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

    static CompositeJsonConsumer createCompositeConsumer(Schema schema, JsonToArrowConfig config) {
        List<Consumer> consumers = new ArrayList<>();
        for (Field field : schema.getFields()) {
            final Consumer consumer = createConsumer(field.getName(), field, config, null);
            consumers.add(consumer);
        }
        return new CompositeJsonConsumer(consumers);
    }

    private static Consumer createConsumer(String name, Field field, JsonToArrowConfig config, FieldVector consumerVector) {
        final FieldType fieldType = field.getFieldType();
        final FieldVector fieldVector = createVector(name, fieldType, config.getAllocator(), consumerVector);
        switch (field.getType().getTypeID()) {
            case Utf8:
                return new StringJsonConsumer((VarCharVector) fieldVector);
            case Bool:
                return new BooleanJsonConsumer((BitVector) fieldVector);
            case FloatingPoint:
                return new NumberJsonConsumer((Float8Vector) fieldVector);
            case Struct:
                return createStructConsumer(name, field, config, fieldVector);
            case List:
                return createArrayConsumer(name, field, config, fieldVector);
            default:
        }
        return null;
    }

    private static FieldVector createVector(String name, FieldType fieldType, BufferAllocator allocator, FieldVector consumerVector) {
        return consumerVector != null ? consumerVector : fieldType.createNewSingleVector(name, allocator, null);
    }

    private static Consumer createStructConsumer(String name, Field field, JsonToArrowConfig config, FieldVector consumerVector) {
        final List<Field> children = field.getChildren();
        Consumer[] delegates = new Consumer[children.size()];
        StructVector structVector = (StructVector) consumerVector;
        int vectorIndex = 0;
        for (int i = 0; i < children.size(); i++) {
            final Field childField = children.get(i);
            Consumer delegate;
            // use full name to distinguish fields have same names between parent and child fields.
            final String fullChildName = String.format("%s.%s", name, childField.getName());
            delegate = createConsumer(fullChildName, childField, config, structVector.getChildrenFromFields().get(vectorIndex++));
            delegates[i] = delegate;
        }

        return new ObjectJsonConsumer(structVector, delegates);
    }


    private static Consumer createArrayConsumer(String name, Field field, JsonToArrowConfig config, FieldVector consumerVector) {
        ListVector listVector = (ListVector) consumerVector;
        FieldVector dataVector = listVector.getDataVector();

        // create delegate
        final String fullChildName = String.format("%s.%s", name, dataVector.getName());
        Consumer delegate = createConsumer(fullChildName, dataVector.getField(), config, dataVector);
        return new ArrayJsonConsumer(listVector, delegate);
    }

}
