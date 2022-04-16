package cn.sliew.http.stream.format.jdbc;

import akka.Done;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.SpawnProtocol;
import akka.actor.typed.javadsl.Behaviors;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import cn.sliew.http.stream.format.jdbc.sink.JdbcSinkBuilder;
import cn.sliew.http.stream.format.jdbc.sql.DataSourceOptions;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.IntVector;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.apache.arrow.vector.types.pojo.FieldType;
import org.apache.arrow.vector.types.pojo.Schema;
import org.apache.arrow.vector.util.Text;

import java.util.List;
import java.util.concurrent.CompletionStage;

public class Main {

    public static void main(String[] args) throws Exception {
        JdbcSinkBuilder builder = new JdbcSinkBuilder();
        DataSourceOptions dataSourceOptions = DataSourceOptions.builder()
                .driverName("com.mysql.cj.jdbc.Driver")
                .jdbcUrl("jdbc:mysql://localhost:3306/data_service?serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=convertToNull&useAffectedRows=true")
                .username("root")
                .password("123")
                .build();
        builder.setDataSourceOptions(dataSourceOptions);
        builder.setSql("insert into test_source (age, name) values (#{age}, #{name})");
        Sink<VectorSchemaRoot, CompletionStage<Done>> sink = builder.build();

        ActorSystem<SpawnProtocol.Command> actorSystem = ActorSystem.create(Behaviors.setup(ctx -> SpawnProtocol.create()), "main");
        CompletionStage<Done> stage = Source.single(getVectorSchemaRoot()).runWith(sink, actorSystem);
        stage.whenComplete(((done, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
            actorSystem.terminate();
        }));
    }

    private static VectorSchemaRoot getVectorSchemaRoot() {
        VectorSchemaRoot vectorSchemaRoot = createVectorSchemaRoot();
        vectorSchemaRoot.allocateNew();
        IntVector age = (IntVector) vectorSchemaRoot.getVector("age");
        age.set(0, 10);
        age.setValueCount(1);
        VarCharVector name = (VarCharVector) vectorSchemaRoot.getVector("name");
        name.set(0, new Text("wangqi"));
        name.setValueCount(1);
        vectorSchemaRoot.setRowCount(1);
        return vectorSchemaRoot;
    }

    private static VectorSchemaRoot createVectorSchemaRoot() {
        Schema schema = createSchema();
        RootAllocator rootAllocator = new RootAllocator();
        return VectorSchemaRoot.create(schema, rootAllocator);
    }

    private static Schema createSchema() {
        Field age = new Field("age", FieldType.nullable(new ArrowType.Int(32, true)), null);
        Field name = new Field("name", FieldType.nullable(new ArrowType.Utf8()), null);
        return new Schema(List.of(age, name));
    }
}
