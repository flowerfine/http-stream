package cn.sliew.http.stream.flink.github.commit;

import cn.sliew.http.stream.flink.util.CheckpointedPosition;
import org.apache.flink.core.memory.DataInputView;
import org.apache.flink.core.memory.DataOutputView;

import java.io.IOException;
import java.util.Date;

public class CommitCheckpointedPosition implements CheckpointedPosition {

    private String sha;
    private String path;
    private String author;
    private Date since;
    private Date until;
    private Integer perPage = 10;
    private Integer page = 1;

    @Override
    public void write(DataOutputView out) throws IOException {

    }

    @Override
    public void read(DataInputView in) throws IOException {

    }
}
