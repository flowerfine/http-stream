package cn.sliew.http.stream.format.jdbc.sql;

import lombok.Getter;

@Getter
public class DataSourceOptions {

    private final String driverName;
    private final String jdbcUrl;
    private final String username;
    private final String password;

    private DataSourceOptions(String driverName, String jdbcUrl, String username, String password) {
        this.driverName = driverName;
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public static DataSourceOptionsBuilder builder() {
        return new DataSourceOptionsBuilder();
    }

    public static class DataSourceOptionsBuilder {

        private String driverName;
        private String jdbcUrl;
        private String username;
        private String password;

        private DataSourceOptionsBuilder() {

        }

        public DataSourceOptionsBuilder driverName(String driverName) {
            this.driverName = driverName;
            return this;
        }

        public DataSourceOptionsBuilder jdbcUrl(String jdbcUrl) {
            this.jdbcUrl = jdbcUrl;
            return this;
        }

        public DataSourceOptionsBuilder username(String username) {
            this.username = username;
            return this;
        }

        public DataSourceOptionsBuilder password(String password) {
            this.password = password;
            return this;
        }

        public DataSourceOptions build() {
            return new DataSourceOptions(driverName, jdbcUrl, username, password);
        }
    }
}
