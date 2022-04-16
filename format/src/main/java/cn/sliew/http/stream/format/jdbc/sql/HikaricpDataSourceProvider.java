package cn.sliew.http.stream.format.jdbc.sql;

import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class HikaricpDataSourceProvider implements DataSourceProvider{

    private final DataSourceOptions options;

    public HikaricpDataSourceProvider(DataSourceOptions options) {
        this.options = options;
    }

    @Override
    public DataSource getDataSource() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(options.getDriverName());
        dataSource.setJdbcUrl(options.getJdbcUrl());
        dataSource.setUsername(options.getUsername());
        dataSource.setPassword(options.getPassword());
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(5);
        dataSource.setConnectionInitSql("select 1 from dual");
        return dataSource;
    }
}
