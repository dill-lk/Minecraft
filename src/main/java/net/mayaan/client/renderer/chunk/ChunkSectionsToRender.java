/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap
 */
package net.mayaan.client.renderer.chunk;

import com.maayanlabs.blaze3d.buffers.GpuBuffer;
import com.maayanlabs.blaze3d.buffers.GpuBufferSlice;
import com.maayanlabs.blaze3d.pipeline.RenderTarget;
import com.maayanlabs.blaze3d.systems.RenderPass;
import com.maayanlabs.blaze3d.systems.RenderSystem;
import com.maayanlabs.blaze3d.textures.FilterMode;
import com.maayanlabs.blaze3d.textures.GpuSampler;
import com.maayanlabs.blaze3d.textures.GpuTextureView;
import com.maayanlabs.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.EnumMap;
import java.util.List;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import net.mayaan.SharedConstants;
import net.mayaan.client.Mayaan;
import net.mayaan.client.renderer.RenderPipelines;
import net.mayaan.client.renderer.chunk.ChunkSectionLayer;
import net.mayaan.client.renderer.chunk.ChunkSectionLayerGroup;

public record ChunkSectionsToRender(GpuTextureView textureView, EnumMap<ChunkSectionLayer, Int2ObjectOpenHashMap<List<RenderPass.Draw<GpuBufferSlice[]>>>> drawGroupsPerLayer, int maxIndicesRequired, GpuBufferSlice[] chunkSectionInfos) {
    public void renderGroup(ChunkSectionLayerGroup group, GpuSampler sampler) {
        RenderSystem.AutoStorageIndexBuffer autoIndices = RenderSystem.getSequentialBuffer(VertexFormat.Mode.QUADS);
        GpuBuffer defaultIndexBuffer = this.maxIndicesRequired == 0 ? null : autoIndices.getBuffer(this.maxIndicesRequired);
        VertexFormat.IndexType defaultIndexType = this.maxIndicesRequired == 0 ? null : autoIndices.type();
        ChunkSectionLayer[] layers = group.layers();
        Mayaan minecraft = Mayaan.getInstance();
        boolean wireframe = SharedConstants.DEBUG_HOTKEYS && minecraft.wireframe;
        RenderTarget renderTarget = group.outputTarget();
        try (RenderPass renderPass = RenderSystem.getDevice().createCommandEncoder().createRenderPass(() -> "Section layers for " + group.label(), renderTarget.getColorTextureView(), OptionalInt.empty(), renderTarget.getDepthTextureView(), OptionalDouble.empty());){
            RenderSystem.bindDefaultUniforms(renderPass);
            renderPass.bindTexture("Sampler0", this.textureView, sampler);
            renderPass.bindTexture("Sampler2", minecraft.gameRenderer.lightmap(), RenderSystem.getSamplerCache().getClampToEdge(FilterMode.LINEAR));
            for (ChunkSectionLayer layer : layers) {
                renderPass.setPipeline(wireframe ? RenderPipelines.WIREFRAME : layer.pipeline());
                Int2ObjectOpenHashMap<List<RenderPass.Draw<GpuBufferSlice[]>>> drawGroup = this.drawGroupsPerLayer.get((Object)layer);
                for (List draws : drawGroup.values()) {
                    if (draws.isEmpty()) continue;
                    if (layer == ChunkSectionLayer.TRANSLUCENT) {
                        draws = draws.reversed();
                    }
                    renderPass.drawMultipleIndexed(draws, defaultIndexBuffer, defaultIndexType, List.of("ChunkSection"), this.chunkSectionInfos);
                }
            }
        }
    }
}

