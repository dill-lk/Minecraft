/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.HolderLookup;
import net.mayaan.core.registries.Registries;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.tags.FluidTags;
import net.mayaan.util.RandomSource;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.SnowLayerBlock;
import net.mayaan.world.level.block.SnowyBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.lighting.LightEngine;

public abstract class SpreadingSnowyBlock
extends SnowyBlock {
    private final ResourceKey<Block> baseBlock;

    protected SpreadingSnowyBlock(BlockBehaviour.Properties properties, ResourceKey<Block> baseBlock) {
        super(properties);
        this.baseBlock = baseBlock;
    }

    protected abstract MapCodec<? extends SpreadingSnowyBlock> codec();

    private static boolean canStayAlive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos above = pos.above();
        BlockState aboveState = level.getBlockState(above);
        if (aboveState.is(Blocks.SNOW) && aboveState.getValue(SnowLayerBlock.LAYERS) == 1) {
            return true;
        }
        if (aboveState.getFluidState().isFull()) {
            return false;
        }
        int lightBlockInto = LightEngine.getLightBlockInto(state, aboveState, Direction.UP, aboveState.getLightDampening());
        return lightBlockInto < 15;
    }

    private static boolean canPropagate(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos above = pos.above();
        return SpreadingSnowyBlock.canStayAlive(state, level, pos) && !level.getFluidState(above).is(FluidTags.WATER);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        HolderLookup.RegistryLookup blocks = level.registryAccess().lookupOrThrow(Registries.BLOCK);
        Optional<Block> baseBlock = blocks.getOptional(this.baseBlock);
        if (baseBlock.isEmpty()) {
            return;
        }
        if (!SpreadingSnowyBlock.canStayAlive(state, level, pos)) {
            level.setBlockAndUpdate(pos, baseBlock.get().defaultBlockState());
            return;
        }
        if (level.getMaxLocalRawBrightness(pos.above()) >= 9) {
            BlockState defaultBlockState = this.defaultBlockState();
            for (int i = 0; i < 4; ++i) {
                BlockPos testPos = pos.offset(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
                if (!level.getBlockState(testPos).is(baseBlock.get()) || !SpreadingSnowyBlock.canPropagate(defaultBlockState, level, testPos)) continue;
                level.setBlockAndUpdate(testPos, (BlockState)defaultBlockState.setValue(SNOWY, SpreadingSnowyBlock.isSnowySetting(level.getBlockState(testPos.above()))));
            }
        }
    }
}

