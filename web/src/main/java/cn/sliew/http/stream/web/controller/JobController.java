package cn.sliew.http.stream.web.controller;

import cn.sliew.http.stream.akka.jst.order.OrderStreamJob;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/job")
@Tag(name = "/job", description = "任务接口")
public class JobController {

    @Autowired
    private OrderStreamJob orderStreamJob;

    @GetMapping("orderStreamJob")
    @ApiOperation("订单同步")
    public void orderStreamJob() throws Exception {
        orderStreamJob.execute();
    }
}
