package cn.sliew.http.stream.flink.util;

public interface HttpSourceParameters {

    Object[] getPathParameters();
    Object[] getQueryParameters();
    Object[] getBodyParameters();
    Object[] getHeaderParameters();
}
