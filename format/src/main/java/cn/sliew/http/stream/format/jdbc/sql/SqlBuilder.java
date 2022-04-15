package cn.sliew.http.stream.format.jdbc.sql;

import cn.sliew.http.stream.format.jdbc.parsing.GenericTokenParser;
import cn.sliew.http.stream.format.jdbc.parsing.TokenHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SqlBuilder {

    private final String originalSql;
    private String sql;
    private List<String> parameters;

    public SqlBuilder(String originalSql) {
        this.originalSql = originalSql;
    }

    public String getSql() {
        return sql;
    }

    public List<String> getParameters() {
        return parameters;
    }

    public void parse() {
        ParameterTokenHandler handler = new ParameterTokenHandler();
        GenericTokenParser parser = new GenericTokenParser("#{", "}", handler);
        this.sql = parser.parse(removeExtraWhitespaces(originalSql));
        this.parameters = handler.getParameters();
    }

    public static String removeExtraWhitespaces(String original) {
        StringTokenizer tokenizer = new StringTokenizer(original);
        StringBuilder builder = new StringBuilder();
        boolean hasMoreTokens = tokenizer.hasMoreTokens();
        while (hasMoreTokens) {
            builder.append(tokenizer.nextToken());
            hasMoreTokens = tokenizer.hasMoreTokens();
            if (hasMoreTokens) {
                builder.append(' ');
            }
        }
        return builder.toString();
    }

    private static class ParameterTokenHandler implements TokenHandler {

        private final List<String> parameters = new ArrayList<>();

        public List<String> getParameters() {
            return parameters;
        }

        @Override
        public String handleToken(String content) {
            parameters.add(content);
            return "?";
        }
    }
}
