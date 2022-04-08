package cn.sliew.http.stream.akka.jst;

import lombok.Getter;

@Getter
public class FetchResult<Query, Result> {

    private final Query query;
    private final Result result;

    public FetchResult(Query query, Result result) {
        this.query = query;
        this.result = result;
    }
}
