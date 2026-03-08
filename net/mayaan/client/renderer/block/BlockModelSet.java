/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 */
package net.mayaan.client.renderer.block;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.mayaan.client.color.block.BlockColors;
import net.mayaan.client.color.block.BlockTintSource;
import net.mayaan.client.renderer.block.BlockStateModelSet;
import net.mayaan.client.renderer.block.dispatch.BlockStateModel;
import net.mayaan.client.renderer.block.model.BlockModel;
import net.mayaan.client.renderer.block.model.BlockStateModelWrapper;
import net.mayaan.world.level.block.state.BlockState;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

public class BlockModelSet {
    private static final Matrix4f IDENTITY = new Matrix4f();
    private final BlockStateModelSet fallback;
    private final BlockColors blockColors;
    private final Map<BlockState, BlockModel> blockModelByStateCache = new HashMap<BlockState, BlockModel>();

    public BlockModelSet(BlockStateModelSet fallback, Map<BlockState, BlockModel> blockModelByState, BlockColors blockColors) {
        this.fallback = fallback;
        this.blockModelByStateCache.putAll(blockModelByState);
        this.blockColors = blockColors;
    }

    public BlockModel get(BlockState blockState) {
        return this.blockModelByStateCache.computeIfAbsent(blockState, this::createFallbackModel);
    }

    private BlockModel createFallbackModel(BlockState blockState) {
        List<BlockTintSource> tints = this.blockColors.getTintSources(blockState);
        BlockStateModel plainModel = this.fallback.get(blockState);
        return new BlockStateModelWrapper(plainModel, tints, (Matrix4fc)IDENTITY);
    }
}

