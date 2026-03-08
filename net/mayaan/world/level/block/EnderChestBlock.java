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
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.network.chat.Component;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.stats.Stats;
import net.mayaan.util.RandomSource;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.SimpleMenuProvider;
import net.mayaan.world.entity.monster.piglin.PiglinAi;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.inventory.ChestMenu;
import net.mayaan.world.inventory.PlayerEnderChestContainer;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.ScheduledTickAccess;
import net.mayaan.world.level.block.AbstractChestBlock;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.DoubleBlockCombiner;
import net.mayaan.world.level.block.HorizontalDirectionalBlock;
import net.mayaan.world.level.block.Mirror;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.block.SimpleWaterloggedBlock;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityTicker;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.entity.ChestBlockEntity;
import net.mayaan.world.level.block.entity.EnderChestBlockEntity;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.block.state.properties.BooleanProperty;
import net.mayaan.world.level.block.state.properties.EnumProperty;
import net.mayaan.world.level.material.FluidState;
import net.mayaan.world.level.material.Fluids;
import net.mayaan.world.level.pathfinder.PathComputationType;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class EnderChestBlock
extends AbstractChestBlock<EnderChestBlockEntity>
implements SimpleWaterloggedBlock {
    public static final MapCodec<EnderChestBlock> CODEC = EnderChestBlock.simpleCodec(EnderChestBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    private static final VoxelShape SHAPE = Block.column(14.0, 0.0, 14.0);
    private static final Component CONTAINER_TITLE = Component.translatable("container.enderchest");

    @Override
    public MapCodec<EnderChestBlock> codec() {
        return CODEC;
    }

    protected EnderChestBlock(BlockBehaviour.Properties properties) {
        super(properties, () -> BlockEntityType.ENDER_CHEST);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH)).setValue(WATERLOGGED, false));
    }

    @Override
    public DoubleBlockCombiner.NeighborCombineResult<? extends ChestBlockEntity> combine(BlockState state, Level level, BlockPos pos, boolean ignoreBeingBlocked) {
        return DoubleBlockCombiner.Combiner::acceptNone;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        FluidState replacedFluidState = context.getLevel().getFluidState(context.getClickedPos());
        return (BlockState)((BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite())).setValue(WATERLOGGED, replacedFluidState.is(Fluids.WATER));
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        PlayerEnderChestContainer container = player.getEnderChestInventory();
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (container == null || !(blockEntity instanceof EnderChestBlockEntity)) {
            return InteractionResult.SUCCESS;
        }
        EnderChestBlockEntity enderChest = (EnderChestBlockEntity)blockEntity;
        BlockPos above = pos.above();
        if (level.getBlockState(above).isRedstoneConductor(level, above)) {
            return InteractionResult.SUCCESS;
        }
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            container.setActiveChest(enderChest);
            player.openMenu(new SimpleMenuProvider((containerId, inventory, p) -> ChestMenu.threeRows(containerId, inventory, container), CONTAINER_TITLE));
            player.awardStat(Stats.OPEN_ENDERCHEST);
            PiglinAi.angerNearbyPiglins(serverLevel, player, true);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new EnderChestBlockEntity(worldPosition, blockState);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level.isClientSide() ? EnderChestBlock.createTickerHelper(type, BlockEntityType.ENDER_CHEST, EnderChestBlockEntity::lidAnimateTick) : null;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        for (int i = 0; i < 3; ++i) {
            int flipX = random.nextInt(2) * 2 - 1;
            int flipZ = random.nextInt(2) * 2 - 1;
            double x = (double)pos.getX() + 0.5 + 0.25 * (double)flipX;
            double y = (float)pos.getY() + random.nextFloat();
            double z = (double)pos.getZ() + 0.5 + 0.25 * (double)flipZ;
            double xa = random.nextFloat() * (float)flipX;
            double ya = ((double)random.nextFloat() - 0.5) * 0.125;
            double za = random.nextFloat() * (float)flipZ;
            level.addParticle(ParticleTypes.PORTAL, x, y, z, xa, ya, za);
        }
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
        builder.add(FACING, WATERLOGGED);
    }

    @Override
    protected FluidState getFluidState(BlockState state) {
        if (state.getValue(WATERLOGGED).booleanValue()) {
            return Fluids.WATER.getSource(false);
        }
        return super.getFluidState(state);
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        if (state.getValue(WATERLOGGED).booleanValue()) {
            ticks.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof EnderChestBlockEntity) {
            ((EnderChestBlockEntity)blockEntity).recheckOpen();
        }
    }
}

