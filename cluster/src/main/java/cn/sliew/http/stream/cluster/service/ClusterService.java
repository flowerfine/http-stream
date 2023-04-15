package cn.sliew.http.stream.cluster.service;

public interface ClusterService {

    void join();

    void leave();

    void down();

    void state();

}
