package cn.sliew.http.stream.remote.jst.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageQuery {

    /**
     * 第几页，从1 开始
     */
    @JsonProperty("page_index")
    private Integer pageIndex = 1;

    /**
     * 默认 30，最大不超过 50
     */
    @JsonProperty("page_size")
    private Integer pageSize = 50;
}
