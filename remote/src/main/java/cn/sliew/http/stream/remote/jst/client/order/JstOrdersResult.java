package cn.sliew.http.stream.remote.jst.client.order;

import cn.sliew.http.stream.remote.jst.client.JstResult;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class JstOrdersResult extends JstResult<OrdersSingleDO> {

    @JsonIgnoreProperties(allowSetters = true)
    private List<OrdersSingleDO> orders;

    @Override
    public List<OrdersSingleDO> getDatas() {
        return orders;
    }
}