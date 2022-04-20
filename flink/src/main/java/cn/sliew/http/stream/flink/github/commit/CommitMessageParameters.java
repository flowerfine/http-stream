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

    private final ShaQueryParameter sha = new ShaQueryParameter();
    private final PathQueryParameter path = new PathQueryParameter();
    private final AuthorQueryParameter author = new AuthorQueryParameter();
    private final SinceQueryParameter since = new SinceQueryParameter();
    private final UntilQueryParameter until = new UntilQueryParameter();
    private final PerPageQueryParameter perPage = new PerPageQueryParameter();
    private final PageQueryParameter page = new PageQueryParameter();

    @Override
    public Collection<MessagePathParameter<?>> getPathParameters() {
        return Collections.unmodifiableCollection(Arrays.asList(owner, repo));
    }

    @Override
    public Collection<MessageQueryParameter<?>> getQueryParameters() {
        return Collections.unmodifiableCollection(Arrays.asList(sha, path, author, since, until, perPage, page));
    }
}
