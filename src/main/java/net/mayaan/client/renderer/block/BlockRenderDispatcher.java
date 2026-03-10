/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.block;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.blaze3d.vertex.QuadInstance;
import com.maayanlabs.blaze3d.vertex.SheetedDecalTextureGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.mayaan.client.color.block.BlockColors;
import net.mayaan.client.renderer.MultiBufferSource;
import net.mayaan.client.renderer.block.BlockStateModelSet;
import net.mayaan.client.renderer.block.LiquidBlockRenderer;
import net.mayaan.client.renderer.block.dispatch.BlockStateModel;
import net.mayaan.client.renderer.block.dispatch.BlockStateModelPart;
import net.mayaan.client.resources.model.ModelBakery;
import net.mayaan.client.resources.model.ModelManager;
import net.mayaan.client.resources.model.geometry.BakedQuad;
import net.mayaan.client.resources.model.sprite.SpriteGetter;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.server.packs.resources.ResourceManager;
import net.mayaan.server.packs.resources.ResourceManagerReloadListener;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.block.RenderShape;
import net.mayaan.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class BlockRenderDispatcher
implements ResourceManagerReloadListener {
    private static final Direction[] DIRECTIONS = Direction.values();
    private final ModelManager modelManager;
    private final SpriteGetter sprites;
    private @Nullable LiquidBlockRenderer liquidBlockRenderer;
    private final RandomSource singleThreadRandom = RandomSource.create();
    private final List<BlockStateModelPart> singleThreadPartList = new ArrayList<BlockStateModelPart>();

    public BlockRenderDispatcher(ModelManager modelManager, SpriteGetter sprites, BlockColors blockColors) {
        this.modelManager = modelManager;
        this.sprites = sprites;
    }

    public void renderBreakingTexture(BlockState state, BlockPos pos, PoseStack poseStack, MultiBufferSource bufferSource, int progress) {
        if (state.getRenderShape() != RenderShape.MODEL) {
            return;
        }
        PoseStack.Pose pose = poseStack.last();
        SheetedDecalTextureGenerator buffer = new SheetedDecalTextureGenerator(bufferSource.getBuffer(ModelBakery.DESTROY_TYPES.get(progress)), pose, 1.0f);
        BlockStateModel model = this.modelManager.getBlockStateModelSet().get(state);
        this.singleThreadRandom.setSeed(state.getSeed(pos));
        this.singleThreadPartList.clear();
        model.collectParts(this.singleThreadRandom, this.singleThreadPartList);
        QuadInstance instance = new QuadInstance();
        for (BlockStateModelPart part : this.singleThreadPartList) {
            for (Direction direction : DIRECTIONS) {
                for (BakedQuad quad : part.getQuads(direction)) {
                    buffer.putBakedQuad(pose, quad, instance);
                }
            }
            for (BakedQuad quad : part.getQuads(null)) {
                buffer.putBakedQuad(pose, quad, instance);
            }
        }
    }

    public BlockStateModelSet getModelSet() {
        return this.modelManager.getBlockStateModelSet();
    }

    public LiquidBlockRenderer getLiquidRenderer() {
        return Objects.requireNonNull(this.liquidBlockRenderer, "Liquid renderer not initialized");
    }

    public BlockStateModel getBlockModel(BlockState state) {
        return this.modelManager.getBlockStateModelSet().get(state);
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.liquidBlockRenderer = new LiquidBlockRenderer(this.sprites);
    }
}

