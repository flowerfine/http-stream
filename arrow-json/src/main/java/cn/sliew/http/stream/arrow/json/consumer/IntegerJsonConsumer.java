package cn.sliew.http.stream.arrow.json.consumer;

import com.fasterxml.jackson.core.JsonParser;
import org.apache.arrow.vector.IntVector;

import java.io.IOException;

public class IntegerJsonConsumer extends BaseJsonConsumer<IntVector> {

    public IntegerJsonConsumer(IntVector vector) {
        super(vector);
    }

    @Override
    public void consume(JsonParser parser) throws IOException {
        vector.set(currentIndex++, parser.getIntValue());
    }
}
