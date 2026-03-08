/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.client.renderer.chunk;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.ByteBufferBuilder;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.blaze3d.vertex.VertexSorting;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.SectionBufferBuilderPack;
import net.minecraft.client.renderer.block.BlockModelLighter;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.BlockStateModelSet;
import net.minecraft.client.renderer.block.LiquidBlockRenderer;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.chunk.RenderSectionRegion;
import net.minecraft.client.renderer.chunk.VisGraph;
import net.minecraft.client.renderer.chunk.VisibilitySet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.jspecify.annotations.Nullable;

public class SectionCompiler {
    private final boolean ambientOcclusion;
    private final boolean cutoutLeaves;
    private final BlockStateModelSet blockModelSet;
    private final LiquidBlockRenderer liquidRenderer;
    private final BlockColors blockColors;
    private final BlockEntityRenderDispatcher blockEntityRenderer;

    public SectionCompiler(boolean ambientOcclusion, boolean cutoutLeaves, BlockStateModelSet blockModelSet, LiquidBlockRenderer liquidRenderer, BlockColors blockColors, BlockEntityRenderDispatcher blockEntityRenderer) {
        this.ambientOcclusion = ambientOcclusion;
        this.cutoutLeaves = cutoutLeaves;
        this.blockModelSet = blockModelSet;
        this.liquidRenderer = liquidRenderer;
        this.blockColors = blockColors;
        this.blockEntityRenderer = blockEntityRenderer;
    }

    public Results compile(SectionPos sectionPos, RenderSectionRegion region, VertexSorting vertexSorting, SectionBufferBuilderPack builders) {
        Results results = new Results();
        BlockPos minPos = sectionPos.origin();
        BlockPos maxPos = minPos.offset(15, 15, 15);
        VisGraph visGraph = new VisGraph();
        BlockModelLighter.enableCaching();
        ModelBlockRenderer blockRenderer = new ModelBlockRenderer(this.ambientOcclusion, true, this.blockColors);
        EnumMap<ChunkSectionLayer, BufferBuilder> startedLayers = new EnumMap<ChunkSectionLayer, BufferBuilder>(ChunkSectionLayer.class);
        BlockQuadOutput quadOutput = (x, y, z, quad, instance) -> {
            BufferBuilder builder = this.getOrBeginLayer(startedLayers, builders, quad.spriteInfo().layer());
            builder.putBlockBakedQuad(x, y, z, quad, instance);
        };
        BlockQuadOutput opaqueQuadOutput = (x, y, z, quad, instance) -> {
            BufferBuilder builder = this.getOrBeginLayer(startedLayers, builders, ChunkSectionLayer.SOLID);
            builder.putBlockBakedQuad(x, y, z, quad, instance);
        };
        for (BlockPos blockPos : BlockPos.betweenClosed(minPos, maxPos)) {
            BlockState blockState = region.getBlockState(blockPos);
            if (blockState.isAir()) continue;
            try {
                FluidState fluidState;
                BlockEntity blockEntity;
                if (blockState.isSolidRender()) {
                    visGraph.setOpaque(blockPos);
                }
                if (blockState.hasBlockEntity() && (blockEntity = region.getBlockEntity(blockPos)) != null) {
                    this.handleBlockEntity(results, blockEntity);
                }
                if (!(fluidState = blockState.getFluidState()).isEmpty()) {
                    ChunkSectionLayer layer = this.liquidRenderer.getRenderLayer(fluidState);
                    BufferBuilder builder = this.getOrBeginLayer(startedLayers, builders, layer);
                    this.liquidRenderer.tesselate(region, blockPos, builder, blockState, fluidState);
                }
                if (blockState.getRenderShape() != RenderShape.MODEL) continue;
                blockRenderer.tesselateBlock(ModelBlockRenderer.forceOpaque(this.cutoutLeaves, blockState) ? opaqueQuadOutput : quadOutput, SectionPos.sectionRelative(blockPos.getX()), SectionPos.sectionRelative(blockPos.getY()), SectionPos.sectionRelative(blockPos.getZ()), region, blockPos, blockState, this.blockModelSet.get(blockState), blockState.getSeed(blockPos));
            }
            catch (Throwable t) {
                CrashReport report = CrashReport.forThrowable(t, "Tesselating block in world");
                CrashReportCategory category = report.addCategory("Block being tesselated");
                CrashReportCategory.populateBlockDetails(category, region, blockPos, blockState);
                throw new ReportedException(report);
            }
        }
        for (Map.Entry entry : startedLayers.entrySet()) {
            ChunkSectionLayer layer = (ChunkSectionLayer)((Object)entry.getKey());
            MeshData mesh = ((BufferBuilder)entry.getValue()).build();
            if (mesh == null) continue;
            if (layer == ChunkSectionLayer.TRANSLUCENT) {
                results.transparencyState = mesh.sortQuads(builders.buffer(layer), vertexSorting);
            }
            results.renderedLayers.put(layer, mesh);
        }
        BlockModelLighter.clearCache();
        results.visibilitySet = visGraph.resolve();
        return results;
    }

    private BufferBuilder getOrBeginLayer(Map<ChunkSectionLayer, BufferBuilder> startedLayers, SectionBufferBuilderPack buffers, ChunkSectionLayer layer) {
        BufferBuilder builder = startedLayers.get((Object)layer);
        if (builder == null) {
            ByteBufferBuilder buffer = buffers.buffer(layer);
            builder = new BufferBuilder(buffer, VertexFormat.Mode.QUADS, layer.vertexFormat());
            startedLayers.put(layer, builder);
        }
        return builder;
    }

    private <E extends BlockEntity> void handleBlockEntity(Results results, E blockEntity) {
        BlockEntityRenderer renderer = this.blockEntityRenderer.getRenderer(blockEntity);
        if (renderer != null && !renderer.shouldRenderOffScreen()) {
            results.blockEntities.add(blockEntity);
        }
    }

    public static final class Results {
        public final List<BlockEntity> blockEntities = new ArrayList<BlockEntity>();
        public final Map<ChunkSectionLayer, MeshData> renderedLayers = new EnumMap<ChunkSectionLayer, MeshData>(ChunkSectionLayer.class);
        public VisibilitySet visibilitySet = new VisibilitySet();
        public @Nullable MeshData.SortState transparencyState;

        public void release() {
            this.renderedLayers.values().forEach(MeshData::close);
        }
    }
}

