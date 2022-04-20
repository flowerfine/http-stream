package cn.sliew.http.stream.flink.github.commit;

import org.apache.flink.runtime.rest.messages.MessageParameters;
import org.apache.flink.runtime.rest.messages.MessagePathParameter;
import org.apache.flink.runtime.rest.messages.MessageQueryParameter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class CommitMessageParameters extends MessageParameters {

    private final OwnerPathParameter owner = new OwnerPathParameter();
    private final RepoPathParameter repo = new RepoPathParameter();

    @Override
    public Collection<MessagePathParameter<?>> getPathParameters() {
        return Collections.unmodifiableCollection(Arrays.asList(owner, repo));
    }

    @Override
    public Collection<MessageQueryParameter<?>> getQueryParameters() {
        return Collections.emptyList();
    }
}
