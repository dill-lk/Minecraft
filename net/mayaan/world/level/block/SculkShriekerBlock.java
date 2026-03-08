/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.util.RandomSource;
import net.mayaan.util.valueproviders.ConstantInt;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.ScheduledTickAccess;
import net.mayaan.world.level.block.BaseEntityBlock;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.SimpleWaterloggedBlock;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityTicker;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.entity.SculkShriekerBlockEntity;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.block.state.properties.BooleanProperty;
import net.mayaan.world.level.gameevent.vibrations.VibrationSystem;
import net.mayaan.world.level.material.FluidState;
import net.mayaan.world.level.material.Fluids;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class SculkShriekerBlock
extends BaseEntityBlock
implements SimpleWaterloggedBlock {
    public static final MapCodec<SculkShriekerBlock> CODEC = SculkShriekerBlock.simpleCodec(SculkShriekerBlock::new);
    public static final BooleanProperty SHRIEKING = BlockStateProperties.SHRIEKING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final BooleanProperty CAN_SUMMON = BlockStateProperties.CAN_SUMMON;
    private static final VoxelShape SHAPE_COLLISION = Block.column(16.0, 0.0, 8.0);
    public static final double TOP_Y = SHAPE_COLLISION.max(Direction.Axis.Y);

    public MapCodec<SculkShriekerBlock> codec() {
        return CODEC;
    }

    public SculkShriekerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(SHRIEKING, false)).setValue(WATERLOGGED, false)).setValue(CAN_SUMMON, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHRIEKING);
        builder.add(WATERLOGGED);
        builder.add(CAN_SUMMON);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState onState, Entity entity) {
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            ServerPlayer player = SculkShriekerBlockEntity.tryGetPlayer(entity);
            if (player != null) {
                serverLevel.getBlockEntity(pos, BlockEntityType.SCULK_SHRIEKER).ifPresent(shrieker -> shrieker.tryShriek(serverLevel, player));
            }
        }
        super.stepOn(level, pos, onState, entity);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(SHRIEKING).booleanValue()) {
            level.setBlock(pos, (BlockState)state.setValue(SHRIEKING, false), 3);
            level.getBlockEntity(pos, BlockEntityType.SCULK_SHRIEKER).ifPresent(shrieker -> shrieker.tryRespond(level));
        }
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_COLLISION;
    }

    @Override
    protected VoxelShape getOcclusionShape(BlockState state) {
        return SHAPE_COLLISION;
    }

    @Override
    protected boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new SculkShriekerBlockEntity(worldPosition, blockState);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (state.getValue(WATERLOGGED).booleanValue()) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(WATERLOGGED, context.getLevel().getFluidState(context.getClickedPos()).is(Fluids.WATER));
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        if (state.getValue(WATERLOGGED).booleanValue()) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(state);
    }

    @Override
    protected void spawnAfterBreak(BlockState state, ServerLevel level, BlockPos pos, ItemStack tool, boolean dropExperience) {
        super.spawnAfterBreak(state, level, pos, tool, dropExperience);
        if (dropExperience) {
            this.tryDropExperience(level, pos, tool, ConstantInt.of(5));
        }
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        if (!level.isClientSide()) {
            return BaseEntityBlock.createTickerHelper(type, BlockEntityType.SCULK_SHRIEKER, (innerLevel, pos, state, entity) -> VibrationSystem.Ticker.tick(innerLevel, entity.getVibrationData(), entity.getVibrationUser()));
        }
        return null;
    }
}

