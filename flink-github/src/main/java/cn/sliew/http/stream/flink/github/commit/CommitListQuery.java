package cn.sliew.http.stream.flink.github.commit;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CommitListQuery {

    /**
     * SHA or branch to start listing commits from.
     * Default: the repository’s default branch (usually master).
     */
    @JsonProperty("sha")
    private String sha;

    /**
     * Only commits containing this file path will be returned.
     */
    @JsonProperty("path")
    private String path;

    /**
     * GitHub login or email address by which to filter by commit author.
     */
    @JsonProperty("author")
    private String author;

    /**
     * Only show notifications updated after the given time.
     * This is a timestamp in ISO 8601 format: YYYY-MM-DDTHH:MM:SSZ.
     */
    @JsonProperty("since")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:MM:ssz", timezone = "GMT+8")
    private Date since;

    /**
     * Only commits before this date will be returned.
     * This is a timestamp in ISO 8601 format: YYYY-MM-DDTHH:MM:SSZ.
     */
    @JsonProperty("until")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:MM:ssz", timezone = "GMT+8")
    private Date until;

    /**
     * Results per page (max 100)
     */
    @JsonProperty("per_page")
    private Integer perPage = 10;

    /**
     * Page number of the results to fetch.
     */
    @JsonProperty("page")
    private Integer page = 1;
}
