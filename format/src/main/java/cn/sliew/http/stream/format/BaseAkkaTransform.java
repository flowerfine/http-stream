package cn.sliew.http.stream.format;

import org.apache.seatunnel.apis.BaseTransform;

/**
 * a base interface indicates a transform plugin running with akka.
 */
public interface BaseAkkaTransform extends BaseTransform<AkkaEnvironment> {

}
