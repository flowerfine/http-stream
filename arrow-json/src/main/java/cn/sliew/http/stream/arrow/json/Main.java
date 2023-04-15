package cn.sliew.http.stream.arrow.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.VectorSchemaRoot;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    public static void main(String[] args) throws Exception {
        InputStream inputStream1 = Main.class.getClassLoader().getResourceAsStream("user.json");
        InputStream inputStream2 = Main.class.getClassLoader().getResourceAsStream("data.json");

        URL sink = Main.class.getClassLoader().getResource("sink.json");
        final Path path = Paths.get(sink.toURI());
        final OutputStream outputStream = Files.newOutputStream(path);

        ObjectMapper mapper = new ObjectMapper();
        final JsonNode jsonNode = mapper.readTree(inputStream1);
        final JsonToArrowConfig jsonToArrowConfig = new JsonToArrowConfigBuilder(new RootAllocator()).build();

        final JsonToArrowVectorIterator iterator = JsonToArrow.jsonToArrowIterator(jsonNode.toPrettyString(), inputStream2, jsonToArrowConfig);
        final JsonConsumer jsonConsumer = new JsonConsumer(mapper, outputStream);
        while (iterator.hasNext()) {
            final VectorSchemaRoot next = iterator.next();
            jsonConsumer.accept(next);
        }
    }
}
