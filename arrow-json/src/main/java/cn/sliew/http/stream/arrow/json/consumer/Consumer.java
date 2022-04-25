package cn.sliew.http.stream.arrow.json.consumer;

import com.fasterxml.jackson.core.JsonParser;
import org.apache.arrow.vector.FieldVector;

import java.io.IOException;

public interface Consumer<T extends FieldVector> extends AutoCloseable {

    /**
     * @param parser
     * @see com.fasterxml.jackson.core.JsonGenerator
     * @see com.fasterxml.jackson.core.JsonParser
     */
    void consume(JsonParser parser) throws IOException;

    void addNull();

    void setPosition(int index);

    FieldVector getVector();

    boolean resetValueVector(T vector);
}
