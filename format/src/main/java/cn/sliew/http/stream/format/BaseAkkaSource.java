package cn.sliew.http.stream.format;

import org.apache.seatunnel.apis.BaseSource;

/**
 * a base interface indicates a source plugin running with akka.
 * @param <Source>
 */
public interface BaseAkkaSource<Source> extends BaseSource<AkkaEnvironment> {

    Source getData(AkkaEnvironment env);
}
