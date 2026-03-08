/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.animal.camel;

import net.mayaan.core.BlockPos;
import net.mayaan.core.Holder;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.tags.BlockTags;
import net.mayaan.tags.ItemTags;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.AgeableMob;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.animal.Animal;
import net.mayaan.world.entity.animal.camel.Camel;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class CamelHusk
extends Camel {
    public CamelHusk(EntityType<? extends Camel> type, Level level) {
        super(type, level);
    }

    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return true;
    }

    @Override
    public boolean isMobControlled() {
        return this.getFirstPassenger() instanceof Mob;
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand hand, Vec3 location) {
        this.setPersistenceRequired();
        return super.interact(player, hand, location);
    }

    @Override
    public boolean canBeLeashed() {
        return !this.isMobControlled();
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ItemTags.CAMEL_HUSK_FOOD);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.CAMEL_HUSK_AMBIENT;
    }

    @Override
    public boolean canMate(Animal partner) {
        return false;
    }

    @Override
    public @Nullable Camel getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return null;
    }

    @Override
    public boolean canFallInLove() {
        return false;
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.CAMEL_HUSK_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.CAMEL_HUSK_HURT;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
        if (blockState.is(BlockTags.CAMEL_SAND_STEP_SOUND_BLOCKS)) {
            this.playSound(SoundEvents.CAMEL_HUSK_STEP_SAND, 0.4f, 1.0f);
        } else {
            this.playSound(SoundEvents.CAMEL_HUSK_STEP, 0.4f, 1.0f);
        }
    }

    @Override
    protected SoundEvent getDashingSound() {
        return SoundEvents.CAMEL_HUSK_DASH;
    }

    @Override
    protected SoundEvent getDashReadySound() {
        return SoundEvents.CAMEL_HUSK_DASH_READY;
    }

    @Override
    protected SoundEvent getEatingSound() {
        return SoundEvents.CAMEL_HUSK_EAT;
    }

    @Override
    protected SoundEvent getStandUpSound() {
        return SoundEvents.CAMEL_HUSK_STAND;
    }

    @Override
    protected SoundEvent getSitDownSound() {
        return SoundEvents.CAMEL_HUSK_SIT;
    }

    @Override
    protected Holder.Reference<SoundEvent> getSaddleSound() {
        return SoundEvents.CAMEL_HUSK_SADDLE;
    }

    @Override
    public float chargeSpeedModifier() {
        return 4.0f;
    }
}

