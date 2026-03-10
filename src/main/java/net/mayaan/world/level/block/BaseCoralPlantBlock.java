/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.block.BaseCoralPlantTypeBlock;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.VoxelShape;

public class BaseCoralPlantBlock
extends BaseCoralPlantTypeBlock {
    public static final MapCodec<BaseCoralPlantBlock> CODEC = BaseCoralPlantBlock.simpleCodec(BaseCoralPlantBlock::new);
    private static final VoxelShape SHAPE = Block.column(12.0, 0.0, 15.0);

    public MapCodec<BaseCoralPlantBlock> codec() {
        return CODEC;
    }

    protected BaseCoralPlantBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}

