package cn.sliew.http.stream.web.controller.remote;

import cn.sliew.http.stream.remote.github.GithubRemoteService;
import cn.sliew.http.stream.remote.github.client.commit.CommitListDO;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/remote/github")
@Tag(name = "/remote/github", description = "github 接口")
public class GithubController {

    @Autowired
    private GithubRemoteService githubRemoteService;

    @GetMapping("{owner}/{repo}/commits")
    @ApiOperation("查询仓库提交记录")
    public List<CommitListDO> listCommits(@PathVariable("owner") String owner,
                                          @PathVariable("repo") String repo) {
        return githubRemoteService.listCommits(owner, repo);
    }
}
