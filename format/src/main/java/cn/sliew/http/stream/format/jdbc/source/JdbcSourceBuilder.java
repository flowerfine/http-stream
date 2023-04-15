package cn.sliew.http.stream.format.jdbc.source;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import cn.sliew.http.stream.format.jdbc.BaseJdbcConnector;
import org.apache.arrow.adapter.jdbc.ArrowVectorIterator;
import org.apache.arrow.adapter.jdbc.JdbcToArrow;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.VectorSchemaRoot;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JdbcSourceBuilder extends BaseJdbcConnector<JdbcSourceBuilder> {

    public Source<VectorSchemaRoot, NotUsed> build() {
        return Source.fromIterator(this::execute);
    }

    private ArrowVectorIterator execute() throws SQLException, IOException {
        Connection connection = dataSource.getConnection();
        PreparedStatement statement = connection.prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        statement.execute();
        ResultSet resultSet = statement.getResultSet();
        return JdbcToArrow.sqlToArrowVectorIterator(resultSet, new RootAllocator());
    }
}
