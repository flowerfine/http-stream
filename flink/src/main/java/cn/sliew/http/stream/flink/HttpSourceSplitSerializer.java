/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cn.sliew.http.stream.flink;

import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.core.io.SimpleVersionedSerializer;
import org.apache.flink.core.memory.DataInputDeserializer;
import org.apache.flink.core.memory.DataOutputSerializer;

import java.io.IOException;

/**
 * A serializer for the {@link HttpSourceSplit}.
 */
@PublicEvolving
public final class HttpSourceSplitSerializer implements SimpleVersionedSerializer<HttpSourceSplit> {

    public static final HttpSourceSplitSerializer INSTANCE = new HttpSourceSplitSerializer();

    private static final ThreadLocal<DataOutputSerializer> SERIALIZER_CACHE =
            ThreadLocal.withInitial(() -> new DataOutputSerializer(64));

    private static final int VERSION = 1;

    // ------------------------------------------------------------------------

    @Override
    public int getVersion() {
        return VERSION;
    }

    @Override
    public byte[] serialize(HttpSourceSplit split) throws IOException {
        final DataOutputSerializer out = SERIALIZER_CACHE.get();

        final byte[] result = out.getCopyOfBuffer();
        out.clear();

        return result;
    }

    @Override
    public HttpSourceSplit deserialize(int version, byte[] serialized) throws IOException {
        if (version == 1) {
            return deserializeV1(serialized);
        }
        throw new IOException("Unknown version: " + version);
    }

    private static HttpSourceSplit deserializeV1(byte[] serialized) throws IOException {
        final DataInputDeserializer in = new DataInputDeserializer(serialized);

        // instantiate a new split and cache the serialized form
        return null;
    }

}
