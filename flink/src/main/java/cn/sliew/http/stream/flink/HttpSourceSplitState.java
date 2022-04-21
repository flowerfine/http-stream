package cn.sliew.http.stream.flink;

import cn.sliew.http.stream.flink.util.CheckpointedPosition;

import java.util.Optional;

public abstract class HttpSourceSplitState<SplitT extends HttpSourceSplit> {

    private final SplitT split;
    private long pageIndex;
    private long pageSize;

    public HttpSourceSplitState(SplitT split) {
        this.split = split;
        final Optional<CheckpointedPosition> position = split.getPosition();
        if (position.isPresent()) {
            this.pageIndex = position.get().getPageIndex();
            this.pageSize = position.get().getPageSize();
        } else {
            this.pageIndex = 1L;
            this.pageSize = 10L;
        }
    }

    public long getPageIndex() {
        return pageIndex;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPosition(long pageIndex, long pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public SplitT toSourceSplit() {
        return split;
    }
}
