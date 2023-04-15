package cn.sliew.http.stream.arrow.json;

import org.apache.arrow.util.Preconditions;

import java.io.IOException;
import java.io.InputStream;

public enum JsonToArrow {
    ;

    public static JsonToArrowVectorIterator jsonToArrowIterator(
            String originalJsonData,
            InputStream in,
            JsonToArrowConfig config) throws IOException {

        Preconditions.checkNotNull(originalJsonData, "original json data can not be null, which is used for inferring json schema");
        Preconditions.checkNotNull(in, "json inputstream can not be null");
        Preconditions.checkNotNull(config, "config can not be null");
        return new JsonToArrowVectorIterator(originalJsonData, in, config);
    }
}
