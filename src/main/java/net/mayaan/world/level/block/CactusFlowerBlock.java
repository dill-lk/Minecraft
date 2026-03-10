/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.tags.BlockTags;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.SupportType;
import net.mayaan.world.level.block.VegetationBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.VoxelShape;

public class CactusFlowerBlock
extends VegetationBlock {
    public static final MapCodec<CactusFlowerBlock> CODEC = CactusFlowerBlock.simpleCodec(CactusFlowerBlock::new);
    private static final VoxelShape SHAPE = Block.column(14.0, 0.0, 12.0);

    public MapCodec<? extends CactusFlowerBlock> codec() {
        return CODEC;
    }

    public CactusFlowerBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        BlockState blockBelow = level.getBlockState(pos);
        return blockBelow.is(BlockTags.SUPPORT_OVERRIDE_CACTUS_FLOWER) || blockBelow.isFaceSturdy(level, pos, Direction.UP, SupportType.CENTER);
    }
}

