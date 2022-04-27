package cn.sliew.http.stream.arrow.json.consumer;

import cn.sliew.http.stream.arrow.json.consumer.BaseJsonConsumer;
import com.fasterxml.jackson.core.JsonParser;
import org.apache.arrow.vector.VarCharVector;
import org.apache.arrow.vector.util.Text;

import java.io.IOException;

public class StringJsonConsumer extends BaseJsonConsumer<VarCharVector> {

    public StringJsonConsumer(VarCharVector vector) {
        super(vector);
    }

    @Override
    public void consume(JsonParser parser) throws IOException {
        vector.set(currentIndex, new Text(parser.getText()));
        currentIndex++;
    }
}
