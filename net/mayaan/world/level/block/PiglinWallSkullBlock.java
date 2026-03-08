/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import java.util.Map;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.SkullBlock;
import net.mayaan.world.level.block.WallSkullBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.Shapes;
import net.mayaan.world.phys.shapes.VoxelShape;

public class PiglinWallSkullBlock
extends WallSkullBlock {
    public static final MapCodec<PiglinWallSkullBlock> CODEC = PiglinWallSkullBlock.simpleCodec(PiglinWallSkullBlock::new);
    private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(10.0, 8.0, 8.0, 16.0));

    public MapCodec<PiglinWallSkullBlock> codec() {
        return CODEC;
    }

    public PiglinWallSkullBlock(BlockBehaviour.Properties properties) {
        super(SkullBlock.Types.PIGLIN, properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }
}

