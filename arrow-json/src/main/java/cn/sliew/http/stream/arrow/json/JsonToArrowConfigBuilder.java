package cn.sliew.http.stream.arrow.json;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.dictionary.DictionaryProvider;

import java.util.HashSet;
import java.util.Set;

/**
 * This class builds {@link JsonToArrowConfig}s.
 */
public class JsonToArrowConfigBuilder {

    private BufferAllocator allocator;

    private int targetBatchSize;

    private DictionaryProvider.MapDictionaryProvider provider;

    private Set<String> skipFieldNames;

    /**
     * Default constructor for the {@link JsonToArrowConfigBuilder}.
     */
    public JsonToArrowConfigBuilder(BufferAllocator allocator) {
        this.allocator = allocator;
        this.targetBatchSize = -1;
        this.provider = new DictionaryProvider.MapDictionaryProvider();
        this.skipFieldNames = new HashSet<>();
    }

    public JsonToArrowConfigBuilder setTargetBatchSize(int targetBatchSize) {
        this.targetBatchSize = targetBatchSize;
        return this;
    }

    public JsonToArrowConfigBuilder setProvider(DictionaryProvider.MapDictionaryProvider provider) {
        this.provider = provider;
        return this;
    }

    public JsonToArrowConfigBuilder setSkipFieldNames(Set<String> skipFieldNames) {
        this.skipFieldNames = skipFieldNames;
        return this;
    }

    /**
     * This builds the {@link JsonToArrowConfig} from the provided params.
     */
    public JsonToArrowConfig build() {
        return new JsonToArrowConfig(
                allocator,
                targetBatchSize,
                provider,
                skipFieldNames);
    }
}