package cn.sliew.http.stream.flink.github.commit;

import org.apache.flink.runtime.rest.messages.ConversionException;
import org.apache.flink.runtime.rest.messages.MessageQueryParameter;

public class PageQueryParameter extends MessageQueryParameter<Integer> {

    public static final String KEY = "page";

    public PageQueryParameter() {
        super(KEY, MessageParameterRequisiteness.OPTIONAL);
    }

    @Override
    public Integer convertStringToValue(String s) throws ConversionException {
        return Integer.parseInt(s);
    }

    @Override
    public String convertValueToString(Integer integer) {
        return integer.toString();
    }

    @Override
    public String getDescription() {
        return "Page number of the results to fetch. default: 1.";
    }
}
