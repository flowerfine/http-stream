package cn.sliew.http.stream.akka.framework.incremental;

import cn.sliew.http.stream.akka.framework.JobProcessor;
import cn.sliew.http.stream.akka.framework.RootTask;
import cn.sliew.http.stream.akka.framework.SubTask;

public interface IncrementalJobProcessor<Context extends IncrementalJobContext, Root extends RootTask, Sub extends SubTask>
        extends JobProcessor<Context, Root, Sub> {

    @Override
    Context getContext();
}
