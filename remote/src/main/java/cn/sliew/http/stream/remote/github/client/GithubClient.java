package cn.sliew.http.stream.remote.github.client;

import cn.sliew.http.stream.remote.config.GithubFeignConfig;
import cn.sliew.http.stream.remote.github.client.commit.CommitListDO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(value = "githubClient", url = "https://api.github.com/", configuration = GithubFeignConfig.class)
public interface GithubClient {

    @GetMapping(value = "repos/{owner}/{repo}/commits", produces = "application/json", consumes = "application/vnd.github.v3+json")
    List<CommitListDO> listCommits(@PathVariable("owner") String owner,
                                   @PathVariable("repo") String repo);
}
