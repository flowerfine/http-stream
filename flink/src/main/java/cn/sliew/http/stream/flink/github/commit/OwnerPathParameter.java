package cn.sliew.http.stream.flink.github.commit;

import org.apache.flink.runtime.rest.messages.ConversionException;
import org.apache.flink.runtime.rest.messages.MessagePathParameter;

public class OwnerPathParameter extends MessagePathParameter<String> {

    public static final String KEY = "owner";

    public OwnerPathParameter() {
        super(KEY);
    }

    @Override
    protected String convertFromString(String s) throws ConversionException {
        return s;
    }

    @Override
    protected String convertToString(String s) {
        return s;
    }

    @Override
    public String getDescription() {
        return "repository owner";
    }
}
