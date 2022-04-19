package cn.sliew.http.stream.remote.github.client.commit;

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
