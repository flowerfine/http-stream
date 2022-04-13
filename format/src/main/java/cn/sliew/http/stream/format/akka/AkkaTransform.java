package cn.sliew.http.stream.format.akka;

import akka.NotUsed;
import akka.stream.javadsl.Flow;
import cn.sliew.http.stream.format.AkkaEnvironment;
import cn.sliew.http.stream.format.BaseAkkaTransform;

public interface AkkaTransform<In, Out> extends BaseAkkaTransform {

    Flow<In, Out, NotUsed> processStream(AkkaEnvironment env, Flow<In, Out, NotUsed> flow) throws Exception;
}
