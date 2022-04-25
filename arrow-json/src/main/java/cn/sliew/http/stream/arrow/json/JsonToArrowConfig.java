package cn.sliew.http.stream.arrow.json;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.util.Preconditions;
import org.apache.arrow.vector.dictionary.DictionaryProvider;

import java.util.Set;

/**
 * This class configures the Avro-to-Arrow conversion process.
 */
public class JsonToArrowConfig {

    private final BufferAllocator allocator;
    /**
     * The maximum rowCount to read each time when partially convert data.
     * Default value is 1024 and -1 means read all data into one vector.
     */
    private final int targetBatchSize;

    /**
     * The dictionary provider used for enum type.
     * If avro schema has enum type, will create dictionary and update this provider.
     */
    private final DictionaryProvider.MapDictionaryProvider provider;

    /**
     * The field names which to skip when reading decoder values.
     */
    private final Set<String> skipFieldNames;

    /**
     * Instantiate an instance.
     *
     * @param allocator       The memory allocator to construct the Arrow vectors with.
     * @param targetBatchSize The maximum rowCount to read each time when partially convert data.
     * @param provider        The dictionary provider used for enum type, adapter will update this provider.
     * @param skipFieldNames  Field names which to skip.
     */
    JsonToArrowConfig(
            BufferAllocator allocator,
            int targetBatchSize,
            DictionaryProvider.MapDictionaryProvider provider,
            Set<String> skipFieldNames) {

        Preconditions.checkArgument(targetBatchSize == JsonToArrowVectorIterator.NO_LIMIT_BATCH_SIZE ||
                targetBatchSize > 0, "invalid targetBatchSize: %s", targetBatchSize);

        this.allocator = allocator;
        this.targetBatchSize = targetBatchSize;
        this.provider = provider;
        this.skipFieldNames = skipFieldNames;
    }

    public BufferAllocator getAllocator() {
        return allocator;
    }

    public int getTargetBatchSize() {
        return targetBatchSize;
    }

    public DictionaryProvider.MapDictionaryProvider getProvider() {
        return provider;
    }

    public Set<String> getSkipFieldNames() {
        return skipFieldNames;
    }
}