package cn.sliew.http.stream.format.akka;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import cn.sliew.http.stream.format.AkkaEnvironment;
import cn.sliew.http.stream.format.BaseAkkaSource;
import org.apache.arrow.vector.VectorSchemaRoot;

public interface AkkaSource extends BaseAkkaSource<Source<VectorSchemaRoot, NotUsed>> {

    @Override
    Source<VectorSchemaRoot, NotUsed> getData(AkkaEnvironment env);
}
