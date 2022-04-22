package cn.sliew.http.stream.flink.reader;

import cn.sliew.http.stream.flink.HttpSourceSplit;
import cn.sliew.http.stream.flink.util.CheckpointedPosition;
import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.api.java.typeutils.ResultTypeQueryable;
import org.apache.flink.configuration.Configuration;

import javax.annotation.Nullable;
import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;

public interface StreamFormat<T, SplitT extends HttpSourceSplit>
        extends Serializable, ResultTypeQueryable<T> {

    Reader<T> createReader(Configuration config, SplitT splitT) throws IOException;

    Reader<T> restoreReader(Configuration config, SplitT splitT) throws IOException;

    boolean isSplittable();

    @Override
    TypeInformation<T> getProducedType();

    // ------------------------------------------------------------------------

    /**
     * The actual reader that reads the records.
     */
    @PublicEvolving
    interface Reader<T> extends Closeable {

        @Nullable
        T read() throws IOException;
        
        @Nullable
        default CheckpointedPosition getCheckpointedPosition() {
            return null;
        }
    }
}
