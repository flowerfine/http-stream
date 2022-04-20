package cn.sliew.http.stream.flink.github.commit;

import org.apache.flink.runtime.rest.messages.ConversionException;
import org.apache.flink.runtime.rest.messages.MessageQueryParameter;

public class AuthorQueryParameter extends MessageQueryParameter<String> {

    public static final String KEY = "author";

    public AuthorQueryParameter() {
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
        return "GitHub login or email address by which to filter by commit author.";
    }
}
