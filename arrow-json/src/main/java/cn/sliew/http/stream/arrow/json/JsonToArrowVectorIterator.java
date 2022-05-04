package cn.sliew.http.stream.arrow.json;

import cn.sliew.milky.common.exception.Rethrower;
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

    private static final JsonFactory jsonFactory = new JsonFactory();
    private static final ObjectMapper codec = new ObjectMapper();

    private final String originalJsonData;
    private final InputStream in;
    private final JsonToArrowConfig config;

    private Schema rootSchema;
    private JsonParser jsonParser;
    private JsonNode firstJsonNode;
    private JsonNode nextJsonNode;

    private boolean firstObjectConsumed = false;

    JsonToArrowVectorIterator(
            String originalJsonData,
            InputStream in,
            JsonToArrowConfig config) throws IOException {

        this.originalJsonData = originalJsonData;
        this.in = in;
        this.config = config;

        jsonParser = jsonFactory.createParser(in);
        jsonParser.setCodec(codec);
        initialize();
        moveToObject();
    }

    private void initialize() throws IOException {
        this.rootSchema = JsonToArrowUtils.inferSchema(originalJsonData);
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
        if (firstObjectConsumed == false) {
            return firstJsonNode != null;
        }
        if (jsonParser.isClosed()) {
            return false;
        }
        try {
            this.nextJsonNode = getJsonNode();
            return nextJsonNode != null;
        } catch (IOException e) {
            Rethrower.throwAs(e);
            // never go here
            return false;
        }
    }

    @Override
    public VectorSchemaRoot next() {
        try {
            final JsonNode nextJsonNode = getNextJsonNode();
            return convertJsonNodeToVector(nextJsonNode);
        } catch (IOException e) {
            Rethrower.throwAs(e);
            return null;
        }
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

        return nextJsonNode;
    }

    private JsonNode getJsonNode() throws IOException {
        while (true) {
            final JsonToken token = jsonParser.nextToken();
            if (token == null) {
                return null;
            }

            switch (token) {
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
        for (Field field : rootSchema.getFields()) {
            final String fieldName = field.getName();
            final JsonNode childNode = jsonNode.get(fieldName);
            final FieldVector fieldVector = consumerRawNodeValue(0, fieldName, field, null, childNode);
            fieldVectors.add(fieldVector);
        }
        return new VectorSchemaRoot(rootSchema, fieldVectors, 1);
    }

    private FieldVector consumerRawNodeValue(int vectorIndex, String fieldName, Field field, FieldVector consumerVector, JsonNode fieldNode) throws IOException {
        if (fieldNode == null || fieldNode.isNull()) {
            return null;
        }

        if (fieldNode.isNumber()) {
            Float8Vector float8Vector = (Float8Vector) JsonToArrowUtils.createVector(fieldName, field.getFieldType(), consumerVector, config.getAllocator());
            while (float8Vector.getValueCapacity() < vectorIndex + 1) {
                float8Vector.reAlloc();
            }
            float8Vector.set(vectorIndex, fieldNode.doubleValue());
            return float8Vector;
        }

        if (fieldNode.isBinary()) {
            VarBinaryVector varBinaryVector =(VarBinaryVector) JsonToArrowUtils.createVector(fieldName, field.getFieldType(), consumerVector, config.getAllocator());
            while (varBinaryVector.getValueCapacity() < vectorIndex + 1) {
                varBinaryVector.reAlloc();
            }
            varBinaryVector.set(vectorIndex, fieldNode.binaryValue());
            return varBinaryVector;
        }

        if (fieldNode.isBoolean()) {
            BitVector bitVector = (BitVector) JsonToArrowUtils.createVector(fieldName, field.getFieldType(), consumerVector, config.getAllocator());
            while (bitVector.getValueCapacity() < vectorIndex + 1) {
                bitVector.reAlloc();
            }
            bitVector.set(vectorIndex, fieldNode.booleanValue() ? 1 : 0);
            return bitVector;
        }

        if (fieldNode.isTextual()) {
            VarCharVector varCharVector =(VarCharVector) JsonToArrowUtils.createVector(fieldName, field.getFieldType(), consumerVector, config.getAllocator());
            while (varCharVector.getValueCapacity() < vectorIndex + 1) {
                varCharVector.reAlloc();
            }
            varCharVector.set(vectorIndex, new Text(fieldNode.textValue()));
            return varCharVector;
        }

        if (fieldNode.isArray()) {
            final ArrayNode arrayNode = (ArrayNode) fieldNode;
            long totalCount = arrayNode.size();

            final Field elementField = field.getChildren().get(0);
            ListVector listVector = (ListVector) field.createVector(config.getAllocator());
            if (listVector.getDataVector().getValueCapacity() < totalCount) {
                listVector.getDataVector().reAlloc();
            }

            listVector.startNewValue(vectorIndex);
            int count = 0;
            for (JsonNode node : arrayNode) {
                consumerRawNodeValue(count++, fieldName, elementField, listVector.getDataVector(), node);
            }
            listVector.endValue(vectorIndex, (int) totalCount);
            listVector.setValueCount((int)totalCount);
            return listVector;
        }

        if (fieldNode.isObject()) {
            final List<Field> children = field.getChildren();
            StructVector structVector = (StructVector) field.createVector(config.getAllocator());
            int childVectorIndex = 0;
            for (Field childField : children) {
                final JsonNode childJsonNode = fieldNode.get(childField.getName());
                final FieldVector valueVector = structVector.getChildrenFromFields().get(childVectorIndex);
                consumerRawNodeValue(vectorIndex, childField.getName(), childField, valueVector, childJsonNode);
                childVectorIndex++;
            }
            structVector.setIndexDefined(vectorIndex);
            return structVector;
        }

        return null;
    }
}
