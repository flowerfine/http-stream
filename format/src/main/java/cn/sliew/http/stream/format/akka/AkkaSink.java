package cn.sliew.http.stream.format.akka;

import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import cn.sliew.http.stream.format.AkkaEnvironment;
import cn.sliew.http.stream.format.BaseAkkaSink;

public interface AkkaSink<In, Out, Mat> extends BaseAkkaSink {

    Sink<Out, Mat> outputStream(AkkaEnvironment env, Flow<In, Out, Mat> source);
}
