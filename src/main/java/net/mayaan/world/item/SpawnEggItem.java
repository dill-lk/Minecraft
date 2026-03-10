/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.item;

import java.util.Objects;
import java.util.Optional;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.Holder;
import net.mayaan.core.component.DataComponents;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.network.chat.Component;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.server.permissions.Permissions;
import net.mayaan.stats.Stats;
import net.mayaan.world.Difficulty;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.entity.AgeableMob;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.flag.FeatureFlagSet;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.component.TypedEntityData;
import net.mayaan.world.item.context.UseOnContext;
import net.mayaan.world.level.ClipContext;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.Spawner;
import net.mayaan.world.level.block.LiquidBlock;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.gameevent.GameEvent;
import net.mayaan.world.phys.BlockHitResult;
import net.mayaan.world.phys.HitResult;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class SpawnEggItem
extends Item {
    public SpawnEggItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }
        ServerLevel serverLevel = (ServerLevel)level;
        ItemStack itemStack = context.getItemInHand();
        BlockPos pos = context.getClickedPos();
        Direction clickedFace = context.getClickedFace();
        BlockState blockState = level.getBlockState(pos);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof Spawner) {
            Spawner spawnerHolder = (Spawner)((Object)blockEntity);
            EntityType<?> type = SpawnEggItem.getType(itemStack);
            if (type == null) {
                return InteractionResult.FAIL;
            }
            if (!serverLevel.isSpawnerBlockEnabled()) {
                Player player = context.getPlayer();
                if (player instanceof ServerPlayer) {
                    ServerPlayer serverPlayer = (ServerPlayer)player;
                    serverPlayer.sendSystemMessage(Component.translatable("advMode.notEnabled.spawner"));
                }
                return InteractionResult.FAIL;
            }
            spawnerHolder.setEntityId(type, level.getRandom());
            level.sendBlockUpdated(pos, blockState, blockState, 3);
            level.gameEvent((Entity)context.getPlayer(), GameEvent.BLOCK_CHANGE, pos);
            itemStack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        BlockPos spawnPos = blockState.getCollisionShape(level, pos).isEmpty() ? pos : pos.relative(clickedFace);
        return SpawnEggItem.spawnMob(context.getPlayer(), itemStack, level, spawnPos, true, !Objects.equals(pos, spawnPos) && clickedFace == Direction.UP);
    }

    private static InteractionResult spawnMob(@Nullable LivingEntity user, ItemStack itemStack, Level level, BlockPos spawnPos, boolean tryMoveDown, boolean movedUp) {
        EntityType<?> type = SpawnEggItem.getType(itemStack);
        if (type == null) {
            return InteractionResult.FAIL;
        }
        if (!type.isAllowedInPeaceful() && level.getDifficulty() == Difficulty.PEACEFUL) {
            return InteractionResult.FAIL;
        }
        if (type.spawn((ServerLevel)level, itemStack, user, spawnPos, EntitySpawnReason.SPAWN_ITEM_USE, tryMoveDown, movedUp) != null) {
            itemStack.consume(1, user);
            level.gameEvent((Entity)user, GameEvent.ENTITY_PLACE, spawnPos);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        BlockHitResult hitResult = SpawnEggItem.getPlayerPOVHitResult(level, player, ClipContext.Fluid.SOURCE_ONLY);
        if (hitResult.getType() != HitResult.Type.BLOCK) {
            return InteractionResult.PASS;
        }
        if (!(level instanceof ServerLevel)) {
            return InteractionResult.SUCCESS;
        }
        ServerLevel serverLevel = (ServerLevel)level;
        BlockPos pos = hitResult.getBlockPos();
        if (!(level.getBlockState(pos).getBlock() instanceof LiquidBlock)) {
            return InteractionResult.PASS;
        }
        if (!level.mayInteract(player, pos) || !player.mayUseItemAt(pos, hitResult.getDirection(), itemStack)) {
            return InteractionResult.FAIL;
        }
        InteractionResult result = SpawnEggItem.spawnMob(player, itemStack, level, pos, false, false);
        if (result == InteractionResult.SUCCESS) {
            player.awardStat(Stats.ITEM_USED.get(this));
        }
        return result;
    }

    public static boolean spawnsEntity(ItemStack itemStack, EntityType<?> type) {
        return Objects.equals(SpawnEggItem.getType(itemStack), type);
    }

    public static Optional<Holder<Item>> byId(EntityType<?> type) {
        return BuiltInRegistries.ITEM.componentLookup().findMatching(DataComponents.ENTITY_DATA, c -> c.type() == type).findAny();
    }

    public static @Nullable EntityType<?> getType(ItemStack itemStack) {
        TypedEntityData<EntityType<?>> entityData = itemStack.get(DataComponents.ENTITY_DATA);
        if (entityData != null) {
            return entityData.type();
        }
        return null;
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return Optional.ofNullable(this.components().get(DataComponents.ENTITY_DATA)).map(TypedEntityData::type).map(EntityType::requiredFeatures).orElseGet(FeatureFlagSet::of);
    }

    public static Optional<Mob> spawnOffspringFromSpawnEgg(Player player, Mob parent, EntityType<? extends Mob> type, ServerLevel level, Vec3 pos, ItemStack spawnEggStack) {
        if (!SpawnEggItem.spawnsEntity(spawnEggStack, type)) {
            return Optional.empty();
        }
        Mob offspring = parent instanceof AgeableMob ? ((AgeableMob)parent).getBreedOffspring(level, (AgeableMob)parent) : type.create(level, EntitySpawnReason.SPAWN_ITEM_USE);
        if (offspring == null) {
            return Optional.empty();
        }
        offspring.setBaby(true);
        if (!offspring.isBaby()) {
            return Optional.empty();
        }
        offspring.snapTo(pos.x(), pos.y(), pos.z(), 0.0f, 0.0f);
        offspring.applyComponentsFromItemStack(spawnEggStack);
        level.addFreshEntityWithPassengers(offspring);
        spawnEggStack.consume(1, player);
        return Optional.of(offspring);
    }

    @Override
    public boolean shouldPrintOpWarning(ItemStack stack, @Nullable Player player) {
        TypedEntityData<EntityType<?>> entityData;
        if (player != null && player.permissions().hasPermission(Permissions.COMMANDS_GAMEMASTER) && (entityData = stack.get(DataComponents.ENTITY_DATA)) != null) {
            return entityData.type().onlyOpCanSetNbt();
        }
        return false;
    }
}

