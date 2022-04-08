# http-stream

There are already so many data integration tools for mass、fast、accurate data processing:

* [datax](https://github.com/alibaba/DataX).
* [sqoop](https://github.com/apache/sqoop).
* [kettle](https://github.com/pentaho/pentaho-kettle).
* [hop](https://github.com/apache/hop).
* [airbyte](https://github.com/airbytehq/airbyte).
* [flinkx(chunjun)](https://github.com/DTStack/chunjun).
* [nifi](https://github.com/apache/nifi).
* [seatunnel](https://github.com/apache/incubator-seatunnel).
* [gobblin](https://github.com/apache/gobblin).

But how to sync data through http is also a big problem for developers.

* various request and response. Different http api provider, different rules have to obey.
* authentication and authorization.
* ratelimiter. qps(query per second) or concurrency.
* network failure. not threaten or caution, frequent network failure would never disappear.

## Challenges and Solutions

### various request

* pagination。
    * [Rethinking Streaming Workloads with Akka Streams: Part II](https://blog.colinbreck.com/rethinking-streaming-workloads-with-akka-streams-part-ii/)。
    * [PagedSource](https://github.com/akka/akka-stream-contrib/blob/main/src/main/scala/akka/stream/contrib/PagedSource.scala)。
* entire or incremental extraction。



## Excellent Practices

### fault tolerance

retry and ratelimiter

* [resilience4j](https://github.com/resilience4j/resilience4j)
* [failsafe](https://github.com/failsafe-lib/failsafe)
* [guava-retrying](https://github.com/rholder/guava-retrying)

### detailed query context

query param and response result

### metrics

* [micrometer](https://github.com/micrometer-metrics/micrometer)
* [metrics](https://github.com/dropwizard/metrics)





