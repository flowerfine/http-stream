package cn.sliew.http.stream.flink.github.commit;

import org.apache.flink.runtime.rest.messages.ConversionException;
import org.apache.flink.runtime.rest.messages.MessageQueryParameter;

public class PathQueryParameter extends MessageQueryParameter<String> {

    public static final String KEY = "path";

    public PathQueryParameter() {
        super(KEY, MessageParameterRequisiteness.OPTIONAL);
    }

    @Override
    public String convertStringToValue(String s) throws ConversionException {
        return s;
    }

    @Override
    public String convertValueToString(String s) {
        return s;
    }

    @Override
    public String getDescription() {
        return "Only commits containing this file path will be returned.";
    }
}
