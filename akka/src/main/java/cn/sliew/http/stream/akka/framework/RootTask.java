package cn.sliew.http.stream.akka.framework;

import java.util.List;

public interface RootTask<Context extends JobContext, Sub extends SubTask> {

    List<Sub> split(Context context);
}


