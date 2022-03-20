package cn.sliew.http.stream.akka.framework.jst.order;

import cn.sliew.http.stream.akka.framework.AbstractJobProcessor;
import cn.sliew.http.stream.akka.framework.JobContext;
import cn.sliew.http.stream.akka.framework.ProcessResult;
import cn.sliew.http.stream.akka.framework.jst.JstRootTask;
import cn.sliew.http.stream.akka.framework.jst.JstSubTask;
import cn.sliew.http.stream.akka.util.BeanUtil;
import cn.sliew.http.stream.akka.util.JacksonUtil;
import cn.sliew.http.stream.dao.entity.job.JobSyncOffset;
import cn.sliew.http.stream.dao.entity.jst.JstOrder;
import cn.sliew.http.stream.dao.mapper.jst.JstOrderMapper;
import cn.sliew.http.stream.remote.jst.JstRemoteService;
import cn.sliew.http.stream.remote.jst.client.JstResult;
import cn.sliew.http.stream.remote.jst.client.order.OrdersSingleDO;
import cn.sliew.http.stream.remote.jst.client.order.OrdersSingleQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class OrderJobProcessor extends AbstractJobProcessor<JstRootTask, JstSubTask, JobSyncOffset> {

    private final JstRemoteService jstRemoteService;
    private final JstOrderMapper jstOrderMapper;

    public OrderJobProcessor(JstRemoteService jstRemoteService, JstOrderMapper jstOrderMapper) {
        this.jstRemoteService = jstRemoteService;
        this.jstOrderMapper = jstOrderMapper;
    }

    @Override
    public CompletableFuture<ProcessResult> process(JobContext<JobSyncOffset, JstRootTask, JstSubTask> context, JstSubTask subTask) {
        OrdersSingleQuery query = convert(subTask);
        Integer pageIndex = 1;
        Integer pageSize = 50;
        while (true) {
            query.setPageIndex(pageIndex);
            query.setPageSize(pageSize);
            JstResult<OrdersSingleDO> result = queryJst(context, query);
            persistData(query, result);
            if (result.getHasNext() == false) {
                break;
            }
            pageIndex += 1;
        }

        return CompletableFuture.completedFuture(ProcessResult.success(subTask));
    }

    private JstResult<OrdersSingleDO> queryJst(JobContext<JobSyncOffset, JstRootTask, JstSubTask> context, OrdersSingleQuery query) {
        JstResult<OrdersSingleDO> jstResult = jstRemoteService.ordersSingleQuery(query);
        if (jstResult.isSuccess()) {
            if (log.isTraceEnabled()) {
                log.debug("请求聚水潭接口返回结果 method: {}, query: {}, page_index: {}, page_size: {}, data_count: {}, page_count: {}",
                        context.getJobName(), JacksonUtil.toJsonString(query), jstResult.getPageIndex(), jstResult.getPageSize(), jstResult.getDataCount(), jstResult.getPageCount());
            }
            return jstResult;
        }
        throw new RuntimeException(jstResult.getMsg());
    }

    private void persistData(OrdersSingleQuery query, JstResult<OrdersSingleDO> result) {
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

    private OrdersSingleQuery convert(JstSubTask subTask) {
        OrdersSingleQuery query = new OrdersSingleQuery();
        query.setStartTime(subTask.getStartTime());
        query.setEndTime(subTask.getEndTime());
        return query;
    }
}
