/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.animal.cow;

import net.mayaan.core.BlockPos;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.tags.ItemTags;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.EntityDimensions;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.Pose;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.ai.goal.BreedGoal;
import net.mayaan.world.entity.ai.goal.FloatGoal;
import net.mayaan.world.entity.ai.goal.FollowParentGoal;
import net.mayaan.world.entity.ai.goal.LookAtPlayerGoal;
import net.mayaan.world.entity.ai.goal.PanicGoal;
import net.mayaan.world.entity.ai.goal.RandomLookAroundGoal;
import net.mayaan.world.entity.ai.goal.TemptGoal;
import net.mayaan.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.mayaan.world.entity.animal.Animal;
import net.mayaan.world.entity.animal.cow.CowSoundVariant;
import net.mayaan.world.entity.animal.cow.CowSoundVariants;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.ItemUtils;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.block.state.BlockState;

public abstract class AbstractCow
extends Animal {
    private static final EntityDimensions BABY_DIMENSIONS = EntityType.COW.getDimensions().scale(0.5f).withEyeHeight(0.665f);

    public AbstractCow(EntityType<? extends AbstractCow> type, Level level) {
        super((EntityType<? extends Animal>)type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 2.0));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25, i -> i.is(ItemTags.COW_FOOD), false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.25));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(ItemTags.COW_FOOD);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createAnimalAttributes().add(Attributes.MAX_HEALTH, 10.0).add(Attributes.MOVEMENT_SPEED, 0.2f);
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.getSoundSet().ambientSound().value();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return this.getSoundSet().hurtSound().value();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return this.getSoundSet().deathSound().value();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
        this.playSound(this.getSoundSet().stepSound().value(), 0.15f, 1.0f);
    }

    @Override
    protected float getSoundVolume() {
        return 0.4f;
    }

    protected CowSoundVariant getSoundSet() {
        return SoundEvents.COW_SOUNDS.get((Object)CowSoundVariants.SoundSet.CLASSIC);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.BUCKET) && !this.isBaby()) {
            player.playSound(SoundEvents.COW_MILK, 1.0f, 1.0f);
            ItemStack bucketOrMilkBucket = ItemUtils.createFilledResult(itemStack, player, Items.MILK_BUCKET.getDefaultInstance());
            player.setItemInHand(hand, bucketOrMilkBucket);
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public EntityDimensions getDefaultDimensions(Pose pose) {
        return this.isBaby() ? BABY_DIMENSIONS : super.getDefaultDimensions(pose);
    }
}

