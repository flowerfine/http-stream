package cn.sliew.http.stream.format.akka;

import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Source;
import cn.sliew.http.stream.format.AkkaEnvironment;
import cn.sliew.http.stream.format.BaseAkkaTransform;

public interface AkkaTransform<In, Out, Mat> extends BaseAkkaTransform {

    Flow<In, Out, Mat> processStream(AkkaEnvironment env, Source<Out, Mat> source) throws Exception;

    Flow<In, Out, Mat> processStream(AkkaEnvironment env, Flow<In, Out, Mat> flow) throws Exception;
}
