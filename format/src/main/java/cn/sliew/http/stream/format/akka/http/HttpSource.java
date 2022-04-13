package cn.sliew.http.stream.format.akka.http;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import cn.sliew.http.stream.format.AkkaEnvironment;
import cn.sliew.http.stream.format.akka.AkkaSource;
import org.apache.seatunnel.common.config.CheckResult;
import org.apache.seatunnel.shade.com.typesafe.config.Config;

public class HttpSource<T> implements AkkaSource<T, NotUsed> {

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
    public void prepare(AkkaEnvironment environment) {

    }

    @Override
    public Source<T, NotUsed> getData(AkkaEnvironment env) {
        return null;
    }
}
