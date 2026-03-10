/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.RandomSource;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.BonemealableBlock;
import net.mayaan.world.level.block.DoublePlantBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;

public class TallFlowerBlock
extends DoublePlantBlock
implements BonemealableBlock {
    public static final MapCodec<TallFlowerBlock> CODEC = TallFlowerBlock.simpleCodec(TallFlowerBlock::new);

    public MapCodec<TallFlowerBlock> codec() {
        return CODEC;
    }

    public TallFlowerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        TallFlowerBlock.popResource((Level)level, pos, new ItemStack(this));
    }
}

