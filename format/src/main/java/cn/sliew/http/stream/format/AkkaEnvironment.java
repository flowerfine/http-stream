package cn.sliew.http.stream.format;

import org.apache.seatunnel.common.config.CheckResult;
import org.apache.seatunnel.env.RuntimeEnv;
import org.apache.seatunnel.shade.com.typesafe.config.Config;

public class AkkaEnvironment implements RuntimeEnv {

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
    public void prepare(Boolean aBoolean) {

    }
}
