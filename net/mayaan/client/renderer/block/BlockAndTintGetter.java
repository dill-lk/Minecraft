/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.block;

import net.mayaan.core.BlockPos;
import net.mayaan.world.level.BlockAndLightGetter;
import net.mayaan.world.level.CardinalLighting;
import net.mayaan.world.level.ColorResolver;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.lighting.LevelLightEngine;
import net.mayaan.world.level.material.FluidState;
import net.mayaan.world.level.material.Fluids;
import org.jspecify.annotations.Nullable;

public interface BlockAndTintGetter
extends BlockAndLightGetter {
    public static final BlockAndTintGetter EMPTY = new BlockAndTintGetter(){

        @Override
        public CardinalLighting cardinalLighting() {
            return CardinalLighting.DEFAULT;
        }

        @Override
        public LevelLightEngine getLightEngine() {
            return LevelLightEngine.EMPTY;
        }

        @Override
        public int getBlockTint(BlockPos pos, ColorResolver color) {
            return -1;
        }

        @Override
        public @Nullable BlockEntity getBlockEntity(BlockPos pos) {
            return null;
        }

        @Override
        public BlockState getBlockState(BlockPos pos) {
            return Blocks.AIR.defaultBlockState();
        }

        @Override
        public FluidState getFluidState(BlockPos pos) {
            return Fluids.EMPTY.defaultFluidState();
        }

        @Override
        public int getHeight() {
            return 0;
        }

        @Override
        public int getMinY() {
            return 0;
        }
    };

    public CardinalLighting cardinalLighting();

    public int getBlockTint(BlockPos var1, ColorResolver var2);
}

