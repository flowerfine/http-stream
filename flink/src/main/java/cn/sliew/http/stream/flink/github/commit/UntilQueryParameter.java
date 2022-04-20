package cn.sliew.http.stream.flink.github.commit;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.apache.flink.runtime.rest.messages.ConversionException;
import org.apache.flink.runtime.rest.messages.MessageQueryParameter;

import java.text.ParseException;
import java.util.Date;

public class UntilQueryParameter extends MessageQueryParameter<Date> {

    public static final String KEY = "until";

    public UntilQueryParameter() {
        super(KEY, MessageParameterRequisiteness.OPTIONAL);
    }

    @Override
    public Date convertStringToValue(String s) throws ConversionException {
        try {
            return DateUtils.parseDate(s, "YYYY-MM-DDTHH:MM:SSZ");
        } catch (ParseException e) {
            throw new ConversionException(s + " is invalid for format YYYY-MM-DDTHH:MM:SSZ");
        }
    }

    @Override
    public String convertValueToString(Date date) {
        return DateFormatUtils.format(date, "YYYY-MM-DDTHH:MM:SSZ");
    }

    @Override
    public String getDescription() {
        return "Only commits before this date will be returned. This is a timestamp in ISO 8601 format: YYYY-MM-DDTHH:MM:SSZ.";
    }
}
