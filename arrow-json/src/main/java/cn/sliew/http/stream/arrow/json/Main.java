package cn.sliew.http.stream.arrow.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.ipc.ArrowStreamWriter;
import org.apache.arrow.vector.ipc.JsonFileWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class Main {

    public static void main(String[] args) throws IOException, URISyntaxException {
        InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("user.json");

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader reader = objectMapper.reader();
        JsonParser parser = reader.createParser(inputStream);
        final JsonToArrowConfig jsonToArrowConfig = new JsonToArrowConfigBuilder(new RootAllocator()).build();
        final JsonToArrowVectorIterator2 iterator = new JsonToArrowVectorIterator2(parser, jsonToArrowConfig);


        while (iterator.hasNext()) {
            VectorSchemaRoot vectorSchemaRoot = iterator.next();
            URL data = Main.class.getClassLoader().getResource("data.json");
            System.out.println(data.getPath());
            JsonFileWriter jsonFileWriter = new JsonFileWriter(new File(data.toURI()));
            jsonFileWriter.start(vectorSchemaRoot.getSchema(), jsonToArrowConfig.getProvider());
            jsonFileWriter.write(vectorSchemaRoot);
            jsonFileWriter.close();
            System.out.println("输出转化结果! 数量: " + vectorSchemaRoot.getRowCount());
        }

    }
}
