package cn.sliew.http.stream.format.jdbc;

import akka.Done;
import akka.stream.javadsl.Sink;
import cn.sliew.http.stream.format.jdbc.sink.JdbcSinkBuilder;
import cn.sliew.http.stream.format.jdbc.sql.DataSourceOptions;
import org.apache.arrow.vector.VectorSchemaRoot;

import java.util.concurrent.CompletionStage;

public class Main {

    public static void main(String[] args) {
        JdbcSinkBuilder builder = new JdbcSinkBuilder();
        DataSourceOptions dataSourceOptions = DataSourceOptions.builder()
                .driverName("com.mysql.cj.jdbc.Driver")
                .jdbcUrl("jdbc:mysql://localhost:3301/data_service?serverTimezone=Asia/Shanghai&zeroDateTimeBehavior=convertToNull&useAffectedRows=true")
                .username("root")
                .password("123")
                .build();
        builder.setDataSourceOptions(dataSourceOptions);
        builder.setSql("insert into test_source (age, name) values (#{age}, #{name)");
        Sink<VectorSchemaRoot, CompletionStage<Done>> sink = builder.build();




    }
}
