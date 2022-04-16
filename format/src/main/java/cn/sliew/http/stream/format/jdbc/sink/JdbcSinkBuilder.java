package cn.sliew.http.stream.format.jdbc.sink;

import akka.Done;
import akka.stream.javadsl.Sink;
import cn.sliew.http.stream.format.jdbc.BaseJdbcConnector;
import cn.sliew.http.stream.format.jdbc.sql.SqlBuilder;
import org.apache.arrow.vector.*;

import java.sql.*;
import java.util.List;
import java.util.concurrent.CompletionStage;

public class JdbcSinkBuilder extends BaseJdbcConnector<JdbcSinkBuilder> {

    private List<String> parameters;

    @Override
    public JdbcSinkBuilder setSql(String sql) {
        SqlBuilder sqlBuilder = new SqlBuilder(sql);
        sqlBuilder.parse();
        this.parameters = sqlBuilder.getParameters();
        return super.setSql(sqlBuilder.getSql());
    }

    public Sink<VectorSchemaRoot, CompletionStage<Done>> build() {
        return Sink.foreach(this::execute);
    }

    private void execute(VectorSchemaRoot vectorSchemaRoot) throws SQLException {
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        for (int row = 0; row < vectorSchemaRoot.getRowCount(); row++) {
            for (int i = 0; i < parameters.size(); i++) {
                FieldVector fieldVector = vectorSchemaRoot.getVector(parameters.get(i));
                setParameter(statement, i + 1, row, fieldVector);
            }
            statement.addBatch();
        }
        statement.executeBatch();
        statement.close();
        connection.close();
        vectorSchemaRoot.close();
    }

    private void setParameter(PreparedStatement statement, int parameterIndex, int row, FieldVector fieldVector) throws SQLException {
        if (fieldVector.isNull(row)) {
            statement.setNull(parameterIndex, Types.NULL);
            return;
        }
        if (fieldVector instanceof TinyIntVector) {
            TinyIntVector tinyIntVector = (TinyIntVector) fieldVector;
            statement.setByte(parameterIndex, tinyIntVector.getObject(row));
        } else if (fieldVector instanceof UInt1Vector) {
            UInt1Vector uInt1Vector = (UInt1Vector) fieldVector;
            statement.setByte(parameterIndex, uInt1Vector.getObject(row));
        } else if (fieldVector instanceof UInt2Vector) {
            UInt2Vector uInt2Vector = (UInt2Vector) fieldVector;
            statement.setString(parameterIndex, uInt2Vector.getObject(row).toString());
        } else if (fieldVector instanceof SmallIntVector) {
            SmallIntVector smallIntVector = (SmallIntVector) fieldVector;
            statement.setShort(parameterIndex, smallIntVector.getObject(row));
        } else if (fieldVector instanceof IntVector) {
            IntVector intVector = (IntVector) fieldVector;
            statement.setInt(parameterIndex, intVector.getObject(row));
        } else if (fieldVector instanceof UInt4Vector) {
            UInt4Vector uInt4Vector = (UInt4Vector) fieldVector;
            statement.setInt(parameterIndex, uInt4Vector.getObject(row));
        } else if (fieldVector instanceof Float4Vector) {
            Float4Vector float4Vector = (Float4Vector) fieldVector;
            statement.setFloat(parameterIndex, float4Vector.getObject(row));
        } else if (fieldVector instanceof TimeSecVector) {
            TimeSecVector timeSecVector = (TimeSecVector) fieldVector;
            statement.setDate(parameterIndex, new Date(timeSecVector.getObject(row) * 1000));
        } else if (fieldVector instanceof TimeMilliVector) {
            TimeMilliVector timeMilliVector = (TimeMilliVector) fieldVector;
            statement.setTimestamp(parameterIndex, Timestamp.valueOf(timeMilliVector.getObject(row)));
        } else if (fieldVector instanceof BigIntVector) {
            BigIntVector bigIntVector = (BigIntVector) fieldVector;
            statement.setLong(parameterIndex, bigIntVector.getObject(row));
        } else if (fieldVector instanceof UInt8Vector) {
            UInt8Vector uInt8Vector = (UInt8Vector) fieldVector;
            statement.setLong(parameterIndex, uInt8Vector.getObject(row));
        } else if (fieldVector instanceof Float8Vector) {
            Float8Vector float8Vector = (Float8Vector) fieldVector;
            statement.setDouble(parameterIndex, float8Vector.getObject(row));
        } else if (fieldVector instanceof DateMilliVector) {
            DateMilliVector dateMilliVector = (DateMilliVector) fieldVector;
            statement.setTimestamp(parameterIndex, Timestamp.valueOf(dateMilliVector.getObject(row)));
        } else if (fieldVector instanceof TimeStampSecVector) {
            TimeStampSecVector timeStampSecVector = (TimeStampSecVector) fieldVector;
            statement.setTimestamp(parameterIndex, Timestamp.valueOf(timeStampSecVector.getObject(row)));
        } else if (fieldVector instanceof TimeStampMilliVector) {
            TimeStampMilliVector timeStampMilliVector = (TimeStampMilliVector) fieldVector;
            statement.setTimestamp(parameterIndex, Timestamp.valueOf(timeStampMilliVector.getObject(row)));
        } else if (fieldVector instanceof TimeStampMicroVector) {
            TimeStampMicroVector timeStampMicroVector = (TimeStampMicroVector) fieldVector;
            statement.setTimestamp(parameterIndex, Timestamp.valueOf(timeStampMicroVector.getObject(row)));
        } else if (fieldVector instanceof TimeStampNanoVector) {
            TimeStampNanoVector timeStampNanoVector = (TimeStampNanoVector) fieldVector;
            statement.setTimestamp(parameterIndex, Timestamp.valueOf(timeStampNanoVector.getObject(row)));
        } else if (fieldVector instanceof TimeMicroVector) {
            TimeMicroVector timeMicroVector = (TimeMicroVector) fieldVector;
            statement.setDate(parameterIndex, new Date(timeMicroVector.getObject(row) / 1000));
        } else if (fieldVector instanceof Decimal256Vector) {
            Decimal256Vector decimal256Vector = (Decimal256Vector) fieldVector;
            statement.setBigDecimal(parameterIndex, decimal256Vector.getObject(row));
        } else if (fieldVector instanceof DecimalVector) {
            DecimalVector decimalVector = (DecimalVector) fieldVector;
            statement.setBigDecimal(parameterIndex, decimalVector.getObject(row));
        } else if (fieldVector instanceof VarBinaryVector) {
            VarBinaryVector varBinaryVector = (VarBinaryVector) fieldVector;
            statement.setBytes(parameterIndex, varBinaryVector.getObject(row));
        } else if (fieldVector instanceof VarCharVector) {
            VarCharVector varCharVector = (VarCharVector) fieldVector;
            statement.setString(parameterIndex, varCharVector.getObject(row).toString());
        } else if (fieldVector instanceof LargeVarCharVector) {
            LargeVarCharVector largeVarCharVector = (LargeVarCharVector) fieldVector;
            statement.setString(parameterIndex, largeVarCharVector.getObject(row).toString());
        } else if (fieldVector instanceof LargeVarBinaryVector) {
            LargeVarBinaryVector largeVarBinaryVector = (LargeVarBinaryVector) fieldVector;
            statement.setBytes(parameterIndex, largeVarBinaryVector.getObject(row));
        } else if (fieldVector instanceof BitVector) {
            BitVector bitVector = (BitVector) fieldVector;
            statement.setBoolean(parameterIndex, bitVector.getObject(row));
        }
    }
}
