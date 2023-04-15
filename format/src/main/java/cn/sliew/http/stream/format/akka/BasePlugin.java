package cn.sliew.http.stream.format.akka;

import cn.sliew.http.stream.format.AkkaEnvironment;
import org.apache.seatunnel.common.config.CheckResult;
import org.apache.seatunnel.plugin.Plugin;
import org.apache.seatunnel.shade.com.typesafe.config.Config;

public abstract class BasePlugin implements Plugin<AkkaEnvironment> {

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
    public void prepare(AkkaEnvironment unused) {

    }
}
