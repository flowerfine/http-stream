package cn.sliew.http.stream.arrow.json.consumer;

import com.fasterxml.jackson.core.JsonParser;
import org.apache.arrow.vector.BitVector;

import java.io.IOException;

public class BooleanJsonConsumer extends BaseJsonConsumer<BitVector> {

    public BooleanJsonConsumer(BitVector vector) {
        super(vector);
    }

    @Override
    public void consume(JsonParser parser) throws IOException {
        vector.set(currentIndex, parser.nextBooleanValue() ? 1 : 0);
        currentIndex++;
    }
}
