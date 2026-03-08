/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import java.util.function.BiConsumer;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.tags.BlockTags;
import net.mayaan.util.RandomSource;
import net.mayaan.world.Containers;
import net.mayaan.world.attribute.EnvironmentAttributes;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.level.Explosion;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelAccessor;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.ScheduledTickAccess;
import net.mayaan.world.level.ServerExplosion;
import net.mayaan.world.level.block.BaseEntityBlock;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.RotatedPillarBlock;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityTicker;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.entity.CreakingHeartBlockEntity;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.block.state.properties.BooleanProperty;
import net.mayaan.world.level.block.state.properties.CreakingHeartState;
import net.mayaan.world.level.block.state.properties.EnumProperty;
import org.jspecify.annotations.Nullable;

public class CreakingHeartBlock
extends BaseEntityBlock {
    public static final MapCodec<CreakingHeartBlock> CODEC = CreakingHeartBlock.simpleCodec(CreakingHeartBlock::new);
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.AXIS;
    public static final EnumProperty<CreakingHeartState> STATE = BlockStateProperties.CREAKING_HEART_STATE;
    public static final BooleanProperty NATURAL = BlockStateProperties.NATURAL;

    public MapCodec<CreakingHeartBlock> codec() {
        return CODEC;
    }

    protected CreakingHeartBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.defaultBlockState().setValue(AXIS, Direction.Axis.Y)).setValue(STATE, CreakingHeartState.UPROOTED)).setValue(NATURAL, false));
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new CreakingHeartBlockEntity(worldPosition, blockState);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        if (level.isClientSide()) {
            return null;
        }
        if (blockState.getValue(STATE) != CreakingHeartState.UPROOTED) {
            return CreakingHeartBlock.createTickerHelper(type, BlockEntityType.CREAKING_HEART, CreakingHeartBlockEntity::serverTick);
        }
        return null;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (!level.environmentAttributes().getValue(EnvironmentAttributes.CREAKING_ACTIVE, pos).booleanValue()) {
            return;
        }
        if (state.getValue(STATE) == CreakingHeartState.UPROOTED) {
            return;
        }
        if (random.nextInt(16) == 0 && CreakingHeartBlock.isSurroundedByLogs(level, pos)) {
            level.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.CREAKING_HEART_IDLE, SoundSource.BLOCKS, 1.0f, 1.0f, false);
        }
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        ticks.scheduleTick(pos, this, 1);
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockState newState = CreakingHeartBlock.updateState(state, level, pos);
        if (newState != state) {
            level.setBlock(pos, newState, 3);
        }
    }

    private static BlockState updateState(BlockState state, Level level, BlockPos pos) {
        boolean disabled;
        boolean hasLogs = CreakingHeartBlock.hasRequiredLogs(state, level, pos);
        boolean bl = disabled = state.getValue(STATE) == CreakingHeartState.UPROOTED;
        if (hasLogs && disabled) {
            return (BlockState)state.setValue(STATE, level.environmentAttributes().getValue(EnvironmentAttributes.CREAKING_ACTIVE, pos) != false ? CreakingHeartState.AWAKE : CreakingHeartState.DORMANT);
        }
        return state;
    }

    public static boolean hasRequiredLogs(BlockState state, LevelReader level, BlockPos pos) {
        Direction.Axis axis = state.getValue(AXIS);
        for (Direction dir : axis.getDirections()) {
            BlockState neigbour = level.getBlockState(pos.relative(dir));
            if (neigbour.is(BlockTags.PALE_OAK_LOGS) && neigbour.getValue(AXIS) == axis) continue;
            return false;
        }
        return true;
    }

    private static boolean isSurroundedByLogs(LevelAccessor level, BlockPos pos) {
        for (Direction dir : Direction.values()) {
            BlockPos neighbourPos = pos.relative(dir);
            BlockState neighbourState = level.getBlockState(neighbourPos);
            if (neighbourState.is(BlockTags.PALE_OAK_LOGS)) continue;
            return false;
        }
        return true;
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        return CreakingHeartBlock.updateState((BlockState)this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis()), context.getLevel(), context.getClickedPos());
    }

    @Override
    protected BlockState rotate(BlockState state, Rotation rotation) {
        return RotatedPillarBlock.rotatePillar(state, rotation);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS, STATE, NATURAL);
    }

    @Override
    protected void affectNeighborsAfterRemoval(BlockState state, ServerLevel level, BlockPos pos, boolean movedByPiston) {
        Containers.updateNeighboursAfterDestroy(state, level, pos);
    }

    @Override
    protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CreakingHeartBlockEntity) {
            CreakingHeartBlockEntity creakingHeartBlockEntity = (CreakingHeartBlockEntity)blockEntity;
            if (explosion instanceof ServerExplosion) {
                ServerExplosion serverExplosion = (ServerExplosion)explosion;
                if (explosion.getBlockInteraction().shouldAffectBlocklikeEntities()) {
                    creakingHeartBlockEntity.removeProtector(serverExplosion.getDamageSource());
                    LivingEntity livingEntity = explosion.getIndirectSourceEntity();
                    if (livingEntity instanceof Player) {
                        Player player = (Player)livingEntity;
                        if (explosion.getBlockInteraction().shouldAffectBlocklikeEntities()) {
                            this.tryAwardExperience(player, state, level, pos);
                        }
                    }
                }
            }
        }
        super.onExplosionHit(state, level, pos, explosion, onHit);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof CreakingHeartBlockEntity) {
            CreakingHeartBlockEntity creakingHeartBlockEntity = (CreakingHeartBlockEntity)blockEntity;
            creakingHeartBlockEntity.removeProtector(player.damageSources().playerAttack(player));
            this.tryAwardExperience(player, state, level, pos);
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    private void tryAwardExperience(Player player, BlockState state, Level level, BlockPos pos) {
        if (!player.preventsBlockDrops() && !player.isSpectator() && state.getValue(NATURAL).booleanValue() && level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            this.popExperience(serverLevel, pos, level.getRandom().nextIntBetweenInclusive(20, 24));
        }
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        if (state.getValue(STATE) == CreakingHeartState.UPROOTED) {
            return 0;
        }
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (!(blockEntity instanceof CreakingHeartBlockEntity)) {
            return 0;
        }
        CreakingHeartBlockEntity creakingHeartBlockEntity = (CreakingHeartBlockEntity)blockEntity;
        return creakingHeartBlockEntity.getAnalogOutputSignal();
    }
}

