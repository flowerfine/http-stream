package cn.sliew.http.stream.remote.github;

import cn.sliew.http.stream.remote.github.client.commit.CommitListDO;

import java.util.List;

public interface GithubRemoteService {

    List<CommitListDO> listCommits(String owner, String repo);
}
