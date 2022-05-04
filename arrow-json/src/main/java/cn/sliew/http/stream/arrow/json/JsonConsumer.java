package cn.sliew.http.stream.arrow.json;

import cn.sliew.milky.common.exception.Rethrower;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.arrow.vector.*;
import org.apache.arrow.vector.complex.ListVector;
import org.apache.arrow.vector.complex.StructVector;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.Schema;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.function.Consumer;

public class JsonConsumer implements Consumer<VectorSchemaRoot> {

    private final ObjectMapper objectMapper;
    private final OutputStream output;

    private final JsonGenerator generator;

    public JsonConsumer(ObjectMapper objectMapper, OutputStream output) throws IOException {
        this.objectMapper = objectMapper;
        this.output = output;
        this.generator = objectMapper.createGenerator(output);
    }

    @Override
    public void accept(VectorSchemaRoot vectorSchemaRoot) {
        try {
            Schema schema = vectorSchemaRoot.getSchema();
            for (int i = 0; i < vectorSchemaRoot.getRowCount(); i++) {
                generator.writeStartObject();
                for (Field field : schema.getFields()) {
                    FieldVector vector = vectorSchemaRoot.getVector(field.getName());
                    consume(i, field, vector);
                    generator.flush();
                }
                generator.writeEndObject();
            }
        } catch (IOException e) {
            Rethrower.throwAs(e);
        }
    }

