package cn.sliew.http.stream.remote.jst.client;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonAutoDetect(setterVisibility = JsonAutoDetect.Visibility.NONE,
        creatorVisibility = JsonAutoDetect.Visibility.NONE,
        fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class JstResult<T> {

    @JsonProperty("page_index")
    private Integer pageIndex;

    @JsonProperty("page_size")
    private Integer pageSize;

    @JsonProperty("data_count")
    private Integer dataCount;

    @JsonProperty("page_count")
    private Integer pageCount;

    @JsonProperty("has_next")
    private Boolean hasNext;

    @JsonProperty("issuccess")
    private boolean success;

    @JsonProperty("code")
    private Integer code;

    @JsonProperty("msg")
    private String msg;

    @JsonProperty("datas")
    private List<T> datas;
}
