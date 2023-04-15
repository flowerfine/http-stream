package cn.sliew.http.stream.cluster.service.impl;

import akka.actor.typed.ActorSystem;
import akka.actor.typed.SpawnProtocol;
import akka.cluster.ClusterEvent;
import akka.cluster.typed.*;
import cn.sliew.http.stream.cluster.service.ClusterService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ClusterServiceImpl implements ClusterService, InitializingBean {

    @Autowired
    private ActorSystem<SpawnProtocol.Command> actorSystem;
    private Cluster cluster;

    @Override
    public void afterPropertiesSet() throws Exception {
        cluster = Cluster.get(actorSystem);
//        cluster.subscriptions().tell(Subscribe.create(subscriber, ClusterEvent.MemberEvent.class));
    }

    @Override
    public void join() {
        cluster.manager().tell(Join.apply(cluster.selfMember().address()));
    }

    @Override
    public void leave() {
        cluster.manager().tell(Leave.apply(cluster.selfMember().address()));
    }

    @Override
    public void down() {
        cluster.manager().tell(Down.apply(cluster.selfMember().address()));
    }

    @Override
    public void state() {
        ClusterEvent.CurrentClusterState state = cluster.state();
    }
}
