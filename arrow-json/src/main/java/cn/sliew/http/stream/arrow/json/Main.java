package cn.sliew.http.stream.arrow.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.ipc.ArrowFileWriter;
import org.apache.arrow.vector.ipc.SeekableReadChannel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {

    public static void main(String[] args) throws IOException {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("user.json");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader reader = objectMapper.reader();
        JsonParser parser = reader.createParser(inputStream);
        final JsonToArrowConfig jsonToArrowConfig = new JsonToArrowConfigBuilder(new RootAllocator()).build();
        final JsonToArrowVectorIterator2 iterator = new JsonToArrowVectorIterator2(parser, jsonToArrowConfig);

        while (iterator.hasNext()) {
            VectorSchemaRoot vectorSchemaRoot = iterator.next();
            System.out.println(vectorSchemaRoot.getRowCount());
        }

    }
}
