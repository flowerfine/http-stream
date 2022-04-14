package cn.sliew.http.stream.format.http;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import cn.sliew.http.stream.format.AkkaEnvironment;
import cn.sliew.http.stream.format.akka.AkkaSource;
import cn.sliew.http.stream.format.akka.BasePlugin;
import org.apache.arrow.vector.VectorSchemaRoot;

/**
 * arrow schema
 *
 * @see org.apache.arrow.vector.types.pojo.Schema
 */
public class HttpSource extends BasePlugin implements AkkaSource {

    @Override
    public Source<VectorSchemaRoot, NotUsed> getData(AkkaEnvironment env) {

        return null;
    }
}
