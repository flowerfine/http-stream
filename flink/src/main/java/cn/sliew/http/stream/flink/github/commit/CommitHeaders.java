package cn.sliew.http.stream.flink.github.commit;

import org.apache.flink.runtime.rest.HttpMethodWrapper;
import org.apache.flink.runtime.rest.messages.EmptyRequestBody;
import org.apache.flink.runtime.rest.messages.MessageHeaders;
import org.apache.flink.shaded.netty4.io.netty.handler.codec.http.HttpResponseStatus;

/**
 * fixme 这玩意在发送的时候会自动添加版本号 v1 到 url 路径中
 */
public class CommitHeaders
        implements MessageHeaders<EmptyRequestBody, CommitResponseBody, CommitMessageParameters> {

    private static final CommitHeaders INSTANCE = new CommitHeaders();

    public static final String URL = "/repos/:" + OwnerPathParameter.KEY + "/:" + RepoPathParameter.KEY + "/commits";

    public static CommitHeaders getInstance() {
        return INSTANCE;
    }

    @Override
    public Class<EmptyRequestBody> getRequestClass() {
        return EmptyRequestBody.class;
    }

    @Override
    public Class<CommitResponseBody> getResponseClass() {
        return CommitResponseBody.class;
    }

    @Override
    public CommitMessageParameters getUnresolvedMessageParameters() {
        return new CommitMessageParameters();
    }

    @Override
    public HttpResponseStatus getResponseStatusCode() {
        return HttpResponseStatus.OK;
    }

    @Override
    public HttpMethodWrapper getHttpMethod() {
        return HttpMethodWrapper.GET;
    }

    @Override
    public String getTargetRestEndpointURL() {
        return URL;
    }

    @Override
    public String getDescription() {
        return "list commits";
    }
}
