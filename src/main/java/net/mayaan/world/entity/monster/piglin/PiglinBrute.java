/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.monster.piglin;

import java.util.List;
import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.util.RandomSource;
import net.mayaan.util.profiling.Profiler;
import net.mayaan.util.profiling.ProfilerFiller;
import net.mayaan.world.DifficultyInstance;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.SpawnGroupData;
import net.mayaan.world.entity.ai.Brain;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.ai.memory.MemoryModuleType;
import net.mayaan.world.entity.ai.sensing.SensorType;
import net.mayaan.world.entity.monster.Monster;
import net.mayaan.world.entity.monster.piglin.AbstractPiglin;
import net.mayaan.world.entity.monster.piglin.PiglinArmPose;
import net.mayaan.world.entity.monster.piglin.PiglinBruteAi;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.ServerLevelAccessor;
import net.mayaan.world.level.block.state.BlockState;
import org.jspecify.annotations.Nullable;

public class PiglinBrute
extends AbstractPiglin {
    private static final int MAX_HEALTH = 50;
    private static final float MOVEMENT_SPEED_WHEN_FIGHTING = 0.35f;
    private static final int ATTACK_DAMAGE = 7;
    private static final double TARGETING_RANGE = 12.0;
    public static final Brain.Provider<PiglinBrute> BRAIN_PROVIDER = Brain.provider(List.of(MemoryModuleType.NEAREST_VISIBLE_ADULT_PIGLINS), List.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS, SensorType.NEAREST_ITEMS, SensorType.HURT_BY, SensorType.PIGLIN_BRUTE_SPECIFIC_SENSOR), PiglinBruteAi::getActivities);

    public PiglinBrute(EntityType<? extends PiglinBrute> type, Level level) {
        super((EntityType<? extends AbstractPiglin>)type, level);
        this.xpReward = 20;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 50.0).add(Attributes.MOVEMENT_SPEED, 0.35f).add(Attributes.ATTACK_DAMAGE, 7.0).add(Attributes.FOLLOW_RANGE, 12.0);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        PiglinBruteAi.initMemories(this);
        this.populateDefaultEquipmentSlots(level.getRandom(), difficulty);
        return super.finalizeSpawn(level, difficulty, spawnReason, groupData);
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.GOLDEN_AXE));
    }

    protected Brain<PiglinBrute> makeBrain(Brain.Packed packedBrain) {
        return BRAIN_PROVIDER.makeBrain(this, packedBrain);
    }

    public Brain<PiglinBrute> getBrain() {
        return super.getBrain();
    }

    @Override
    public boolean canHunt() {
        return false;
    }

    @Override
    public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
        if (itemStack.is(Items.GOLDEN_AXE)) {
            return super.wantsToPickUp(level, itemStack);
        }
        return false;
    }

    @Override
    protected void customServerAiStep(ServerLevel level) {
        ProfilerFiller profiler = Profiler.get();
        profiler.push("piglinBruteBrain");
        this.getBrain().tick(level, this);
        profiler.pop();
        PiglinBruteAi.updateActivity(this);
        PiglinBruteAi.maybePlayActivitySound(this);
        super.customServerAiStep(level);
    }

    @Override
    public PiglinArmPose getArmPose() {
        if (this.isAggressive() && this.isHoldingMeleeWeapon()) {
            return PiglinArmPose.ATTACKING_WITH_MELEE_WEAPON;
        }
        return PiglinArmPose.DEFAULT;
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource source, float damage) {
        Entity entity;
        boolean wasHurt = super.hurtServer(level, source, damage);
        if (wasHurt && (entity = source.getEntity()) instanceof LivingEntity) {
            LivingEntity sourceEntity = (LivingEntity)entity;
            PiglinBruteAi.wasHurtBy(level, this, sourceEntity);
        }
        return wasHurt;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.PIGLIN_BRUTE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.PIGLIN_BRUTE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_BRUTE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
        this.playSound(SoundEvents.PIGLIN_BRUTE_STEP, 0.15f, 1.0f);
    }

    protected void playAngrySound() {
        this.makeSound(SoundEvents.PIGLIN_BRUTE_ANGRY);
    }

    @Override
    protected void playConvertedSound() {
        this.makeSound(SoundEvents.PIGLIN_BRUTE_CONVERTED_TO_ZOMBIFIED);
    }
}

