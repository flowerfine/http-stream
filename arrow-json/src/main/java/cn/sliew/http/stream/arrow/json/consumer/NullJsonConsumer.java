package cn.sliew.http.stream.arrow.json.consumer;

import com.fasterxml.jackson.core.JsonParser;
import org.apache.arrow.vector.NullVector;

import java.io.IOException;

public class NullJsonConsumer extends BaseJsonConsumer<NullVector>{

    public NullJsonConsumer(NullVector vector) {
        super(vector);
    }

    @Override
    public void consume(JsonParser parser) throws IOException {
        currentIndex++;
    }
}
