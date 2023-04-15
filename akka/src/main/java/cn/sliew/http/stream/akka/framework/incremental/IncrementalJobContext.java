package cn.sliew.http.stream.akka.framework.incremental;

import cn.sliew.http.stream.akka.framework.JobContext;
import cn.sliew.http.stream.akka.framework.RootTask;
import cn.sliew.http.stream.akka.framework.SubTask;

public interface IncrementalJobContext<SyncOffset, Root extends RootTask, Sub extends SubTask>
        extends JobContext<Root, Sub> {

    SyncOffset getSyncOffset(Root rootTask);

    void updateSyncOffset(Sub subTask);
}
