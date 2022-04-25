package cn.sliew.http.stream.arrow.json.consumer;

import com.fasterxml.jackson.core.JsonParser;

import java.io.IOException;

/**
 * Adapter function to skip (throw away) data from the parser.
 */
@FunctionalInterface
public interface SkipFunction {

    void apply(JsonParser parser) throws IOException;
}