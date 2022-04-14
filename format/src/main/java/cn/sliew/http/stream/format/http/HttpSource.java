package cn.sliew.http.stream.format.http;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import cn.sliew.http.stream.format.AkkaEnvironment;
import cn.sliew.http.stream.format.akka.AkkaSource;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.seatunnel.common.config.CheckResult;
import org.apache.seatunnel.shade.com.typesafe.config.Config;

/**
 * arrow schema
 *
 * @see org.apache.arrow.vector.types.pojo.Schema
 */
public class HttpSource implements AkkaSource<VectorSchemaRoot, NotUsed> {

    private Config config;

    @Override
    public void setConfig(Config config) {
        this.config = config;
    }

    @Override
    public Config getConfig() {
        return config;
    }

    @Override
    public CheckResult checkConfig() {
        return CheckResult.success();
    }

    @Override
    public void prepare(AkkaEnvironment akkaEnvironment) {

    }

    @Override
    public Source<VectorSchemaRoot, NotUsed> getData(AkkaEnvironment env) {

        return null;
    }
}