    private void consume(int row, Field field, FieldVector fieldVector) throws IOException {

        if (fieldVector instanceof TinyIntVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof UInt1Vector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof UInt2Vector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row, fieldVector);
        } else if (fieldVector instanceof SmallIntVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row, fieldVector);
        } else if (fieldVector instanceof IntVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof UInt4Vector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof Float4Vector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row, fieldVector);
        } else if (fieldVector instanceof TimeSecVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof TimeMilliVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof BigIntVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row, fieldVector);
        } else if (fieldVector instanceof UInt8Vector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof Float8Vector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof DateMilliVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row, fieldVector);
        } else if (fieldVector instanceof TimeStampSecVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof TimeStampMilliVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof TimeStampMicroVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof TimeStampNanoVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row, fieldVector);
        } else if (fieldVector instanceof TimeMicroVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof Decimal256Vector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof DecimalVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof VarBinaryVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row, fieldVector);
        } else if (fieldVector instanceof VarCharVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof LargeVarCharVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof LargeVarBinaryVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof BitVector) {
            generator.writeFieldName(field.getName());
            consumeFieldVector(row,  fieldVector);
        } else if (fieldVector instanceof ListVector) {
            ListVector listVector = (ListVector) fieldVector;
            generator.writeArrayFieldStart(field.getName());
            FieldVector dataVector = listVector.getDataVector();
            for (int i = 0; i < dataVector.getValueCapacity(); i++) {
                consumeFieldVector(i, dataVector);
            }
            generator.writeEndArray();
        } else if (fieldVector instanceof StructVector) {
            StructVector structVector = (StructVector) fieldVector;
            generator.writeObjectFieldStart(field.getName());
            List<String> childFieldNames = structVector.getChildFieldNames();
            for (String childFieldName : childFieldNames) {
                FieldVector childFieldVector = structVector.getChild(childFieldName);
                generator.writeFieldName(childFieldName);
                consumeFieldVector(row, childFieldVector);
            }
            generator.writeEndObject();
        }
    }

    private void consumeFieldVector(int row, FieldVector fieldVector) throws IOException {
        if (fieldVector.isNull(row)) {
            generator.writeNull();
            return;
        }
        if (fieldVector instanceof TinyIntVector) {
            TinyIntVector tinyIntVector = (TinyIntVector) fieldVector;
            generator.writeNumber(tinyIntVector.getObject(row));
        } else if (fieldVector instanceof UInt1Vector) {
            UInt1Vector uInt1Vector = (UInt1Vector) fieldVector;
            generator.writeNumber(uInt1Vector.getObject(row));
        } else if (fieldVector instanceof UInt2Vector) {
            UInt2Vector uInt2Vector = (UInt2Vector) fieldVector;
            generator.writeNumber(uInt2Vector.getObject(row));
        } else if (fieldVector instanceof SmallIntVector) {
            SmallIntVector smallIntVector = (SmallIntVector) fieldVector;
            generator.writeNumber(smallIntVector.getObject(row));
        } else if (fieldVector instanceof IntVector) {
            IntVector intVector = (IntVector) fieldVector;
            generator.writeNumber(intVector.getObject(row));
        } else if (fieldVector instanceof UInt4Vector) {
            UInt4Vector uInt4Vector = (UInt4Vector) fieldVector;
            generator.writeNumber(uInt4Vector.getObject(row));
        } else if (fieldVector instanceof Float4Vector) {
            Float4Vector float4Vector = (Float4Vector) fieldVector;
            generator.writeNumber(float4Vector.getObject(row));
        } else if (fieldVector instanceof TimeSecVector) {
            TimeSecVector timeSecVector = (TimeSecVector) fieldVector;
            Date date = new Date(timeSecVector.getObject(row) * 1000);
            generator.writeNumber(date.getTime());
        } else if (fieldVector instanceof TimeMilliVector) {
            TimeMilliVector timeMilliVector = (TimeMilliVector) fieldVector;
            Timestamp timestamp = Timestamp.valueOf(timeMilliVector.getObject(row));
            generator.writeNumber(timestamp.getTime());
        } else if (fieldVector instanceof BigIntVector) {
            BigIntVector bigIntVector = (BigIntVector) fieldVector;
            generator.writeNumber(bigIntVector.getObject(row));
        } else if (fieldVector instanceof UInt8Vector) {
            UInt8Vector uInt8Vector = (UInt8Vector) fieldVector;
            generator.writeNumber(uInt8Vector.getObject(row));
        } else if (fieldVector instanceof Float8Vector) {
            Float8Vector float8Vector = (Float8Vector) fieldVector;
            generator.writeNumber(float8Vector.getObject(row));
        } else if (fieldVector instanceof DateMilliVector) {
            DateMilliVector dateMilliVector = (DateMilliVector) fieldVector;
            Timestamp timestamp = Timestamp.valueOf(dateMilliVector.getObject(row));
            generator.writeNumber(timestamp.getTime());
        } else if (fieldVector instanceof TimeStampSecVector) {
            TimeStampSecVector timeStampSecVector = (TimeStampSecVector) fieldVector;
            Timestamp timestamp = Timestamp.valueOf(timeStampSecVector.getObject(row));
            generator.writeNumber(timestamp.getTime());
        } else if (fieldVector instanceof TimeStampMilliVector) {
            TimeStampMilliVector timeStampMilliVector = (TimeStampMilliVector) fieldVector;
            Timestamp timestamp = Timestamp.valueOf(timeStampMilliVector.getObject(row));
            generator.writeNumber( timestamp.getTime());
        } else if (fieldVector instanceof TimeStampMicroVector) {
            TimeStampMicroVector timeStampMicroVector = (TimeStampMicroVector) fieldVector;
            Timestamp timestamp = Timestamp.valueOf(timeStampMicroVector.getObject(row));
            generator.writeNumber(timestamp.getTime());
        } else if (fieldVector instanceof TimeStampNanoVector) {
            TimeStampNanoVector timeStampNanoVector = (TimeStampNanoVector) fieldVector;
            Timestamp timestamp = Timestamp.valueOf(timeStampNanoVector.getObject(row));
            generator.writeNumber( timestamp.getTime());
        } else if (fieldVector instanceof TimeMicroVector) {
            TimeMicroVector timeMicroVector = (TimeMicroVector) fieldVector;
            Date date = new Date(timeMicroVector.getObject(row) / 1000);
            generator.writeNumber(date.getTime());
        } else if (fieldVector instanceof Decimal256Vector) {
            Decimal256Vector decimal256Vector = (Decimal256Vector) fieldVector;
            generator.writeNumber( decimal256Vector.getObject(row));
        } else if (fieldVector instanceof DecimalVector) {
            DecimalVector decimalVector = (DecimalVector) fieldVector;
            generator.writeNumber(decimalVector.getObject(row));
        } else if (fieldVector instanceof VarBinaryVector) {
            VarBinaryVector varBinaryVector = (VarBinaryVector) fieldVector;
            generator.writeBinary( varBinaryVector.getObject(row));
        } else if (fieldVector instanceof VarCharVector) {
            VarCharVector varCharVector = (VarCharVector) fieldVector;
            generator.writeString(varCharVector.getObject(row).toString());
        } else if (fieldVector instanceof LargeVarCharVector) {
            LargeVarCharVector largeVarCharVector = (LargeVarCharVector) fieldVector;
            generator.writeString(largeVarCharVector.getObject(row).toString());
        } else if (fieldVector instanceof LargeVarBinaryVector) {
            LargeVarBinaryVector largeVarBinaryVector = (LargeVarBinaryVector) fieldVector;
            generator.writeBinary(largeVarBinaryVector.getObject(row));
        } else if (fieldVector instanceof BitVector) {
            BitVector bitVector = (BitVector) fieldVector;
            generator.writeBoolean(bitVector.getObject(row));
        } else if (fieldVector instanceof ListVector) {
            ListVector listVector = (ListVector) fieldVector;
            generator.writeStartArray();
            FieldVector dataVector = listVector.getDataVector();
            for (int i = 0; i < dataVector.getValueCapacity(); i++) {
                consumeFieldVector(i, dataVector);
            }
            generator.writeEndArray();
        } else if (fieldVector instanceof StructVector) {
            StructVector structVector = (StructVector) fieldVector;
            generator.writeStartObject();
            int childVectorIndex = 0;
            List<String> childFieldNames = structVector.getChildFieldNames();
            for (String childFieldName : childFieldNames) {
                FieldVector childFieldVector = structVector.getChild(childFieldName);
                consumeFieldVector(childVectorIndex++, childFieldVector);
            }
            generator.writeEndObject();
        }
    }
}
