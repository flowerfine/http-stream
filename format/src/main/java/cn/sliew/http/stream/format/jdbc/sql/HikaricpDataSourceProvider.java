package cn.sliew.http.stream.format.jdbc.sql;

import javax.sql.DataSource;

public class HikaricpDataSourceProvider implements DataSourceProvider{

    private final DataSourceOptions options;

    public HikaricpDataSourceProvider(DataSourceOptions options) {
        this.options = options;
    }

    @Override
    public DataSource getDataSource() {
        return null;
    }
}
