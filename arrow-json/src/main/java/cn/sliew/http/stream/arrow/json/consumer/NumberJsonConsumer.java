package cn.sliew.http.stream.arrow.json.consumer;

import com.fasterxml.jackson.core.JsonParser;
import org.apache.arrow.vector.Float8Vector;

import java.io.IOException;

public class NumberJsonConsumer extends BaseJsonConsumer<Float8Vector> {

    public NumberJsonConsumer(Float8Vector vector) {
        super(vector);
    }

    @Override
    public void consume(JsonParser parser) throws IOException {
        vector.set(currentIndex++, parser.getNumberValue().doubleValue());
    }
}
