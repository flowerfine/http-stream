package cn.sliew.http.stream.format.jdbc.sql;

import javax.sql.DataSource;

public interface DataSourceProvider {

    DataSource getDataSource();
}
