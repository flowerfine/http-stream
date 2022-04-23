package cn.sliew.http.stream.flink.github;

import lombok.Getter;

import java.io.Serializable;

@Getter
public final class NameValuePair implements Serializable {

    private final String name;
    private final String value;

    public NameValuePair(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
