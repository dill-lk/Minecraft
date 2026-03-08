/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.Direction;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.GrowingPlantBodyBlock;
import net.mayaan.world.level.block.GrowingPlantHeadBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.phys.shapes.VoxelShape;

public class TwistingVinesPlantBlock
extends GrowingPlantBodyBlock {
    public static final MapCodec<TwistingVinesPlantBlock> CODEC = TwistingVinesPlantBlock.simpleCodec(TwistingVinesPlantBlock::new);
    private static final VoxelShape SHAPE = Block.column(8.0, 0.0, 16.0);

    public MapCodec<TwistingVinesPlantBlock> codec() {
        return CODEC;
    }

    public TwistingVinesPlantBlock(BlockBehaviour.Properties properties) {
        super(properties, Direction.UP, SHAPE, false);
    }

    @Override
    protected GrowingPlantHeadBlock getHeadBlock() {
        return (GrowingPlantHeadBlock)Blocks.TWISTING_VINES;
    }
}

