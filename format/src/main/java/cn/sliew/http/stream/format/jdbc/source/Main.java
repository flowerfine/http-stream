package cn.sliew.http.stream.format.jdbc.source;

import akka.Done;
import akka.NotUsed;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.SpawnProtocol;
import akka.actor.typed.javadsl.Behaviors;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import cn.sliew.http.stream.format.jdbc.sink.JdbcSinkBuilder;
import cn.sliew.http.stream.format.jdbc.sql.DataSourceOptions;
import org.apache.arrow.vector.VectorSchemaRoot;

import java.util.concurrent.CompletionStage;

public class Main {

    public static void main(String[] args) {
        DataSourceOptions dataSourceOptions = DataSourceOptions.builder()
                .driverName("com.mysql.cj.jdbc.Driver")
                .jdbcUrl("jdbc:mysql://localhost:3306/data_service?serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=convertToNull&useAffectedRows=true")
                .username("root")
                .password("123")
                .build();
        JdbcSourceBuilder jdbcSourceBuilder = new JdbcSourceBuilder();
        jdbcSourceBuilder.setDataSource(dataSourceOptions);
        jdbcSourceBuilder.setSql("select * from test_source");
        Source<VectorSchemaRoot, NotUsed> source = jdbcSourceBuilder.build();

        JdbcSinkBuilder jdbcSinkBuilder = new JdbcSinkBuilder();
        jdbcSinkBuilder.setDataSource(dataSourceOptions);
        jdbcSinkBuilder.setSql("insert into test_sink (age, name) values (#{age}, #{name})");
        Sink<VectorSchemaRoot, CompletionStage<Done>> sink = jdbcSinkBuilder.build();
        ActorSystem<SpawnProtocol.Command> actorSystem = ActorSystem.create(Behaviors.setup(ctx -> SpawnProtocol.create()), "main");
        CompletionStage<Done> stage = source.runWith(sink, actorSystem);
        stage.whenComplete(((done, throwable) -> {
            if (throwable != null) {
                throwable.printStackTrace();
            }
            actorSystem.terminate();
        }));
    }
}
