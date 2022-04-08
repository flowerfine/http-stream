package cn.sliew.http.stream.akka.page;

import cn.sliew.http.stream.akka.util.BeanUtil;
import cn.sliew.http.stream.remote.jst.client.JstResult;
import cn.sliew.http.stream.remote.jst.client.order.OrdersSingleDO;
import cn.sliew.http.stream.remote.jst.client.order.OrdersSingleQuery;

import java.util.Iterator;
import java.util.Optional;

public class JstOrderPage implements Page<OrdersSingleQuery, OrdersSingleDO> {

    private final OrdersSingleQuery query;
    private final JstResult<OrdersSingleDO> jstResult;

    public JstOrderPage(OrdersSingleQuery query, JstResult<OrdersSingleDO> jstResult) {
        this.query = query;
        this.jstResult = jstResult;
    }

    @Override
    public Optional<OrdersSingleQuery> getNextPage() {
        if (jstResult.getHasNext()) {
            OrdersSingleQuery next = BeanUtil.copy(query, new OrdersSingleQuery());
            next.setPageIndex(query.getPageIndex() + 1);
            return Optional.of(next);
        }
        return Optional.empty();
    }

    @Override
    public Iterator<OrdersSingleDO> iterator() {
        return jstResult.getDatas().iterator();
    }
}
