package cn.sliew.http.stream.remote.github;

import cn.sliew.http.stream.remote.github.client.GithubClient;
import cn.sliew.http.stream.remote.github.client.commit.CommitListDO;
import cn.sliew.http.stream.remote.github.client.commit.CommitListQuery;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GithubRemoteServiceImpl implements GithubRemoteService {

    @Autowired
    private GithubClient client;

    @Override
    public List<CommitListDO> listCommits(String owner, String repo, CommitListQuery query) {
        return client.listCommits(owner, repo, query);
    }
}
