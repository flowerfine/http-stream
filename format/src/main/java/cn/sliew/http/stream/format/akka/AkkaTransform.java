package cn.sliew.http.stream.format.akka;

import akka.NotUsed;
import akka.stream.javadsl.Flow;
import cn.sliew.http.stream.format.AkkaEnvironment;
import cn.sliew.http.stream.format.BaseAkkaTransform;
import org.apache.arrow.vector.VectorSchemaRoot;

public interface AkkaTransform extends BaseAkkaTransform {

    Flow<VectorSchemaRoot, VectorSchemaRoot, NotUsed> processStream(AkkaEnvironment env) throws Exception;
}
