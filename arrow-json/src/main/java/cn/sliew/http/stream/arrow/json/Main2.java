package cn.sliew.http.stream.arrow.json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.arrow.vector.types.pojo.Schema;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;

public class Main2 {

    public static void main(String[] args) throws IOException {
        InputStream inputStream = Main2.class.getClassLoader().getResourceAsStream("user.json");

        ObjectMapper mapper = new ObjectMapper();
        final JsonNode jsonNode = mapper.readTree(inputStream);
        final Schema schema = JsonToArrowUtils.inferSchema(jsonNode);
        System.out.println(schema.toJson());

    }
}
