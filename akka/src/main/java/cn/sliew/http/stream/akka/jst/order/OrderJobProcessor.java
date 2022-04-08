package cn.sliew.http.stream.akka.jst.order;

import akka.Done;
import akka.NotUsed;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.SpawnProtocol;
import akka.japi.Pair;
import akka.stream.javadsl.Source;
import cn.sliew.http.stream.akka.framework.DefaultJobProcessor;
import cn.sliew.http.stream.akka.framework.JobContext;
import cn.sliew.http.stream.akka.framework.ProcessResult;
import cn.sliew.http.stream.akka.framework.incremental.IncrementalJobProcessor;
import cn.sliew.http.stream.akka.jst.JstRootTask;
import cn.sliew.http.stream.akka.jst.JstSubTask;
import cn.sliew.http.stream.akka.util.BeanUtil;
import cn.sliew.http.stream.akka.util.JacksonUtil;
import cn.sliew.http.stream.dao.entity.job.JobSyncOffset;
import cn.sliew.http.stream.dao.entity.jst.JstOrder;
import cn.sliew.http.stream.dao.mapper.jst.JstOrderMapper;
import cn.sliew.http.stream.remote.jst.JstRemoteService;
import cn.sliew.http.stream.remote.jst.client.JstResult;
import cn.sliew.http.stream.remote.jst.client.order.JstOrdersResult;
import cn.sliew.http.stream.remote.jst.client.order.OrdersSingleDO;
import cn.sliew.http.stream.remote.jst.client.order.OrdersSingleQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Slf4j
public class OrderJobProcessor extends IncrementalJobProcessor<OrderJobContext, JstRootTask, JstSubTask> {

    private final JstRemoteService jstRemoteService;
    private final JstOrderMapper jstOrderMapper;
    private final ActorSystem<SpawnProtocol.Command> actorSystem;

    public OrderJobProcessor(JstRemoteService jstRemoteService, JstOrderMapper jstOrderMapper, ActorSystem<SpawnProtocol.Command> actorSystem) {
        this.jstRemoteService = jstRemoteService;
        this.jstOrderMapper = jstOrderMapper;
        this.actorSystem = actorSystem;
    }



    @Override
    public CompletableFuture<ProcessResult> process(JobContext<JobSyncOffset, JstRootTask, JstSubTask> context, JstSubTask subTask) {
        return doProcess2(context, subTask);
    }

    /**
     * 实现方式一：写一个 Iterable，内部完成数据同步。缺点是数据同步部分是同步阻塞的。
     * 实现方式二：使用 Source.unfoldAsync()，异步处理数据同步。
     */
    private CompletableFuture<ProcessResult> doProcess2(JobContext<JobSyncOffset, JstRootTask, JstSubTask> context, JstSubTask subTask) {
        OrdersSingleQuery query = convert(subTask);
        query.setPageIndex(1);
        query.setPageSize(50);
        Source<Pair<OrdersSingleQuery, JstOrdersResult>, NotUsed> jstResultSource = Source.unfoldAsync(query, key -> {
            if (key == null) {
                return CompletableFuture.completedFuture(Optional.empty());
            }
            JstOrdersResult jstOrdersResult = queryJst(context, key);
            Pair<OrdersSingleQuery, JstOrdersResult> result = Pair.create(key, jstOrdersResult);
            if (jstOrdersResult.getHasNext()) {
                OrdersSingleQuery next = BeanUtil.copy(key, new OrdersSingleQuery());
                next.setPageIndex(key.getPageIndex() + 1);
                return CompletableFuture.completedFuture(Optional.of(Pair.create(next, result)));
            }
            return CompletableFuture.completedFuture(Optional.of(Pair.create(null, result)));
        });


        CompletionStage<Done> completionStage = jstResultSource.runForeach(pair -> persistData(pair.first(), pair.second()), actorSystem);
        return completionStage.thenApply(done -> ProcessResult.success(subTask)).toCompletableFuture();
    }

    private CompletableFuture<ProcessResult> doProcess(JobContext<JobSyncOffset, JstRootTask, JstSubTask> context, JstSubTask subTask) {
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

    private JstOrdersResult queryJst(JobContext<JobSyncOffset, JstRootTask, JstSubTask> context, OrdersSingleQuery query) {
        JstOrdersResult jstResult = jstRemoteService.ordersSingleQuery(query);
        if (jstResult.isSuccess()) {
            if (log.isDebugEnabled()) {
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
