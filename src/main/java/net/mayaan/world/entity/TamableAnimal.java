/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity;

import java.util.Objects;
import java.util.Optional;
import net.mayaan.advancements.CriteriaTriggers;
import net.mayaan.core.BlockPos;
import net.mayaan.core.component.DataComponents;
import net.mayaan.core.particles.ParticleTypes;
import net.mayaan.core.particles.SimpleParticleType;
import net.mayaan.network.syncher.EntityDataAccessor;
import net.mayaan.network.syncher.EntityDataSerializers;
import net.mayaan.network.syncher.SynchedEntityData;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.tags.TagKey;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.damagesource.DamageType;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntityReference;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.OwnableEntity;
import net.mayaan.world.entity.PathfinderMob;
import net.mayaan.world.entity.ai.goal.PanicGoal;
import net.mayaan.world.entity.animal.Animal;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.food.FoodProperties;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.LeavesBlock;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.gamerules.GameRules;
import net.mayaan.world.level.pathfinder.PathType;
import net.mayaan.world.level.pathfinder.WalkNodeEvaluator;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import net.mayaan.world.scores.PlayerTeam;
import org.jspecify.annotations.Nullable;

public abstract class TamableAnimal
extends Animal
implements OwnableEntity {
    public static final int TELEPORT_WHEN_DISTANCE_IS_SQ = 144;
    private static final int MIN_HORIZONTAL_DISTANCE_FROM_TARGET_AFTER_TELEPORTING = 2;
    private static final int MAX_HORIZONTAL_DISTANCE_FROM_TARGET_AFTER_TELEPORTING = 3;
    private static final int MAX_VERTICAL_DISTANCE_FROM_TARGET_AFTER_TELEPORTING = 1;
    private static final boolean DEFAULT_ORDERED_TO_SIT = false;
    protected static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(TamableAnimal.class, EntityDataSerializers.BYTE);
    protected static final EntityDataAccessor<Optional<EntityReference<LivingEntity>>> DATA_OWNERUUID_ID = SynchedEntityData.defineId(TamableAnimal.class, EntityDataSerializers.OPTIONAL_LIVING_ENTITY_REFERENCE);
    private boolean orderedToSit = false;

    protected TamableAnimal(EntityType<? extends TamableAnimal> type, Level level) {
        super((EntityType<? extends Animal>)type, level);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder entityData) {
        super.defineSynchedData(entityData);
        entityData.define(DATA_FLAGS_ID, (byte)0);
        entityData.define(DATA_OWNERUUID_ID, Optional.empty());
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        EntityReference<LivingEntity> owner = this.getOwnerReference();
        EntityReference.store(owner, output, "Owner");
        output.putBoolean("Sitting", this.orderedToSit);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        EntityReference owner = EntityReference.readWithOldOwnerConversion(input, "Owner", this.level());
        if (owner != null) {
            try {
                this.entityData.set(DATA_OWNERUUID_ID, Optional.of(owner));
                this.setTame(true, false);
            }
            catch (Throwable ignored) {
                this.setTame(false, true);
            }
        } else {
            this.entityData.set(DATA_OWNERUUID_ID, Optional.empty());
            this.setTame(false, true);
        }
        this.orderedToSit = input.getBooleanOr("Sitting", false);
        this.setInSittingPose(this.orderedToSit);
    }

    @Override
    public boolean canBeLeashed() {
        return true;
    }

    protected void spawnTamingParticles(boolean success) {
        SimpleParticleType particle = ParticleTypes.HEART;
        if (!success) {
            particle = ParticleTypes.SMOKE;
        }
        for (int i = 0; i < 7; ++i) {
            double xa = this.random.nextGaussian() * 0.02;
            double ya = this.random.nextGaussian() * 0.02;
            double za = this.random.nextGaussian() * 0.02;
            this.level().addParticle(particle, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), xa, ya, za);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 7) {
            this.spawnTamingParticles(true);
        } else if (id == 6) {
            this.spawnTamingParticles(false);
        } else {
            super.handleEntityEvent(id);
        }
    }

    public boolean isTame() {
        return (this.entityData.get(DATA_FLAGS_ID) & 4) != 0;
    }

    public void setTame(boolean isTame, boolean includeSideEffects) {
        byte current = this.entityData.get(DATA_FLAGS_ID);
        if (isTame) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(current | 4));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(current & 0xFFFFFFFB));
        }
        if (includeSideEffects) {
            this.applyTamingSideEffects();
        }
    }

    protected void applyTamingSideEffects() {
    }

    protected void feed(Player player, InteractionHand hand, ItemStack itemStack, float healingFactor, float defaultHeal) {
        FoodProperties foodProperties = itemStack.get(DataComponents.FOOD);
        this.usePlayerItem(player, hand, itemStack);
        this.heal(foodProperties != null ? healingFactor * (float)foodProperties.nutrition() : defaultHeal);
        this.playEatingSound();
    }

    public boolean isInSittingPose() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setInSittingPose(boolean value) {
        byte current = this.entityData.get(DATA_FLAGS_ID);
        if (value) {
            this.entityData.set(DATA_FLAGS_ID, (byte)(current | 1));
        } else {
            this.entityData.set(DATA_FLAGS_ID, (byte)(current & 0xFFFFFFFE));
        }
    }

    @Override
    public @Nullable EntityReference<LivingEntity> getOwnerReference() {
        return this.entityData.get(DATA_OWNERUUID_ID).orElse(null);
    }

    public void setOwner(@Nullable LivingEntity owner) {
        this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(owner).map(EntityReference::of));
    }

    public void setOwnerReference(@Nullable EntityReference<LivingEntity> owner) {
        this.entityData.set(DATA_OWNERUUID_ID, Optional.ofNullable(owner));
    }

    public void tame(Player player) {
        this.setTame(true, true);
        this.setOwner(player);
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)player;
            CriteriaTriggers.TAME_ANIMAL.trigger(serverPlayer, this);
        }
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if (this.isOwnedBy(target)) {
            return false;
        }
        return super.canAttack(target);
    }

    public boolean isOwnedBy(LivingEntity entity) {
        return entity == this.getOwner();
    }

    public boolean wantsToAttack(LivingEntity target, LivingEntity owner) {
        return true;
    }

    @Override
    public @Nullable PlayerTeam getTeam() {
        LivingEntity owner;
        PlayerTeam ownTeam = super.getTeam();
        if (ownTeam != null) {
            return ownTeam;
        }
        if (this.isTame() && (owner = this.getRootOwner()) != null) {
            return owner.getTeam();
        }
        return null;
    }

    @Override
    protected boolean considersEntityAsAlly(Entity other) {
        if (this.isTame()) {
            LivingEntity owner = this.getRootOwner();
            if (other == owner) {
                return true;
            }
            if (owner != null) {
                return owner.considersEntityAsAlly(other);
            }
        }
        return super.considersEntityAsAlly(other);
    }

    @Override
    public void die(DamageSource source) {
        LivingEntity livingEntity;
        ServerLevel serverLevel;
        Level level = this.level();
        if (level instanceof ServerLevel && (serverLevel = (ServerLevel)level).getGameRules().get(GameRules.SHOW_DEATH_MESSAGES).booleanValue() && (livingEntity = this.getOwner()) instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer)livingEntity;
            serverPlayer.sendSystemMessage(this.getCombatTracker().getDeathMessage());
        }
        super.die(source);
    }

    public boolean isOrderedToSit() {
        return this.orderedToSit;
    }

    public void setOrderedToSit(boolean orderedToSit) {
        this.orderedToSit = orderedToSit;
    }

    public void tryToTeleportToOwner() {
        LivingEntity owner = this.getOwner();
        if (owner != null) {
            this.teleportToAroundBlockPos(owner.blockPosition());
        }
    }

    public boolean shouldTryTeleportToOwner() {
        LivingEntity owner = this.getOwner();
        return owner != null && this.distanceToSqr(this.getOwner()) >= 144.0;
    }

    private void teleportToAroundBlockPos(BlockPos targetPos) {
        for (int attempt = 0; attempt < 10; ++attempt) {
            int xd = this.random.nextIntBetweenInclusive(-3, 3);
            int zd = this.random.nextIntBetweenInclusive(-3, 3);
            if (Math.abs(xd) < 2 && Math.abs(zd) < 2) continue;
            int yd = this.random.nextIntBetweenInclusive(-1, 1);
            if (!this.maybeTeleportTo(targetPos.getX() + xd, targetPos.getY() + yd, targetPos.getZ() + zd)) continue;
            return;
        }
    }

    private boolean maybeTeleportTo(int x, int y, int z) {
        if (!this.canTeleportTo(new BlockPos(x, y, z))) {
            return false;
        }
        this.snapTo((double)x + 0.5, y, (double)z + 0.5, this.getYRot(), this.getXRot());
        this.navigation.stop();
        return true;
    }

    private boolean canTeleportTo(BlockPos pos) {
        PathType pathType = WalkNodeEvaluator.getPathTypeStatic(this, pos);
        if (pathType != PathType.WALKABLE) {
            return false;
        }
        BlockState blockStateBelow = this.level().getBlockState(pos.below());
        if (!this.canFlyToOwner() && blockStateBelow.getBlock() instanceof LeavesBlock) {
            return false;
        }
        BlockPos delta = pos.subtract(this.blockPosition());
        return this.level().noCollision(this, this.getBoundingBox().move(delta));
    }

    public final boolean unableToMoveToOwner() {
        return this.isOrderedToSit() || this.isPassenger() || this.mayBeLeashed() || this.getOwner() != null && this.getOwner().isSpectator();
    }

    protected boolean canFlyToOwner() {
        return false;
    }

    public class TamableAnimalPanicGoal
    extends PanicGoal {
        final /* synthetic */ TamableAnimal this$0;

        public TamableAnimalPanicGoal(TamableAnimal this$0, double speedModifier, TagKey<DamageType> panicCausingDamageTypes) {
            TamableAnimal tamableAnimal = this$0;
            Objects.requireNonNull(tamableAnimal);
            this.this$0 = tamableAnimal;
            super((PathfinderMob)this$0, speedModifier, panicCausingDamageTypes);
        }

        public TamableAnimalPanicGoal(TamableAnimal this$0, double speedModifier) {
            TamableAnimal tamableAnimal = this$0;
            Objects.requireNonNull(tamableAnimal);
            this.this$0 = tamableAnimal;
            super(this$0, speedModifier);
        }

        @Override
        public void tick() {
            if (!this.this$0.unableToMoveToOwner() && this.this$0.shouldTryTeleportToOwner()) {
                this.this$0.tryToTeleportToOwner();
            }
            super.tick();
        }
    }
}

