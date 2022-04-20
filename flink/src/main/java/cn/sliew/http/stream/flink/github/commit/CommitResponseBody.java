package cn.sliew.http.stream.flink.github.commit;

import org.apache.flink.runtime.rest.messages.ResponseBody;
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class CommitResponseBody implements ResponseBody {

    @JsonProperty("sha")
    private String sha;

    @JsonProperty("nodeId")
    private String node_id;

    @JsonProperty("url")
    private String url;

    @JsonProperty("htmlUrl")
    private String html_url;

    @JsonProperty("commentsUrl")
    private String comments_url;

    private Map commit;

    private Map author;

    private Map committer;

    private List parents;
}
