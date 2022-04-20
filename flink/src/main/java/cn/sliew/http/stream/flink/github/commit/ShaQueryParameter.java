package cn.sliew.http.stream.flink.github.commit;

import org.apache.flink.runtime.rest.messages.ConversionException;
import org.apache.flink.runtime.rest.messages.MessageQueryParameter;

public class ShaQueryParameter extends MessageQueryParameter<String> {

    public static final String KEY = "repo";

    public ShaQueryParameter() {
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
        return "SHA or branch to start listing commits from. Default: the repository’s default branch (usually master).";
    }
}
