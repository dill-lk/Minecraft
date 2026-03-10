/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.App
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.MapCodec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 */
package net.mayaan.world.level.block;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Map;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.AbstractSkullBlock;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.HorizontalDirectionalBlock;
import net.mayaan.world.level.block.Mirror;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.block.SkullBlock;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.EnumProperty;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.Shapes;
import net.mayaan.world.phys.shapes.VoxelShape;

public class WallSkullBlock
extends AbstractSkullBlock {
    public static final MapCodec<WallSkullBlock> CODEC = RecordCodecBuilder.mapCodec(i -> i.group((App)SkullBlock.Type.CODEC.fieldOf("kind").forGetter(AbstractSkullBlock::getType), WallSkullBlock.propertiesCodec()).apply((Applicative)i, WallSkullBlock::new));
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    private static final Map<Direction, VoxelShape> SHAPES = Shapes.rotateHorizontal(Block.boxZ(8.0, 8.0, 16.0));

    public MapCodec<? extends WallSkullBlock> codec() {
        return CODEC;
    }

    protected WallSkullBlock(SkullBlock.Type type, BlockBehaviour.Properties properties) {
        super(type, properties);
        this.registerDefaultState((BlockState)this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction[] directions;
        BlockState state = super.getStateForPlacement(context);
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        for (Direction direction : directions = context.getNearestLookingDirections()) {
            if (!direction.getAxis().isHorizontal()) continue;
            Direction facing = direction.getOpposite();
            state = (BlockState)state.setValue(FACING, facing);
            if (level.getBlockState(pos.relative(direction)).canBeReplaced(context)) continue;
            return state;
        }
        return null;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
}

