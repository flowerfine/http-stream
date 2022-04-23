package cn.sliew.http.stream.flink.github.commit;

import cn.sliew.http.stream.flink.HttpSourceSplit;
import cn.sliew.http.stream.flink.github.MessageParameters;
import cn.sliew.http.stream.flink.reader.StreamFormat;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.flink.api.common.typeinfo.TypeInformation;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.formats.json.JsonRowSchemaConverter;

import javax.annotation.Nullable;
import java.io.IOException;

/**
 * 接口返回数据转换为读取，参考 flink json 和 csv 的实现，杂糅在一块
 *
 * @param <T>
 * @see JsonRowSchemaConverter
 */
public class CommitStreamFormat<T> implements StreamFormat<T, HttpSourceSplit> {

    private final ObjectMapper objectMapper;
    private final TypeInformation<T> typeInformation;
    private final OkHttpClient client;
    private final MessageParameters parameters;

    public CommitStreamFormat(ObjectMapper objectMapper, TypeInformation<T> typeInformation, OkHttpClient client, MessageParameters parameters) {
        this.objectMapper = objectMapper;
        this.typeInformation = typeInformation;
        this.client = client;
        this.parameters = parameters;
    }

    @Override
    public Reader<T> createReader(Configuration config, HttpSourceSplit split) throws IOException {
        return new PageReader(split);
    }

    @Override
    public Reader<T> restoreReader(Configuration config, HttpSourceSplit split) throws IOException {
        return new PageReader<>(split);
    }

    @Override
    public boolean isSplittable() {
        return false;
    }

    @Override
    public TypeInformation<T> getProducedType() {
        return typeInformation;
    }

    private class PageReader<T> implements Reader<T> {

        private HttpSourceSplit split;
        private MappingIterator<T> iterator;

        public PageReader(HttpSourceSplit split) throws IOException {
            this.split = split;
            fetchData();
        }

        private boolean fetchData() throws IOException {
            Request request = parameters.getRequest();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                throw new RuntimeException(String.format("code: %d, message: %s", response.code(), response.message()));
            }
            this.iterator = objectMapper.readerFor(typeInformation.getTypeClass()).readValues(response.body().bytes());
            // todo 更新下一页，判断是否有下一页
            return true;
        }

        @Nullable
        @Override
        public T read() throws IOException {
            while (iterator.hasNext()) {
                return iterator.next();
            }
            if (fetchData()) {
                return read();
            }
            return null;
        }

        @Override
        public void close() throws IOException {
            iterator.close();
        }
    }
}
