package cn.sliew.http.stream.arrow.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.VectorSchemaRoot;

import java.io.IOException;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) throws IOException {
        InputStream inputStream1 = Main.class.getClassLoader().getResourceAsStream("user.json");
        InputStream inputStream2 = Main.class.getClassLoader().getResourceAsStream("data.json");

        ObjectMapper mapper = new ObjectMapper();
        final JsonNode jsonNode = mapper.readTree(inputStream1);
        final JsonToArrowConfig jsonToArrowConfig = new JsonToArrowConfigBuilder(new RootAllocator()).build();

        final JsonToArrowVectorIterator iterator = JsonToArrow.jsonToArrowIterator(jsonNode.toPrettyString(), inputStream2, jsonToArrowConfig);
        while (iterator.hasNext()) {
            final VectorSchemaRoot next = iterator.next();
            System.out.println(next);
        }
    }
}
