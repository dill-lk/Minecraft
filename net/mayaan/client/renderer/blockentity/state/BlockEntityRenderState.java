/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.blockentity.state;

import net.mayaan.CrashReportCategory;
import net.mayaan.client.renderer.LevelRenderer;
import net.mayaan.core.BlockPos;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class BlockEntityRenderState {
    public BlockPos blockPos = BlockPos.ZERO;
    private BlockState blockState = Blocks.AIR.defaultBlockState();
    public BlockEntityType<?> blockEntityType = BlockEntityType.TEST_BLOCK;
    public int lightCoords;
    public  @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress;

    public static void extractBase(BlockEntity blockEntity, BlockEntityRenderState state,  @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        state.blockPos = blockEntity.getBlockPos();
        state.blockState = blockEntity.getBlockState();
        state.blockEntityType = blockEntity.getType();
        state.lightCoords = blockEntity.getLevel() != null ? LevelRenderer.getLightCoords(blockEntity.getLevel(), blockEntity.getBlockPos()) : 0xF000F0;
        state.breakProgress = breakProgress;
    }

    public void fillCrashReportCategory(CrashReportCategory category) {
        category.setDetail("BlockEntityRenderState", this.getClass().getCanonicalName());
        category.setDetail("Position", this.blockPos);
        category.setDetail("Block state", this.blockState::toString);
    }
}

