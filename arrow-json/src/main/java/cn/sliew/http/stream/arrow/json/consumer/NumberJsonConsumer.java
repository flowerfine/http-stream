package cn.sliew.http.stream.arrow.json.consumer;

import com.fasterxml.jackson.core.JsonParser;
import org.apache.arrow.vector.IntVector;

import java.io.IOException;

public class NumberJsonConsumer extends BaseJsonConsumer<IntVector> {

    public NumberJsonConsumer(IntVector vector) {
        super(vector);
    }

    @Override
    public void consume(JsonParser parser) throws IOException {

    }
}
