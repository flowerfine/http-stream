package cn.sliew.http.stream.flink.util;

import lombok.Getter;

import java.util.Collection;

@Getter
public class RecordsAndPosition<E> {

    private Collection<E> records;

    private long pageIndex;
    private long pageSize;

    public RecordsAndPosition(Collection<E> records, long pageIndex, long pageSize) {
        this.records = records;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }
}
