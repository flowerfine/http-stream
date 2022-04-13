package cn.sliew.http.stream.format.akka;

import akka.NotUsed;
import akka.stream.javadsl.Source;
import cn.sliew.http.stream.format.AkkaEnvironment;
import cn.sliew.http.stream.format.BaseAkkaSource;

public interface AkkaSource<In> extends BaseAkkaSource<Source<In, NotUsed>> {

    @Override
    Source<In, NotUsed> getData(AkkaEnvironment env);
}
