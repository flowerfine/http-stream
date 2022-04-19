package cn.sliew.http.stream.remote.github;

import cn.sliew.http.stream.remote.github.client.commit.CommitListDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class GithubRemoteServiceImpl implements GithubRemoteService {

    @Override
    public List<CommitListDO> listCommits(String owner, String repo) {
        return null;
    }
}
