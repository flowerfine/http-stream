package cn.sliew.http.stream.flink.github.commit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class CommitListDO {

    @JsonProperty("sha")
    private String sha;

    @JsonProperty("node_id")
    private String nodeId;

    @JsonProperty("url")
    private String url;

    @JsonProperty("html_url")
    private String htmlUrl;

    @JsonProperty("comments_url")
    private String commentsUrl;

    private Map commit;

    private Map author;

    private Map committer;

    private List parents;
}
