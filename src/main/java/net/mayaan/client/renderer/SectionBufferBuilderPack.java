/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer;

import com.maayanlabs.blaze3d.vertex.ByteBufferBuilder;
import java.util.Arrays;
import java.util.Map;
import net.mayaan.client.renderer.chunk.ChunkSectionLayer;
import net.mayaan.util.Util;

public class SectionBufferBuilderPack
implements AutoCloseable {
    public static final int TOTAL_BUFFERS_SIZE = Arrays.stream(ChunkSectionLayer.values()).mapToInt(ChunkSectionLayer::bufferSize).sum();
    private final Map<ChunkSectionLayer, ByteBufferBuilder> buffers = Util.makeEnumMap(ChunkSectionLayer.class, layer -> new ByteBufferBuilder(layer.bufferSize()));

    public ByteBufferBuilder buffer(ChunkSectionLayer layer) {
        return this.buffers.get((Object)layer);
    }

    public void clearAll() {
        this.buffers.values().forEach(ByteBufferBuilder::clear);
    }

    public void discardAll() {
        this.buffers.values().forEach(ByteBufferBuilder::discard);
    }

    @Override
    public void close() {
        this.buffers.values().forEach(ByteBufferBuilder::close);
    }
}

