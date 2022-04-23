package cn.sliew.http.stream.flink;

import cn.sliew.http.stream.flink.util.CheckpointedPosition;
import org.apache.flink.api.connector.source.SourceSplit;

import javax.annotation.Nullable;
import java.io.Serializable;

import static org.apache.flink.util.Preconditions.checkNotNull;

public class HttpSourceSplit implements SourceSplit, Serializable {

    private static final long serialVersionUID = 6348086794567295161L;

    private final String id;

    private CheckpointedPosition position;

    /**
     * The splits are frequently serialized into checkpoints. Caching the byte representation makes
     * repeated serialization cheap. This field is used by {@link HttpSourceSplitSerializer}.
     */
    @Nullable
    transient byte[] serializedFormCache;

    public HttpSourceSplit(String id, CheckpointedPosition position) {
        this(id, position, null);
    }

    public HttpSourceSplit(String id, CheckpointedPosition position, @Nullable byte[] serializedFormCache) {
        this.id = id;
        this.position = checkNotNull(position);
        this.serializedFormCache = serializedFormCache;
    }

    @Override
    public String splitId() {
        return id;
    }

    public CheckpointedPosition getPosition() {
        return position;
    }

    public HttpSourceSplit updateWithCheckpointedPosition(CheckpointedPosition position) {
        return new HttpSourceSplit(id, position);
    }
}
