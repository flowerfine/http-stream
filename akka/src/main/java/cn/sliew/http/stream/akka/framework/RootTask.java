package cn.sliew.http.stream.akka.framework;

import java.util.List;

public interface RootTask<SyncOffset, Task extends SubTask> {

    boolean supportSplit(SyncOffset offset);

    List<Task> split(SyncOffset offset);

}
