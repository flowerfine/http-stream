package cn.sliew.http.stream.web.controller.remote;

import cn.sliew.http.stream.akka.util.JacksonUtil;
import cn.sliew.http.stream.flink.github.commit.CommitHeaders;
import cn.sliew.http.stream.flink.github.commit.CommitMessageParameters;
import cn.sliew.http.stream.flink.github.commit.CommitResponseBody;
import cn.sliew.http.stream.remote.github.GithubRemoteService;
import cn.sliew.http.stream.remote.github.client.commit.CommitListDO;
import cn.sliew.http.stream.remote.github.client.commit.CommitListQuery;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.runtime.rest.RestClient;
import org.apache.flink.runtime.rest.RestClientConfiguration;
import org.apache.flink.runtime.rest.messages.EmptyRequestBody;
import org.apache.flink.runtime.util.ExecutorThreadFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@RestController
@RequestMapping("/remote/github")
@Tag(name = "/remote/github", description = "github 接口")
public class GithubController {

    private final ExecutorService executorService = Executors.newFixedThreadPool(4, new ExecutorThreadFactory("Flink-RestClusterClient-IO"));

    @Autowired
    private GithubRemoteService githubRemoteService;

    @GetMapping("{owner}/{repo}/commits")
    @ApiOperation("查询仓库提交记录")
    public List<CommitListDO> listCommits(@PathVariable("owner") String owner,
                                          @PathVariable("repo") String repo,
                                          CommitListQuery query) throws Exception {

        RestClientConfiguration configuration = RestClientConfiguration.fromConfiguration(new Configuration());
        RestClient client = new RestClient(configuration, executorService);
        CommitMessageParameters parameters = new CommitMessageParameters();
        CompletableFuture<CommitResponseBody> future = client.sendRequest("https://api.github.com/", 80, CommitHeaders.getInstance(), parameters, EmptyRequestBody.getInstance());
        CommitResponseBody commitResponseBody = future.get();
        System.out.println(JacksonUtil.toJsonString(commitResponseBody));
        return githubRemoteService.listCommits(owner, repo, query);
    }
}
