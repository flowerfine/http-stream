package cn.sliew.http.stream.format.akka;

import akka.NotUsed;
import akka.stream.javadsl.Sink;
import cn.sliew.http.stream.format.AkkaEnvironment;
import cn.sliew.http.stream.format.BaseAkkaSink;
import org.apache.arrow.vector.VectorSchemaRoot;

public interface AkkaSink extends BaseAkkaSink {

    Sink<VectorSchemaRoot, NotUsed> outputStream(AkkaEnvironment env);
}
