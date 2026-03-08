/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  java.lang.MatchException
 *  org.joml.Matrix4fc
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.client.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.QuadInstance;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.block.BlockQuadOutput;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.block.MovingBlockRenderState;
import net.minecraft.client.renderer.block.dispatch.BlockStateModel;
import net.minecraft.client.renderer.block.dispatch.BlockStateModelPart;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.OptionsRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Matrix4fc;
import org.jspecify.annotations.Nullable;

public class BlockFeatureRenderer {
    private static final Direction[] DIRECTIONS = Direction.values();

    public void renderSolid(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, BlockRenderDispatcher blockRenderDispatcher, OutlineBufferSource outlineBufferSource, OptionsRenderState optionsState) {
        this.renderMovingBlockSubmits(nodeCollection, bufferSource, blockRenderDispatcher, optionsState, false);
        this.renderBlockModelSubmits(nodeCollection, bufferSource, outlineBufferSource, false);
    }

    public void renderTranslucent(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, BlockRenderDispatcher blockRenderDispatcher, OutlineBufferSource outlineBufferSource, OptionsRenderState optionsState) {
        this.renderMovingBlockSubmits(nodeCollection, bufferSource, blockRenderDispatcher, optionsState, true);
        this.renderBlockModelSubmits(nodeCollection, bufferSource, outlineBufferSource, true);
    }

    private void renderMovingBlockSubmits(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, BlockRenderDispatcher blockRenderDispatcher, OptionsRenderState optionsState, boolean translucent) {
        PoseStack poseStack = new PoseStack();
        BlockQuadOutput output = (x, y, z, quad, instance) -> BlockFeatureRenderer.putBakedQuad(poseStack, bufferSource, x, y, z, quad, instance, quad.spriteInfo().layer());
        BlockQuadOutput solidOutput = (x, y, z, quad, instance) -> BlockFeatureRenderer.putBakedQuad(poseStack, bufferSource, x, y, z, quad, instance, ChunkSectionLayer.SOLID);
        Minecraft minecraft = Minecraft.getInstance();
        boolean ambientOcclusion = optionsState.ambientOcclusion;
        boolean cutoutLeaves = optionsState.cutoutLeaves;
        ModelBlockRenderer blockRenderer = new ModelBlockRenderer(ambientOcclusion, false, minecraft.getBlockColors());
        for (SubmitNodeStorage.MovingBlockSubmit submit : nodeCollection.getMovingBlockSubmits()) {
            MovingBlockRenderState movingBlockRenderState = submit.movingBlockRenderState();
            BlockState blockState = movingBlockRenderState.blockState;
            BlockStateModel model = blockRenderDispatcher.getBlockModel(blockState);
            if (model.hasTranslucency() != translucent) continue;
            poseStack.setIdentity();
            poseStack.mulPose((Matrix4fc)submit.pose());
            BlockQuadOutput blockOutput = ModelBlockRenderer.forceOpaque(cutoutLeaves, blockState) ? solidOutput : output;
            long blockSeed = blockState.getSeed(movingBlockRenderState.randomSeedPos);
            blockRenderer.tesselateBlock(blockOutput, 0.0f, 0.0f, 0.0f, movingBlockRenderState, movingBlockRenderState.blockPos, blockState, model, blockSeed);
        }
    }

    private static void putBakedQuad(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, float x, float y, float z, BakedQuad quad, QuadInstance instance, ChunkSectionLayer layer) {
        poseStack.pushPose();
        poseStack.translate(x, y, z);
        VertexConsumer buffer = bufferSource.getBuffer(switch (layer) {
            default -> throw new MatchException(null, null);
            case ChunkSectionLayer.SOLID -> RenderTypes.solidMovingBlock();
            case ChunkSectionLayer.CUTOUT -> RenderTypes.cutoutMovingBlock();
            case ChunkSectionLayer.TRANSLUCENT -> RenderTypes.translucentMovingBlock();
        });
        buffer.putBakedQuad(poseStack.last(), quad, instance);
        poseStack.popPose();
    }

    private void renderBlockModelSubmits(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, OutlineBufferSource outlineBufferSource, boolean translucent) {
        for (SubmitNodeStorage.BlockModelSubmit submit : nodeCollection.getBlockModelSubmits()) {
            VertexConsumer outlineBuffer;
            if (submit.renderType().hasBlending() != translucent) continue;
            VertexConsumer buffer = bufferSource.getBuffer(submit.renderType());
            if (submit.outlineColor() != 0) {
                outlineBufferSource.setColor(submit.outlineColor());
                outlineBuffer = outlineBufferSource.getBuffer(submit.renderType());
            } else {
                outlineBuffer = null;
            }
            QuadInstance quadInstance = new QuadInstance();
            quadInstance.setLightCoords(submit.lightCoords());
            quadInstance.setOverlayCoords(submit.overlayCoords());
            for (BlockStateModelPart part : submit.modelParts()) {
                BlockFeatureRenderer.putPartQuads(part, submit.pose(), quadInstance, submit.tintLayers(), buffer, outlineBuffer);
            }
        }
    }

    private static void putPartQuads(BlockStateModelPart part, PoseStack.Pose pose, QuadInstance quadInstance, int[] tintLayers, VertexConsumer buffer, @Nullable VertexConsumer outlineBuffer) {
        for (Direction direction : DIRECTIONS) {
            for (BakedQuad quad : part.getQuads(direction)) {
                BlockFeatureRenderer.putQuad(pose, quad, quadInstance, tintLayers, buffer, outlineBuffer);
            }
        }
        for (BakedQuad quad : part.getQuads(null)) {
            BlockFeatureRenderer.putQuad(pose, quad, quadInstance, tintLayers, buffer, outlineBuffer);
        }
    }

    private static void putQuad(PoseStack.Pose pose, BakedQuad quad, QuadInstance instance, int[] tintLayers, VertexConsumer buffer, @Nullable VertexConsumer outlineBuffer) {
        int tintIndex = quad.tintIndex();
        boolean tintColor = tintIndex != -1 && tintIndex < tintLayers.length;
        instance.setColor(tintColor ? tintLayers[tintIndex] : -1);
        buffer.putBakedQuad(pose, quad, instance);
        if (outlineBuffer != null) {
            outlineBuffer.putBakedQuad(pose, quad, instance);
        }
    }
}

