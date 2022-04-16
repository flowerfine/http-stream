package cn.sliew.http.stream.flink;

import org.apache.flink.api.connector.source.SourceSplit;

import java.io.Serializable;

public abstract class HttpSourceSplit implements SourceSplit, Serializable {

    private static final long serialVersionUID = 6348086794567295161L;

    private final String id;

    public HttpSourceSplit(String id) {
        this.id = id;
    }

    @Override
    public String splitId() {
        return id;
    }
}
