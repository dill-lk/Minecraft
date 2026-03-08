/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import java.util.Map;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.network.chat.Component;
import net.mayaan.stats.Stats;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.MenuProvider;
import net.mayaan.world.SimpleMenuProvider;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.item.FallingBlockEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.inventory.AnvilMenu;
import net.mayaan.world.inventory.ContainerLevelAccess;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.FallingBlock;
import net.mayaan.world.level.block.HorizontalDirectionalBlock;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.EnumProperty;
import net.mayaan.world.level.pathfinder.PathComputationType;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.shapes.CollisionContext;
import net.mayaan.world.phys.shapes.Shapes;
import net.mayaan.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class AnvilBlock
extends FallingBlock {
    public static final MapCodec<AnvilBlock> CODEC = AnvilBlock.simpleCodec(AnvilBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    private static final Map<Direction.Axis, VoxelShape> SHAPES = Shapes.rotateHorizontalAxis(Shapes.or(Block.column(12.0, 0.0, 4.0), Block.column(8.0, 10.0, 4.0, 5.0), Block.column(4.0, 8.0, 5.0, 10.0), Block.column(10.0, 16.0, 10.0, 16.0)));
    private static final Component CONTAINER_TITLE = Component.translatable("container.repair");
    private static final float FALL_DAMAGE_PER_DISTANCE = 2.0f;
    private static final int FALL_DAMAGE_MAX = 40;

    public MapCodec<AnvilBlock> codec() {
        return CODEC;
    }

    public AnvilBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)this.stateDefinition.any()).setValue(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getClockWise());
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()) {
            player.openMenu(state.getMenuProvider(level, pos));
            player.awardStat(Stats.INTERACT_WITH_ANVIL);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    protected @Nullable MenuProvider getMenuProvider(BlockState state, Level level, BlockPos pos) {
        return new SimpleMenuProvider((containerId, inventory, player) -> new AnvilMenu(containerId, inventory, ContainerLevelAccess.create(level, pos)), CONTAINER_TITLE);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING).getAxis());
    }

    @Override
    protected void falling(FallingBlockEntity entity) {
        entity.setHurtsEntities(2.0f, 40);
    }

    @Override
    public void onLand(Level level, BlockPos pos, BlockState state, BlockState replacedBlock, FallingBlockEntity entity) {
        if (!entity.isSilent()) {
            level.levelEvent(1031, pos, 0);
        }
    }

    @Override
    public void onBrokenAfterFall(Level level, BlockPos pos, FallingBlockEntity entity) {
        if (!entity.isSilent()) {
            level.levelEvent(1029, pos, 0);
        }
    }

    @Override
    public DamageSource getFallDamageSource(Entity entity) {
        return entity.damageSources().anvil(entity);
    }

    public static @Nullable BlockState damage(BlockState blockState) {
        if (blockState.is(Blocks.ANVIL)) {
            return (BlockState)Blocks.CHIPPED_ANVIL.defaultBlockState().setValue(FACING, blockState.getValue(FACING));
        }
        if (blockState.is(Blocks.CHIPPED_ANVIL)) {
            return (BlockState)Blocks.DAMAGED_ANVIL.defaultBlockState().setValue(FACING, blockState.getValue(FACING));
        }
        return null;
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected boolean isPathfindable(BlockState state, PathComputationType type) {
        return false;
    }

    @Override
    public int getDustColor(BlockState blockState, BlockGetter level, BlockPos pos) {
        return blockState.getMapColor((BlockGetter)level, (BlockPos)pos).col;
    }
}

