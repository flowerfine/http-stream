package cn.sliew.http.stream.akka.framework;

public interface JobContext<SyncOffset, Root extends RootTask, Sub extends SubTask> {

    String getJobName();

    SyncOffset getSyncOffset(Root rootTask);

    void updateSyncOffset(Sub subTask);

}
