package cn.sliew.http.stream.arrow.json.consumer;

import com.fasterxml.jackson.core.JsonParser;
import org.apache.arrow.util.AutoCloseables;
import org.apache.arrow.vector.VectorSchemaRoot;

import java.io.IOException;
import java.util.List;

/**
 * Composite consumer which hold all consumers.
 * It manages the consume and cleanup process.
 */
public class CompositeJsonConsumer implements AutoCloseable {

    private final List<Consumer> consumers;

    public List<Consumer> getConsumers() {
        return consumers;
    }

    public CompositeJsonConsumer(List<Consumer> consumers) {
        this.consumers = consumers;
    }

    /**
     * Consume decoder data.
     */
    public void consume(JsonParser parser) throws IOException {
        for (Consumer consumer : consumers) {
            consumer.consume(parser);
        }
    }

    /**
     * Reset vector of consumers with the given {@link VectorSchemaRoot}.
     */
    public void resetConsumerVectors(VectorSchemaRoot root) {
        int index = 0;
        for (Consumer consumer : consumers) {
            if (consumer.resetValueVector(root.getFieldVectors().get(index))) {
                index++;
            }
        }
    }

    @Override
    public void close() {
        try {
            AutoCloseables.close(consumers);
        } catch (Exception e) {
            throw new RuntimeException("Error occurs in close.", e);
        }
    }
}