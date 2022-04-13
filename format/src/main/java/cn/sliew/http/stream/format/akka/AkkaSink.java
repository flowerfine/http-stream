package cn.sliew.http.stream.format.akka;

import akka.NotUsed;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import cn.sliew.http.stream.format.AkkaEnvironment;
import cn.sliew.http.stream.format.BaseAkkaSink;

public interface AkkaSink<In, Out> extends BaseAkkaSink {

    Sink<Out, NotUsed> outputStream(AkkaEnvironment env, Flow<In, Out, NotUsed> source);
}
