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
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.BonemealableBlock;
import net.mayaan.world.level.block.DoublePlantBlock;
import net.mayaan.world.level.block.VegetationBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.VoxelShape;

public class TallGrassBlock
extends VegetationBlock
implements BonemealableBlock {
    public static final MapCodec<TallGrassBlock> CODEC = TallGrassBlock.simpleCodec(TallGrassBlock::new);
    private static final VoxelShape SHAPE = Block.column(12.0, 0.0, 13.0);

    public MapCodec<TallGrassBlock> codec() {
        return CODEC;
    }

    protected TallGrassBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state) {
        return TallGrassBlock.getGrownBlock(state).defaultBlockState().canSurvive(level, pos) && level.isEmptyBlock(pos.above()) && level.isInsideBuildHeight(pos.above());
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState state) {
        DoublePlantBlock.placeAt(level, TallGrassBlock.getGrownBlock(state).defaultBlockState(), pos, 2);
    }

    private static DoublePlantBlock getGrownBlock(BlockState state) {
        return (DoublePlantBlock)(state.is(Blocks.FERN) ? Blocks.LARGE_FERN : Blocks.TALL_GRASS);
    }
}

