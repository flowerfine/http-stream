package cn.sliew.http.stream.flink.github.commit;

import org.apache.flink.runtime.rest.messages.ConversionException;
import org.apache.flink.runtime.rest.messages.MessageQueryParameter;

public class PerPageQueryParameter extends MessageQueryParameter<Integer> {

    public static final String KEY = "per_page";

    public PerPageQueryParameter() {
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
        return "Results per page (max 100). default: 30.";
    }
}
