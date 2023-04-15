package cn.sliew.http.stream.format.jdbc;

import cn.sliew.http.stream.format.jdbc.sql.DataSourceOptions;
import cn.sliew.http.stream.format.jdbc.sql.DataSourceProvider;
import cn.sliew.http.stream.format.jdbc.sql.HikaricpDataSourceProvider;

import javax.sql.DataSource;

public abstract class BaseJdbcConnector<T extends BaseJdbcConnector> {

    protected DataSource dataSource;
    protected String sql;

    public T setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        return (T) this;
    }

    public T setDataSource(DataSourceProvider provider) {
        this.dataSource = provider.getDataSource();
        return (T) this;
    }

    public T setDataSource(DataSourceOptions options) {
        return setDataSource(new HikaricpDataSourceProvider(options));
    }

    public T setSql(String sql) {
        this.sql = sql;
        return (T) this;
    }
}
