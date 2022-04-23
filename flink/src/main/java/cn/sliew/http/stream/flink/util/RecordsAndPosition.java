package cn.sliew.http.stream.flink.util;

import lombok.Getter;

import java.util.Collection;

@Getter
public class RecordsAndPosition<E> {

    private final Collection<E> records;
    private final CheckpointedPosition position;

    public RecordsAndPosition(Collection<E> records, CheckpointedPosition position) {
        this.records = records;
        this.position = position;
    }
}
