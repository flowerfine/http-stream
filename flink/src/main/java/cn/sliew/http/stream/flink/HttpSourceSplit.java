package cn.sliew.http.stream.flink;

import cn.sliew.http.stream.flink.util.CheckpointedPosition;
import org.apache.flink.api.connector.source.SourceSplit;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Date;
import java.util.Optional;

public class HttpSourceSplit implements SourceSplit, Serializable {

    private static final long serialVersionUID = 6348086794567295161L;

    private final String id;
    private final Date startTime;
    private final Date endTime;

    private CheckpointedPosition position;

    /**
     * The splits are frequently serialized into checkpoints. Caching the byte representation makes
     * repeated serialization cheap. This field is used by {@link HttpSourceSplitSerializer}.
     */
    @Nullable
    transient byte[] serializedFormCache;

    public HttpSourceSplit(String id, Date startTime, Date endTime) {
        this(id, startTime, endTime, null);
    }

    public HttpSourceSplit(String id, Date startTime, Date endTime, @Nullable CheckpointedPosition position) {
        this(id, startTime, endTime, position, null);
    }

    public HttpSourceSplit(String id, Date startTime, Date endTime, CheckpointedPosition position, @Nullable byte[] serializedFormCache) {
        this.id = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.position = position;
        this.serializedFormCache = serializedFormCache;
    }

    @Override
    public String splitId() {
        return id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Optional<CheckpointedPosition> getPosition() {
        return Optional.ofNullable(position);
    }
}
