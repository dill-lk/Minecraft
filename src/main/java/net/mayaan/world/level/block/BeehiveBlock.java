/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.level.block;

import com.mojang.serialization.MapCodec;
import java.util.List;
import java.util.function.BiConsumer;
import net.mayaan.advancements.CriteriaTriggers;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.component.DataComponents;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.sounds.SoundSource;
import net.mayaan.stats.Stats;
import net.mayaan.tags.BlockTags;
import net.mayaan.tags.EnchantmentTags;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import net.mayaan.util.Util;
import net.mayaan.world.Containers;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.animal.bee.Bee;
import net.mayaan.world.entity.boss.wither.WitherBoss;
import net.mayaan.world.entity.item.ItemEntity;
import net.mayaan.world.entity.item.PrimedTnt;
import net.mayaan.world.entity.monster.Creeper;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.entity.projectile.hurtingprojectile.WitherSkull;
import net.mayaan.world.entity.vehicle.minecart.MinecartTNT;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.item.component.BlockItemStateProperties;
import net.mayaan.world.item.context.BlockPlaceContext;
import net.mayaan.world.item.enchantment.EnchantmentHelper;
import net.mayaan.world.level.Explosion;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LevelReader;
import net.mayaan.world.level.ScheduledTickAccess;
import net.mayaan.world.level.block.BaseEntityBlock;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.CampfireBlock;
import net.mayaan.world.level.block.FireBlock;
import net.mayaan.world.level.block.HorizontalDirectionalBlock;
import net.mayaan.world.level.block.Mirror;
import net.mayaan.world.level.block.Rotation;
import net.mayaan.world.level.block.entity.BeehiveBlockEntity;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityTicker;
import net.mayaan.world.level.block.entity.BlockEntityType;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.block.state.StateDefinition;
import net.mayaan.world.level.block.state.properties.BlockStateProperties;
import net.mayaan.world.level.block.state.properties.EnumProperty;
import net.mayaan.world.level.block.state.properties.IntegerProperty;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.level.gamerules.GameRules;
import net.mayaan.world.level.storage.loot.BuiltInLootTables;
import net.mayaan.world.level.storage.loot.LootParams;
import net.mayaan.world.level.storage.loot.parameters.LootContextParams;
import net.mayaan.world.phys.AABB;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class BeehiveBlock
extends BaseEntityBlock {
    public static final MapCodec<BeehiveBlock> CODEC = BeehiveBlock.simpleCodec(BeehiveBlock::new);
    public static final EnumProperty<Direction> FACING = HorizontalDirectionalBlock.FACING;
    public static final IntegerProperty HONEY_LEVEL = BlockStateProperties.LEVEL_HONEY;
    public static final int MAX_HONEY_LEVELS = 5;

    public MapCodec<BeehiveBlock> codec() {
        return CODEC;
    }

    public BeehiveBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState((BlockState)((BlockState)((BlockState)this.stateDefinition.any()).setValue(HONEY_LEVEL, 0)).setValue(FACING, Direction.NORTH));
    }

    @Override
    protected boolean hasAnalogOutputSignal(BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(BlockState state, Level level, BlockPos pos, Direction direction) {
        return state.getValue(HONEY_LEVEL);
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack destroyedWith) {
        super.playerDestroy(level, player, pos, state, blockEntity, destroyedWith);
        if (!level.isClientSide() && blockEntity instanceof BeehiveBlockEntity) {
            BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
            if (!EnchantmentHelper.hasTag(destroyedWith, EnchantmentTags.PREVENTS_BEE_SPAWNS_WHEN_MINING)) {
                beehiveBlockEntity.emptyAllLivingFromHive(player, state, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
                Containers.updateNeighboursAfterDestroy(state, level, pos);
                this.angerNearbyBees(level, pos);
            }
            CriteriaTriggers.BEE_NEST_DESTROYED.trigger((ServerPlayer)player, state, destroyedWith, beehiveBlockEntity.getOccupantCount());
        }
    }

    @Override
    protected void onExplosionHit(BlockState state, ServerLevel level, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> onHit) {
        super.onExplosionHit(state, level, pos, explosion, onHit);
        this.angerNearbyBees(level, pos);
    }

    private void angerNearbyBees(Level level, BlockPos pos) {
        AABB areaAroundBeehive = new AABB(pos).inflate(8.0, 6.0, 8.0);
        List<Bee> beesToAnger = level.getEntitiesOfClass(Bee.class, areaAroundBeehive);
        if (!beesToAnger.isEmpty()) {
            List<Player> playersToBeAngryAt = level.getEntitiesOfClass(Player.class, areaAroundBeehive);
            if (playersToBeAngryAt.isEmpty()) {
                return;
            }
            for (Bee bee : beesToAnger) {
                if (bee.getTarget() != null) continue;
                Player angerTarget = Util.getRandom(playersToBeAngryAt, level.getRandom());
                bee.setTarget(angerTarget);
            }
        }
    }

    public static void dropHoneycomb(ServerLevel level, ItemStack tool, BlockState blockState, @Nullable BlockEntity blockEntity, @Nullable Entity entity, BlockPos pos) {
        BeehiveBlock.dropFromBlockInteractLootTable(level, BuiltInLootTables.HARVEST_BEEHIVE, blockState, blockEntity, tool, entity, (serverLevel, stack) -> BeehiveBlock.popResource((Level)serverLevel, pos, stack));
    }

    /*
     * Unable to fully structure code
     */
    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        block11: {
            honeyLevel = state.getValue(BeehiveBlock.HONEY_LEVEL);
            hiveEmptied = false;
            if (honeyLevel < 5) break block11;
            item = itemStack.getItem();
            if (!(level instanceof ServerLevel)) ** GOTO lbl-1000
            serverLevel = (ServerLevel)level;
            if (itemStack.is(Items.SHEARS)) {
                BeehiveBlock.dropHoneycomb(serverLevel, itemStack, state, level.getBlockEntity(pos), player, pos);
                level.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.BEEHIVE_SHEAR, SoundSource.BLOCKS, 1.0f, 1.0f);
                itemStack.hurtAndBreak(1, (LivingEntity)player, hand.asEquipmentSlot());
                hiveEmptied = true;
                level.gameEvent((Entity)player, GameEvent.SHEAR, pos);
            } else if (itemStack.is(Items.GLASS_BOTTLE)) {
                itemStack.shrink(1);
                level.playSound((Entity)player, player.getX(), player.getY(), player.getZ(), SoundEvents.BOTTLE_FILL, SoundSource.BLOCKS, 1.0f, 1.0f);
                if (itemStack.isEmpty()) {
                    player.setItemInHand(hand, new ItemStack(Items.HONEY_BOTTLE));
                } else if (!player.getInventory().add(new ItemStack(Items.HONEY_BOTTLE))) {
                    player.drop(new ItemStack(Items.HONEY_BOTTLE), false);
                }
                hiveEmptied = true;
                level.gameEvent((Entity)player, GameEvent.FLUID_PICKUP, pos);
            }
            if (!level.isClientSide() && hiveEmptied) {
                player.awardStat(Stats.ITEM_USED.get(item));
            }
        }
        if (hiveEmptied) {
            if (!CampfireBlock.isSmokeyPos(level, pos)) {
                if (this.hiveContainsBees(level, pos)) {
                    this.angerNearbyBees(level, pos);
                }
                this.releaseBeesAndResetHoneyLevel(level, state, pos, player, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
            } else {
                this.resetHoneyLevel(level, state, pos);
            }
            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(itemStack, state, level, pos, player, hand, hitResult);
    }

    private boolean hiveContainsBees(Level level, BlockPos pos) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BeehiveBlockEntity) {
            BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
            return !beehiveBlockEntity.isEmpty();
        }
        return false;
    }

    public void releaseBeesAndResetHoneyLevel(Level level, BlockState state, BlockPos pos, @Nullable Player player, BeehiveBlockEntity.BeeReleaseStatus beeReleaseStatus) {
        this.resetHoneyLevel(level, state, pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BeehiveBlockEntity) {
            BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
            beehiveBlockEntity.emptyAllLivingFromHive(player, state, beeReleaseStatus);
        }
    }

    public void resetHoneyLevel(Level level, BlockState state, BlockPos pos) {
        level.setBlock(pos, (BlockState)state.setValue(HONEY_LEVEL, 0), 3);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(HONEY_LEVEL) >= 5) {
            for (int i = 0; i < random.nextInt(1) + 1; ++i) {
                this.trySpawnDripParticles(level, pos, state);
            }
        }
    }

    private void trySpawnDripParticles(Level level, BlockPos pos, BlockState state) {
        if (!state.getFluidState().isEmpty() || level.getRandom().nextFloat() < 0.3f) {
            return;
        }
        VoxelShape collisionShape = state.getCollisionShape(level, pos);
        double topSideHeight = collisionShape.max(Direction.Axis.Y);
        if (topSideHeight >= 1.0 && !state.is(BlockTags.IMPERMEABLE)) {
            double bottomSideHeight = collisionShape.min(Direction.Axis.Y);
            if (bottomSideHeight > 0.0) {
                this.spawnParticle(level, pos, collisionShape, (double)pos.getY() + bottomSideHeight - 0.05);
            } else {
                BlockPos below = pos.below();
                BlockState belowState = level.getBlockState(below);
                VoxelShape belowShape = belowState.getCollisionShape(level, below);
                double belowTopSideHeight = belowShape.max(Direction.Axis.Y);
                if ((belowTopSideHeight < 1.0 || !belowState.isCollisionShapeFullBlock(level, below)) && belowState.getFluidState().isEmpty()) {
                    this.spawnParticle(level, pos, collisionShape, (double)pos.getY() - 0.05);
                }
            }
        }
    }

    private void spawnParticle(Level level, BlockPos pos, VoxelShape dripShape, double height) {
        this.spawnFluidParticle(level, (double)pos.getX() + dripShape.min(Direction.Axis.X), (double)pos.getX() + dripShape.max(Direction.Axis.X), (double)pos.getZ() + dripShape.min(Direction.Axis.Z), (double)pos.getZ() + dripShape.max(Direction.Axis.Z), height);
    }

    private void spawnFluidParticle(Level level, double x1, double x2, double z1, double z2, double y) {
        level.addParticle(ParticleTypes.DRIPPING_HONEY, Mth.lerp(level.getRandom().nextDouble(), x1, x2), y, Mth.lerp(level.getRandom().nextDouble(), z1, z2), 0.0, 0.0, 0.0);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState)this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HONEY_LEVEL, FACING);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos worldPosition, BlockState blockState) {
        return new BeehiveBlockEntity(worldPosition, blockState);
    }

    @Override
    public <T extends BlockEntity> @Nullable BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return level.isClientSide() ? null : BeehiveBlock.createTickerHelper(type, BlockEntityType.BEEHIVE, BeehiveBlockEntity::serverTick);
    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (level instanceof ServerLevel) {
            BlockEntity blockEntity;
            ServerLevel serverLevel = (ServerLevel)level;
            if (player.preventsBlockDrops() && serverLevel.getGameRules().get(GameRules.BLOCK_DROPS).booleanValue() && (blockEntity = level.getBlockEntity(pos)) instanceof BeehiveBlockEntity) {
                boolean hasBees;
                BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
                int honeyLevel = state.getValue(HONEY_LEVEL);
                boolean bl = hasBees = !beehiveBlockEntity.isEmpty();
                if (hasBees || honeyLevel > 0) {
                    ItemStack itemStack = new ItemStack(this);
                    itemStack.applyComponents(beehiveBlockEntity.collectComponents());
                    itemStack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(HONEY_LEVEL, honeyLevel));
                    ItemEntity entity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), itemStack);
                    entity.setDefaultPickUpDelay();
                    level.addFreshEntity(entity);
                }
            }
        }
        return super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    protected List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        BlockEntity blockEntity;
        Entity entity = params.getOptionalParameter(LootContextParams.THIS_ENTITY);
        if ((entity instanceof PrimedTnt || entity instanceof Creeper || entity instanceof WitherSkull || entity instanceof WitherBoss || entity instanceof MinecartTNT) && (blockEntity = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY)) instanceof BeehiveBlockEntity) {
            BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
            beehiveBlockEntity.emptyAllLivingFromHive(null, state, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
        }
        return super.getDrops(state, params);
    }

    @Override
    protected ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData) {
        ItemStack itemStack = super.getCloneItemStack(level, pos, state, includeData);
        if (includeData) {
            itemStack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(HONEY_LEVEL, state.getValue(HONEY_LEVEL)));
        }
        return itemStack;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
        BlockEntity blockEntity;
        if (level.getBlockState(neighbourPos).getBlock() instanceof FireBlock && (blockEntity = level.getBlockEntity(pos)) instanceof BeehiveBlockEntity) {
            BeehiveBlockEntity beehiveBlockEntity = (BeehiveBlockEntity)blockEntity;
            beehiveBlockEntity.emptyAllLivingFromHive(null, state, BeehiveBlockEntity.BeeReleaseStatus.EMERGENCY);
        }
        return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState)state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }
}

