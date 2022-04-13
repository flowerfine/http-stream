package cn.sliew.http.stream.format.akka;

import akka.stream.javadsl.Source;
import cn.sliew.http.stream.format.AkkaEnvironment;
import cn.sliew.http.stream.format.BaseAkkaSource;

public interface AkkaSource<Out, Mat> extends BaseAkkaSource<Source<Out, Mat>> {

    @Override
    Source<Out, Mat> getData(AkkaEnvironment env);
}
