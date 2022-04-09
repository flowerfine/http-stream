package cn.sliew.http.stream.akka.jst.order;

import akka.NotUsed;
import akka.japi.Pair;
import akka.stream.javadsl.Source;
import cn.sliew.http.stream.akka.jst.FetchResult;
import cn.sliew.http.stream.akka.jst.JstSubTask;
import cn.sliew.http.stream.akka.util.BeanUtil;
import cn.sliew.http.stream.akka.util.JacksonUtil;
import cn.sliew.http.stream.dao.entity.jst.JstOrder;
import cn.sliew.http.stream.dao.mapper.jst.JstOrderMapper;
import cn.sliew.http.stream.remote.jst.JstRemoteService;
import cn.sliew.http.stream.remote.jst.client.order.JstOrdersResult;
import cn.sliew.http.stream.remote.jst.client.order.OrdersSingleDO;
import cn.sliew.http.stream.remote.jst.client.order.OrdersSingleQuery;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Slf4j
class OrderSubTask extends JstSubTask<OrderJobContext, OrdersSingleQuery, JstOrdersResult> {

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private final Date startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private final Date endTime;

    private final JstRemoteService jstRemoteService;
    private final JstOrderMapper jstOrderMapper;

    public OrderSubTask(Long identifier, Date startTime, Date endTime, JstRemoteService jstRemoteService, JstOrderMapper jstOrderMapper) {
        super(identifier);
        this.startTime = startTime;
        this.endTime = endTime;
        this.jstRemoteService = jstRemoteService;
        this.jstOrderMapper = jstOrderMapper;
    }

    @Override
    protected Source<FetchResult<OrdersSingleQuery, JstOrdersResult>, NotUsed> fetch(OrderJobContext context) {
        OrdersSingleQuery query = getFirstPageQuery();
        return Source.unfoldAsync(query, key -> {
            if (key == null) {
                return CompletableFuture.completedFuture(Optional.empty());
            }
            JstOrdersResult jstOrdersResult = queryJst(context, key);
            FetchResult<OrdersSingleQuery, JstOrdersResult> result = new FetchResult<>(query, jstOrdersResult);
            if (jstOrdersResult.getHasNext()) {
                OrdersSingleQuery next = BeanUtil.copy(key, new OrdersSingleQuery());
                next.setPageIndex(key.getPageIndex() + 1);
                return CompletableFuture.completedFuture(Optional.of(Pair.create(next, result)));
            }
            return CompletableFuture.completedFuture(Optional.of(Pair.create(null, result)));
        });
    }

    @Override
    protected void persist(OrderJobContext context, FetchResult<OrdersSingleQuery, JstOrdersResult> data) {
        persistData(context, data.getQuery(), data.getResult());
    }

    @Override
    protected JstOrdersResult queryJst(OrderJobContext context, OrdersSingleQuery query) {
        JstOrdersResult jstResult = jstRemoteService.ordersSingleQuery(query);
        if (jstResult.isSuccess()) {
            if (log.isDebugEnabled()) {
                log.debug("请求聚水潭接口返回结果 method: {}, query: {}, page_index: {}, page_size: {}, data_count: {}, page_count: {}",
                        context.getJobName(), JacksonUtil.toJsonString(query), jstResult.getPageIndex(), jstResult.getPageSize(),
                        jstResult.getDataCount(), jstResult.getPageCount());
            }
            return jstResult;
        }
        log.error("请求聚水潭接口失败! method: {}, code: {}, msg: {}, query: {}",
                context.getJobName(), jstResult.getCode(), jstResult.getMsg(), JacksonUtil.toJsonString(query));
        throw new RuntimeException(jstResult.getMsg());
    }

    @Override
    protected void persistData(OrderJobContext context, OrdersSingleQuery query, JstOrdersResult result) {
        if (CollectionUtils.isEmpty(result.getDatas())) {
            return;
        }
        result.getDatas().forEach(data -> upsert(query, data));
    }

    private void upsert(OrdersSingleQuery query, OrdersSingleDO data) {
        JstOrder jstOrder = jstOrderMapper.selectByOId(data.getOId());
        if (jstOrder != null) {
            BeanUtil.copy(data, jstOrder);
            jstOrder.setItems(JacksonUtil.toJsonString(data.getItems()));
            BeanUtil.copy(query, jstOrder);
            jstOrder.setModifiedBegin(query.getStartTime());
            jstOrder.setModifiedEnd(query.getEndTime());
            jstOrder.setModifier("sync-task");
            jstOrderMapper.updateByPrimaryKeySelective(jstOrder);
        } else {
            JstOrder record = BeanUtil.copy(data, new JstOrder());
            record.setItems(JacksonUtil.toJsonString(data.getItems()));
            BeanUtil.copy(query, record);
            record.setModifiedBegin(query.getStartTime());
            record.setModifiedEnd(query.getEndTime());
            record.setCreator("sync-task");
            record.setModifier("sync-task");
            jstOrderMapper.insertSelective(record);
        }
    }

    private OrdersSingleQuery getFirstPageQuery() {
        OrdersSingleQuery query = new OrdersSingleQuery();
        query.setStartTime(startTime);
        query.setEndTime(endTime);
        query.setPageIndex(1);
        query.setPageSize(50);
        return query;
    }
}
