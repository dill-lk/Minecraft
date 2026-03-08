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
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.RenderShape;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.VoxelShape;

public class StructureVoidBlock
extends Block {
    public static final MapCodec<StructureVoidBlock> CODEC = StructureVoidBlock.simpleCodec(StructureVoidBlock::new);
    private static final VoxelShape SHAPE = Block.cube(6.0);

    public MapCodec<StructureVoidBlock> codec() {
        return CODEC;
    }

    protected StructureVoidBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected float getShadeBrightness(BlockState state, BlockGetter level, BlockPos pos) {
        return 1.0f;
    }
}

