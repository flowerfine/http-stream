package cn.sliew.http.stream.flink.github.commit;

import org.apache.flink.runtime.rest.messages.MessageParameters;
import org.apache.flink.runtime.rest.messages.MessagePathParameter;
import org.apache.flink.runtime.rest.messages.MessageQueryParameter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class CommitMessageParameters extends MessageParameters {

    public final OwnerPathParameter owner = new OwnerPathParameter();
    public final RepoPathParameter repo = new RepoPathParameter();

    public final ShaQueryParameter sha = new ShaQueryParameter();
    public final PathQueryParameter path = new PathQueryParameter();
    public final AuthorQueryParameter author = new AuthorQueryParameter();
    public final SinceQueryParameter since = new SinceQueryParameter();
    public final UntilQueryParameter until = new UntilQueryParameter();
    public final PerPageQueryParameter perPage = new PerPageQueryParameter();
    public final PageQueryParameter page = new PageQueryParameter();

    @Override
    public Collection<MessagePathParameter<?>> getPathParameters() {
        return Collections.unmodifiableCollection(Arrays.asList(owner, repo));
    }

    @Override
    public Collection<MessageQueryParameter<?>> getQueryParameters() {
        return Collections.unmodifiableCollection(Arrays.asList(sha, path, author, since, until, perPage, page));
    }
}
