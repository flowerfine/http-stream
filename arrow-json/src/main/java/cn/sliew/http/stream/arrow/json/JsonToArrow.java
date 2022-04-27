package cn.sliew.http.stream.arrow.json;

import com.fasterxml.jackson.core.JsonParser;
import org.apache.arrow.util.Preconditions;

import java.io.IOException;

public enum JsonToArrow {
    ;

    public static JsonToArrowVectorIterator jsonToArrowIterator(
            String originalJsonData,
            JsonParser parser,
            JsonToArrowConfig config) throws IOException {

        Preconditions.checkNotNull(originalJsonData, "original json data can not be null, which is used for inferring json schema");
        Preconditions.checkNotNull(parser, "json parser object can not be null");
        Preconditions.checkNotNull(config, "config can not be null");
        return new JsonToArrowVectorIterator(originalJsonData, parser, config);
    }
}
