package cn.sliew.http.stream.arrow.json.consumer;

import com.fasterxml.jackson.core.JsonParser;
import org.apache.arrow.vector.complex.StructVector;

import java.io.IOException;

public class ObjectJsonConsumer extends BaseJsonConsumer<StructVector>{

    private final Consumer[] delegates;

    public ObjectJsonConsumer(StructVector vector, Consumer[] delegates) {
        super(vector);
        this.delegates = delegates;
    }

    @Override
    public void consume(JsonParser parser) throws IOException {
        for (int i = 0; i < delegates.length; i++) {
            delegates[i].consume(parser);
        }
        vector.setIndexDefined(currentIndex);
        currentIndex++;
    }
}
