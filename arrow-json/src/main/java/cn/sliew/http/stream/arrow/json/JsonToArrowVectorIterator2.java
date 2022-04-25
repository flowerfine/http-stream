package cn.sliew.http.stream.arrow.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.TreeNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.arrow.util.Preconditions;
import org.apache.arrow.vector.*;
import org.apache.arrow.vector.complex.MapVector;
import org.apache.arrow.vector.complex.impl.UnionMapWriter;
import org.apache.arrow.vector.types.FloatingPointPrecision;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;
import org.apache.arrow.vector.util.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
        return parser.isClosed();
    }

    @Override
    public VectorSchemaRoot next() {
        Preconditions.checkArgument(hasNext());
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
        return null;
    }

    private VectorSchemaRoot loadObject() throws IOException {
        JsonToken jsonToken = parser.nextToken();
        List<Field> fields = new ArrayList<>();
        List<FieldVector> valueVectors = new ArrayList<>();
        String fieldName = "";
        int i = 0;
        while (jsonToken != JsonToken.END_OBJECT) {
            if (jsonToken == JsonToken.FIELD_NAME) {
                fieldName = jsonToken.name();
            } else if (jsonToken == JsonToken.VALUE_STRING) {
                FieldType fieldType = FieldType.notNullable(ArrowType.Utf8.INSTANCE);
                VarCharVector vector = new VarCharVector(fieldName, fieldType, config.getAllocator());
                vector.set(i++, new Text(parser.getText()));
                fields.add(new Field(fieldName, fieldType, null));
                valueVectors.add(vector);
            } else if (jsonToken == JsonToken.VALUE_FALSE) {
                FieldType fieldType = FieldType.notNullable(ArrowType.Bool.INSTANCE);
                BitVector vector = new BitVector(fieldName, fieldType, config.getAllocator());
                vector.set(i++, 0);
                fields.add(new Field(fieldName, fieldType, null));
                valueVectors.add(vector);
            } else if (jsonToken == JsonToken.VALUE_TRUE) {
                FieldType fieldType = FieldType.notNullable(ArrowType.Bool.INSTANCE);
                BitVector vector = new BitVector(fieldName, fieldType, config.getAllocator());
                vector.set(i++, 1);
                fields.add(new Field(fieldName, fieldType, null));
                valueVectors.add(vector);
            } else if (jsonToken == JsonToken.VALUE_NUMBER_INT) {
                FieldType fieldType = FieldType.notNullable(new ArrowType.Int(32, true));
                IntVector vector = new IntVector(fieldName, fieldType, config.getAllocator());
                vector.set(i++, parser.getIntValue());
                fields.add(new Field(fieldName, fieldType, null));
                valueVectors.add(vector);
            } else if (jsonToken == JsonToken.VALUE_NUMBER_FLOAT) {
                FieldType fieldType = FieldType.notNullable(new ArrowType.FloatingPoint(FloatingPointPrecision.SINGLE));
                Float4Vector vector = new Float4Vector(fieldName, fieldType, config.getAllocator());
                vector.set(i++, parser.getFloatValue());
                fields.add(new Field(fieldName, fieldType, null));
                valueVectors.add(vector);
            } else if (jsonToken == JsonToken.VALUE_NULL) {
                FieldType fieldType = FieldType.notNullable(ArrowType.Null.INSTANCE);
                NullVector vector = new NullVector(fieldName, fieldType);
                i++;
                fields.add(new Field(fieldName, fieldType, null));
                valueVectors.add(vector);
            } else if (jsonToken == JsonToken.VALUE_EMBEDDED_OBJECT) {
                FieldType fieldType = FieldType.notNullable(new ArrowType.Map(true));
                String valueAsString = parser.getValueAsString();
                MapVector vector = new MapVector(fieldName, config.getAllocator(), fieldType, () -> {});
                final UnionMapWriter writer = vector.getWriter();
                final TreeNode treeNode = objectMapper.readTree(parser);
                writer.startMap();
                writer.startEntry();

                writer.endEntry();
                writer.endMap();
                fields.add(new Field(fieldName, fieldType, null));
                valueVectors.add(vector);

            } else if (jsonToken == JsonToken.NOT_AVAILABLE) {

            }
        }
        return new VectorSchemaRoot(fields, valueVectors, 1);
    }

    private VectorSchemaRoot loadArray() {
        JsonToken jsonToken = parser.nextToken();
        while (jsonToken != JsonToken.END_ARRAY) {

        }
        return null;
    }

    @Override
    public void close() throws Exception {
        parser.close();
    }
}
