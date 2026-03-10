/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap
 *  java.util.SequencedMap
 */
package net.mayaan.client.renderer;

import com.maayanlabs.blaze3d.vertex.ByteBufferBuilder;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.SequencedMap;
import net.mayaan.client.renderer.MultiBufferSource;
import net.mayaan.client.renderer.OutlineBufferSource;
import net.mayaan.client.renderer.SectionBufferBuilderPack;
import net.mayaan.client.renderer.SectionBufferBuilderPool;
import net.mayaan.client.renderer.Sheets;
import net.mayaan.client.renderer.chunk.ChunkSectionLayer;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.resources.model.ModelBakery;
import net.mayaan.util.Util;

public class RenderBuffers {
    private final SectionBufferBuilderPack fixedBufferPack = new SectionBufferBuilderPack();
    private final SectionBufferBuilderPool sectionBufferPool;
    private final MultiBufferSource.BufferSource bufferSource;
    private final MultiBufferSource.BufferSource crumblingBufferSource;
    private final OutlineBufferSource outlineBufferSource;

    public RenderBuffers(int maxSectionBuilders) {
        this.sectionBufferPool = SectionBufferBuilderPool.allocate(maxSectionBuilders);
        SequencedMap fixedBuffers = (SequencedMap)Util.make(new Object2ObjectLinkedOpenHashMap(), map -> {
            map.put((Object)Sheets.cutoutBlockItemSheet(), (Object)this.fixedBufferPack.buffer(ChunkSectionLayer.CUTOUT));
            map.put((Object)Sheets.translucentBlockItemSheet(), (Object)this.fixedBufferPack.buffer(ChunkSectionLayer.TRANSLUCENT));
            RenderBuffers.put((Object2ObjectLinkedOpenHashMap<RenderType, ByteBufferBuilder>)map, Sheets.cutoutItemSheet());
            RenderBuffers.put((Object2ObjectLinkedOpenHashMap<RenderType, ByteBufferBuilder>)map, Sheets.translucentItemSheet());
            RenderBuffers.put((Object2ObjectLinkedOpenHashMap<RenderType, ByteBufferBuilder>)map, RenderTypes.armorEntityGlint());
            RenderBuffers.put((Object2ObjectLinkedOpenHashMap<RenderType, ByteBufferBuilder>)map, RenderTypes.glint());
            RenderBuffers.put((Object2ObjectLinkedOpenHashMap<RenderType, ByteBufferBuilder>)map, RenderTypes.glintTranslucent());
            RenderBuffers.put((Object2ObjectLinkedOpenHashMap<RenderType, ByteBufferBuilder>)map, RenderTypes.entityGlint());
            RenderBuffers.put((Object2ObjectLinkedOpenHashMap<RenderType, ByteBufferBuilder>)map, RenderTypes.waterMask());
        });
        this.bufferSource = MultiBufferSource.immediateWithBuffers((SequencedMap<RenderType, ByteBufferBuilder>)fixedBuffers, new ByteBufferBuilder(786432));
        this.outlineBufferSource = new OutlineBufferSource();
        SequencedMap crumblingBuffers = (SequencedMap)Util.make(new Object2ObjectLinkedOpenHashMap(), map -> ModelBakery.DESTROY_TYPES.forEach(type -> RenderBuffers.put((Object2ObjectLinkedOpenHashMap<RenderType, ByteBufferBuilder>)map, type)));
        this.crumblingBufferSource = MultiBufferSource.immediateWithBuffers((SequencedMap<RenderType, ByteBufferBuilder>)crumblingBuffers, new ByteBufferBuilder(0));
    }

    private static void put(Object2ObjectLinkedOpenHashMap<RenderType, ByteBufferBuilder> map, RenderType type) {
        map.put((Object)type, (Object)new ByteBufferBuilder(type.bufferSize()));
    }

    public SectionBufferBuilderPack fixedBufferPack() {
        return this.fixedBufferPack;
    }

    public SectionBufferBuilderPool sectionBufferPool() {
        return this.sectionBufferPool;
    }

    public MultiBufferSource.BufferSource bufferSource() {
        return this.bufferSource;
    }

    public MultiBufferSource.BufferSource crumblingBufferSource() {
        return this.crumblingBufferSource;
    }

    public OutlineBufferSource outlineBufferSource() {
        return this.outlineBufferSource;
    }
}

