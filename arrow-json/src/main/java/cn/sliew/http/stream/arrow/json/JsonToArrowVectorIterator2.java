package cn.sliew.http.stream.arrow.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.util.Preconditions;
import org.apache.arrow.vector.*;
import org.apache.arrow.vector.complex.StructVector;
import org.apache.arrow.vector.types.FloatingPointPrecision;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;
import org.apache.arrow.vector.util.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JsonToArrowVectorIterator2 implements Iterator<VectorSchemaRoot>, AutoCloseable {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final JsonParser parser;
    private final JsonToArrowConfig config;

    public JsonToArrowVectorIterator2(JsonParser parser, JsonToArrowConfig config) {
        this.parser = parser;
        this.config = config;
    }

    @Override
    public boolean hasNext() {
        return parser.isClosed() == false;
    }

    @Override
    public VectorSchemaRoot next() {
        Preconditions.checkArgument(hasNext());
        try {
            final JsonToken jsonToken = parser.nextToken();
            Preconditions.checkState(jsonToken == JsonToken.START_OBJECT || jsonToken == JsonToken.START_ARRAY);
            if (jsonToken == JsonToken.START_OBJECT) {
                return loadObject();
            }
            if (jsonToken == JsonToken.START_ARRAY) {
                return loadArray();
            }
            switch (jsonToken) {
                case FIELD_NAME:
                case START_OBJECT:
                case END_OBJECT:
                case START_ARRAY:
                case END_ARRAY:
                case VALUE_STRING:
                case VALUE_FALSE:
                case VALUE_TRUE:
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT:
                case VALUE_NULL:
                case VALUE_EMBEDDED_OBJECT:
                case NOT_AVAILABLE:
                default:

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void readObject(String fieldName, FieldVector consumerVector) throws IOException {

    }

    private VectorSchemaRoot loadObject() throws IOException {
        JsonToken jsonToken = parser.nextToken();
        List<Field> fields = new ArrayList<>();
        List<FieldVector> valueVectors = new ArrayList<>();
        String fieldName = "";
        int i = 0;
        while (jsonToken != JsonToken.END_OBJECT) {
            FieldType fieldType = null;
            FieldVector vector = null;
            switch (jsonToken) {
                case FIELD_NAME:
                    fieldName = parser.getCurrentName();
                    break;
                case START_OBJECT:
                    // 读取 child object
                    fieldType = FieldType.notNullable(ArrowType.Struct.INSTANCE);
                    vector = new StructVector(fieldName, config.getAllocator(), fieldType, () -> {});
                    vector.allocateNew();
                    consumerObject(fieldName, (StructVector) vector);
                    fields.add(new Field(fieldName, fieldType, null));
                    valueVectors.add(vector);
                case END_OBJECT:
                case START_ARRAY:
                    // 读取 child arrays
                case END_ARRAY:
                case VALUE_STRING:
                    fieldType = FieldType.notNullable(ArrowType.Utf8.INSTANCE);
                    vector = new VarCharVector(fieldName, fieldType, config.getAllocator());
                    vector.allocateNew();
                    consumeJsonToken(fieldName, jsonToken, i++, vector);
                    fields.add(new Field(fieldName, fieldType, null));
                    valueVectors.add(vector);
                    break;
                case VALUE_FALSE:
                case VALUE_TRUE:
                    fieldType = FieldType.notNullable(ArrowType.Bool.INSTANCE);
                    vector = new BitVector(fieldName, fieldType, config.getAllocator());
                    vector.allocateNew();
                    consumeJsonToken(fieldName, jsonToken, i++, vector);
                    fields.add(new Field(fieldName, fieldType, null));
                    valueVectors.add(vector);
                    break;
                case VALUE_NUMBER_INT:
                    fieldType = FieldType.notNullable(new ArrowType.Int(32, true));
                    vector = new IntVector(fieldName, fieldType, config.getAllocator());
                    vector.allocateNew();
                    consumeJsonToken(fieldName, jsonToken, i++, vector);
                    fields.add(new Field(fieldName, fieldType, null));
                    valueVectors.add(vector);
                    break;
                case VALUE_NUMBER_FLOAT:
                    fieldType = FieldType.notNullable(new ArrowType.FloatingPoint(FloatingPointPrecision.SINGLE));
                    vector = new Float4Vector(fieldName, fieldType, config.getAllocator());
                    vector.allocateNew();
                    consumeJsonToken(fieldName, jsonToken, i++, vector);
                    fields.add(new Field(fieldName, fieldType, null));
                    valueVectors.add(vector);
                    break;
                case VALUE_NULL:
                    fieldType = FieldType.notNullable(ArrowType.Null.INSTANCE);
                    vector = new NullVector(fieldName, fieldType);
                    i++;
                    fields.add(new Field(fieldName, fieldType, null));
                    valueVectors.add(vector);
                case VALUE_EMBEDDED_OBJECT:
                case NOT_AVAILABLE:
                default:
                    jsonToken = parser.nextToken();
            }
        }
        return new VectorSchemaRoot(fields, valueVectors, 1);
    }

    private void consumeJsonToken(String fieldName,
                                  JsonToken valueToken,
                                  int vectorIndex,
                                  FieldVector consumerVector) throws IOException {
        switch (valueToken) {
            case FIELD_NAME:
            case START_OBJECT:
            case END_OBJECT:
            case START_ARRAY:
            case END_ARRAY:
            case NOT_AVAILABLE:
                throw new IllegalStateException("must be value token");
            case VALUE_STRING:
                ((VarCharVector) consumerVector).set(vectorIndex, new Text(parser.getText()));
                break;
            case VALUE_FALSE:
            case VALUE_TRUE:
                ((BitVector) consumerVector).set(vectorIndex, parser.getBooleanValue() ? 1 : 0);
                break;
            case VALUE_NUMBER_INT:
                ((IntVector) consumerVector).set(vectorIndex, parser.getIntValue());
                break;
            case VALUE_NUMBER_FLOAT:
                ((Float4Vector) consumerVector).set(vectorIndex, parser.getFloatValue());
                break;
            case VALUE_NULL:
                break;
            case VALUE_EMBEDDED_OBJECT:
                break;
            default:
        }
    }

    private void consumerObject(String name, StructVector vector) throws IOException {
        JsonToken jsonToken = parser.nextToken();
        int vectorIndex = 0;
        String fieldName = "";
        while (jsonToken != JsonToken.END_OBJECT) {
            FieldType fieldType = null;
            FieldVector vector = null;
            switch (jsonToken) {
                case FIELD_NAME:
                    fieldName = parser.getCurrentName();
                    break;
                case START_OBJECT:
                case END_OBJECT:
                case START_ARRAY:
                case END_ARRAY:
                case VALUE_STRING:
                case VALUE_FALSE:
                case VALUE_TRUE:
                case VALUE_NUMBER_INT:
                case VALUE_NUMBER_FLOAT:
                    FieldVector valueVectors = vector.getChildrenFromFields().get(vectorIndex++);
                    consumeJsonToken(fieldName, jsonToken, vectorIndex, valueVectors);
                    break;
                case VALUE_NULL:
                    fieldType = FieldType.notNullable(ArrowType.Null.INSTANCE);
                    vector = new NullVector(fieldName, fieldType);
                    i++;
                    fields.add(new Field(fieldName, fieldType, null));
                    valueVectors.add(vector);
                case VALUE_EMBEDDED_OBJECT:
                case NOT_AVAILABLE:
                default:
                    jsonToken = parser.nextToken();
            }
        }

        for (Map.Entry<String, Object> entry : map.entrySet()) {
            final String childName = entry.getKey();
            final Object childValue = entry.getValue();

            final String fullChildName = String.format("%s.%s", name, entry.getKey());
            final FieldVector valueVectors = vector.getChildrenFromFields().get(vectorIndex++);
        }
    }

    private VectorSchemaRoot loadArray() throws IOException {
        JsonToken jsonToken = parser.nextToken();
        while (jsonToken != JsonToken.END_ARRAY) {

        }
        return null;
    }

    private static FieldVector createVector(String name,
                                            FieldType fieldType,
                                            FieldVector consumerVector,
                                            BufferAllocator allocator) {
        return consumerVector != null ? consumerVector : fieldType.createNewSingleVector(name, allocator, null);
    }


    @Override
    public void close() throws Exception {
        parser.close();
    }
}
