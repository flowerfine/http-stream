package cn.sliew.http.stream.arrow.json;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.apache.arrow.util.Preconditions;
import org.apache.arrow.vector.*;
import org.apache.arrow.vector.complex.ListVector;
import org.apache.arrow.vector.complex.StructVector;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.Schema;
import org.apache.arrow.vector.util.Text;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class JsonToArrowVectorIterator implements Iterator<VectorSchemaRoot>, AutoCloseable {

    public static final int NO_LIMIT_BATCH_SIZE = -1;
    public static final int DEFAULT_BATCH_SIZE = 1024;

    private static final JsonFactory jsonFactory = new JsonFactory();
    private static final ObjectMapper codec = new ObjectMapper();

    private final String originalJsonData;
    private final InputStream in;
    private final JsonToArrowConfig config;

    private Schema rootSchema;
    private JsonParser jsonParser;
    private JsonNode firstJsonNode;

    private boolean firstObjectConsumed = false;

    private final int targetBatchSize;

    JsonToArrowVectorIterator(
            String originalJsonData,
            InputStream in,
            JsonToArrowConfig config) throws IOException {

        this.originalJsonData = originalJsonData;
        this.in = in;
        this.config = config;
        this.targetBatchSize = config.getTargetBatchSize();

        jsonParser = jsonFactory.createParser(in);
        jsonParser.setCodec(codec);
        initialize();
        moveToObject();
    }

    private void initialize() throws IOException {
        this.rootSchema = JsonToArrowUtils.inferSchema(originalJsonData);
        System.out.println(rootSchema.toJson());
    }

    private void moveToObject() throws IOException {
        JsonToken token = jsonParser.nextToken();
        if (token == JsonToken.START_ARRAY) {
            token = jsonParser.nextToken(); // advance to START_OBJECT token
        }

        if (token == JsonToken.START_OBJECT) { // could be END_ARRAY also
            firstJsonNode = jsonParser.readValueAsTree();
        } else {
            firstJsonNode = null;
        }
    }

    @Override
    public boolean hasNext() {
        return jsonParser.isClosed() == false;
    }

    @Override
    public VectorSchemaRoot next() {
        try {
            Preconditions.checkArgument(hasNext());
            final JsonNode nextJsonNode = getNextJsonNode();
            return convertJsonNodeToVector(nextJsonNode);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void close() throws Exception {
        jsonParser.close();
    }

    private JsonNode getNextJsonNode() throws IOException {
        if (!firstObjectConsumed) {
            firstObjectConsumed = true;
            return firstJsonNode;
        }

        return getJsonNode();
    }

    private JsonNode getJsonNode() throws IOException {
        while (true) {
            final JsonToken token = jsonParser.nextToken();
            if (token == null) {
                return null;
            }

            switch (token) {
                case START_ARRAY:
                case END_ARRAY:
                case END_OBJECT:
                    break;
                case START_OBJECT:
                    return jsonParser.readValueAsTree();
                default:
                    throw new IllegalStateException("Expected to get a JSON Object but got a token of type " + token.name());
            }
        }
    }

    private VectorSchemaRoot convertJsonNodeToVector(JsonNode jsonNode) throws IOException {
        List<FieldVector> fieldVectors = new ArrayList<>();
        final List<Field> fields = rootSchema.getFields();
        for (int i = 0; i < fields.size(); i++) {
            final Field field = fields.get(i);
            final String fieldName = field.getName();
            final JsonNode childNode = jsonNode.get(fieldName);
            final FieldVector fieldVector = getRawNodeValue(i, fieldName, field, childNode);
            fieldVectors.add(fieldVector);
        }
        return new VectorSchemaRoot(rootSchema, fieldVectors, 1);
    }

    private FieldVector getRawNodeValue(int vectorIndex, String fieldName, Field field, final JsonNode fieldNode) throws IOException {
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }

        final FieldVector fieldVector = field.getFieldType().createNewSingleVector(fieldName, config.getAllocator(), null);
        fieldVector.allocateNew();

        if (fieldNode.isNumber()) {
            Float8Vector float8Vector = (Float8Vector) fieldVector;
            float8Vector.set(vectorIndex, fieldNode.doubleValue());
            return float8Vector;
        }

        if (fieldNode.isBinary()) {
            VarBinaryVector varBinaryVector = (VarBinaryVector) fieldVector;
            varBinaryVector.set(vectorIndex, fieldNode.binaryValue());
            return varBinaryVector;
        }

        if (fieldNode.isBoolean()) {
            BitVector bitVector = (BitVector) fieldVector;
            bitVector.set(vectorIndex, fieldNode.booleanValue() ? 1 : 0);
            return bitVector;
        }

        if (fieldNode.isTextual()) {
            VarCharVector varCharVector = (VarCharVector) fieldVector;
            varCharVector.set(vectorIndex, new Text(fieldNode.textValue()));
            return varCharVector;
        }

        if (fieldNode.isArray()) {
            final ArrayNode arrayNode = (ArrayNode) fieldNode;
            int count = 0;

            final List<Field> children = field.getChildren();
            final Field elementField = children.get(0);
            ListVector listVector = (ListVector) fieldVector;
            listVector.startNewValue(vectorIndex);
            long totalCount = 0;
            for (final JsonNode node : arrayNode) {
                getRawNodeValue(count++, fieldName, elementField, node);
            }
            listVector.endValue(vectorIndex, (int) totalCount);
            return listVector;

        }

        if (fieldNode.isObject()) {
            final List<Field> children = field.getChildren();
            StructVector structVector = (StructVector) fieldVector;
            int childVectorIndex = 0;
            for (Field childField : children) {
                final JsonNode childJsonNode = fieldNode.get(childField.getName());
                getRawNodeValue(childVectorIndex++, childField.getName(), childField, childJsonNode);
            }
            structVector.setIndexDefined(vectorIndex);
            return structVector;
        }

        return null;
    }
}
