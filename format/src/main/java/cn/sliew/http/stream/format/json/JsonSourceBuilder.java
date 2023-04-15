package cn.sliew.http.stream.format.json;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import org.apache.arrow.vector.VectorSchemaRoot;

public class JsonSourceBuilder {

    private String path;

    public Source<VectorSchemaRoot, NotUsed> build() {
        return null;
    }
}
