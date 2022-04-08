# http-stream

There are already so many data integration tools for mass、fast、accurate data processing:

* datax.
* sqoop.
* airbyte.
* flinkx.
* seatunnel.
* gobblin.

But how to sync data through http is also a big problem for developers.

* various request and response. Different http api provider, different rules have to obey.
* authentication and authorization.
* ratelimiter. qps(query per second) or concurrency.
* network failure. not threaten or caution, frequent network failure would never disappear.

