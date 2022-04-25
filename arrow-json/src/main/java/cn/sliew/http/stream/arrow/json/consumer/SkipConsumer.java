package cn.sliew.http.stream.arrow.json.consumer;

import com.fasterxml.jackson.core.JsonParser;
import org.apache.arrow.vector.FieldVector;

import java.io.IOException;

/**
 * Consumer which skip (throw away) data from the parser.
 */
public class SkipConsumer implements Consumer {

    private final SkipFunction skipFunction;

    public SkipConsumer(SkipFunction skipFunction) {
        this.skipFunction = skipFunction;
    }

    @Override
    public void consume(JsonParser parser) throws IOException {
        skipFunction.apply(parser);
    }

    @Override
    public void addNull() {
    }

    @Override
    public void setPosition(int index) {
    }

    @Override
    public FieldVector getVector() {
        return null;
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public boolean resetValueVector(FieldVector vector) {
        return false;
    }

}