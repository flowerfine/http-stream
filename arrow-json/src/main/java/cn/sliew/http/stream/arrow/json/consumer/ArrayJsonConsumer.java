package cn.sliew.http.stream.arrow.json.consumer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.apache.arrow.util.Preconditions;
import org.apache.arrow.vector.complex.ListVector;

import java.io.IOException;

public class ArrayJsonConsumer extends BaseJsonConsumer<ListVector> {

    private final Consumer delegate;

    public ArrayJsonConsumer(ListVector vector, Consumer delegate) {
        super(vector);
        this.delegate = delegate;
    }

    @Override
    public void consume(JsonParser parser) throws IOException {
        Preconditions.checkState(parser.nextToken() == JsonToken.START_ARRAY);
        vector.startNewValue(currentIndex);
        long totalCount = 0;
        while (parser.nextToken() != JsonToken.END_ARRAY) {
            delegate.consume(parser);
            totalCount++;
        }
        vector.endValue(currentIndex, (int) totalCount);
        currentIndex++;
    }
}
