package cn.sliew.http.stream.flink.util;

import org.apache.flink.core.io.IOReadableWritable;
import org.apache.flink.core.memory.DataInputView;
import org.apache.flink.core.memory.DataOutputView;

import java.io.IOException;
import java.io.Serializable;

public class HttpSourceParameters implements IOReadableWritable, Serializable {

    public Object[] getPathParameters() {
        return null;
    }
    public Object[] getQueryParameters() {
        return null;
    }
    public Object[] getBodyParameters() {
        return null;
    }
    public Object[] getHeaderParameters() {
        return null;
    }

    @Override
    public void write(DataOutputView out) throws IOException {

    }

    @Override
    public void read(DataInputView in) throws IOException {

    }
}
