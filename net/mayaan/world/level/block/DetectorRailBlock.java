/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.function.Predicate;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.RandomSource;
import net.mayaan.world.Container;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntitySelector;
import net.mayaan.world.entity.InsideBlockEffectApplier;
import net.mayaan.world.entity.vehicle.minecart.AbstractMinecart;
import net.mayaan.world.entity.vehicle.minecart.MinecartCommandBlock;
import net.mayaan.world.inventory.AbstractContainerMenu;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.BaseRailBlock;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Mirror;
import net.mayaan.world.level.block.RailState;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.block.state.properties.BooleanProperty;
import net.mayaan.world.level.block.state.properties.EnumProperty;
import net.mayaan.world.level.block.state.properties.Property;
import net.mayaan.world.level.block.state.properties.RailShape;
import net.mayaan.world.phys.AABB;

public class DetectorRailBlock
extends BaseRailBlock {
    public static final MapCodec<DetectorRailBlock> CODEC = DetectorRailBlock.simpleCodec(DetectorRailBlock::new);
    public static final EnumProperty<RailShape> SHAPE = BlockStateProperties.RAIL_SHAPE_STRAIGHT;
    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    private static final int PRESSED_CHECK_PERIOD = 20;

    public MapCodec<DetectorRailBlock> codec() {
        return CODEC;
    }

    public DetectorRailBlock(BlockBehaviour.Properties properties) {
        super(true, properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(POWERED, false)).setValue(SHAPE, RailShape.NORTH_SOUTH)).setValue(WATERLOGGED, false));
    }

    @Override
    protected boolean isSignalSource(BlockState state) {
        return true;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity, InsideBlockEffectApplier effectApplier, boolean isPrecise) {
        if (level.isClientSide()) {
            return;
        }
        if (state.getValue(POWERED).booleanValue()) {
            return;
        }
        this.checkPressed(level, pos, state);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!state.getValue(POWERED).booleanValue()) {
            return;
        }
        this.checkPressed(level, pos, state);
    }

    @Override
    protected int getSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return state.getValue(POWERED) != false ? 15 : 0;
    }

    @Override
    protected int getDirectSignal(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        if (!state.getValue(POWERED).booleanValue()) {
            return 0;
        }
        return direction == Direction.UP ? 15 : 0;
    }

    private void checkPressed(Level level, BlockPos pos, BlockState state) {
        BlockState newState;
        if (!this.canSurvive(state, level, pos)) {
            return;
        }
        boolean wasPressed = state.getValue(POWERED);
        boolean shouldBePressed = false;
        List<AbstractMinecart> entities = this.getInteractingMinecartOfType(level, pos, AbstractMinecart.class, e -> true);
        if (!entities.isEmpty()) {
            shouldBePressed = true;
        }
        if (shouldBePressed && !wasPressed) {
            newState = (BlockState)state.setValue(POWERED, true);
            level.setBlock(pos, newState, 3);
            this.updatePowerToConnected(level, pos, newState, true);
            level.updateNeighborsAt(pos, this);
            level.updateNeighborsAt(pos.below(), this);
            level.setBlocksDirty(pos, state, newState);
        }
        if (!shouldBePressed && wasPressed) {
            newState = (BlockState)state.setValue(POWERED, false);
            level.setBlock(pos, newState, 3);
            this.updatePowerToConnected(level, pos, newState, false);
            level.updateNeighborsAt(pos, this);
            level.updateNeighborsAt(pos.below(), this);
            level.setBlocksDirty(pos, state, newState);
        }
        if (shouldBePressed) {
            level.scheduleTick(pos, this, 20);
        }
        level.updateNeighbourForOutputSignal(pos, this);
    }

    protected void updatePowerToConnected(Level level, BlockPos pos, BlockState state, boolean powered) {
        RailState rail = new RailState(level, pos, state);
        List<BlockPos> connections = rail.getConnections();
        for (BlockPos connectionPos : connections) {
            BlockState connectionState = level.getBlockState(connectionPos);
            level.neighborChanged(connectionState, connectionPos, connectionState.getBlock(), null, false);
        }
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        if (oldState.is(state.getBlock())) {
            return;
        }
        BlockState updatedState = this.updateState(state, level, pos, movedByPiston);
        this.checkPressed(level, pos, updatedState);
    }

    @Override
    public Property<RailShape> getShapeProperty() {
        return SHAPE;
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        if (state.getValue(POWERED).booleanValue()) {
            List<MinecartCommandBlock> commandBlocks = this.getInteractingMinecartOfType(level, pos, MinecartCommandBlock.class, e -> true);
            if (!commandBlocks.isEmpty()) {
                return commandBlocks.get(0).getCommandBlock().getSuccessCount();
            }
            List<AbstractMinecart> entities = this.getInteractingMinecartOfType(level, pos, AbstractMinecart.class, EntitySelector.CONTAINER_ENTITY_SELECTOR);
            if (!entities.isEmpty()) {
                return AbstractContainerMenu.getRedstoneSignalFromContainer((Container)((Object)entities.get(0)));
            }
        }
        return 0;
    }

    private <T extends AbstractMinecart> List<T> getInteractingMinecartOfType(Level level, BlockPos pos, Class<T> type, Predicate<Entity> containerEntitySelector) {
        return level.getEntitiesOfClass(type, this.getSearchBB(pos), containerEntitySelector);
    }

    private AABB getSearchBB(BlockPos pos) {
        double b = 0.2;
        return new AABB((double)pos.getX() + 0.2, pos.getY(), (double)pos.getZ() + 0.2, (double)(pos.getX() + 1) - 0.2, (double)(pos.getY() + 1) - 0.2, (double)(pos.getZ() + 1) - 0.2);
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        RailShape currentShape = state.getValue(SHAPE);
        RailShape newShape = this.rotate(currentShape, rotation);
        return (BlockState)state.setValue(SHAPE, newShape);
    }

    @Override
    protected BlockState mirror(BlockState state, Mirror mirror) {
        RailShape currentShape = state.getValue(SHAPE);
        RailShape newShape = this.mirror(currentShape, mirror);
        return (BlockState)state.setValue(SHAPE, newShape);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE, POWERED, WATERLOGGED);
    }
}

