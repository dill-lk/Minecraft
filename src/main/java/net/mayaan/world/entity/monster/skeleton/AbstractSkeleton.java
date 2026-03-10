/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.monster.skeleton;

import java.util.Objects;
import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.tags.ItemTags;
import net.mayaan.tags.TagKey;
import net.mayaan.util.RandomSource;
import net.mayaan.util.SpecialDates;
import net.mayaan.world.Difficulty;
import net.mayaan.world.DifficultyInstance;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntitySpawnReason;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.PathfinderMob;
import net.mayaan.world.entity.SpawnGroupData;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.ai.goal.AvoidEntityGoal;
import net.mayaan.world.entity.ai.goal.FleeSunGoal;
import net.mayaan.world.entity.ai.goal.LookAtPlayerGoal;
import net.mayaan.world.entity.ai.goal.MeleeAttackGoal;
import net.mayaan.world.entity.ai.goal.RandomLookAroundGoal;
import net.mayaan.world.entity.ai.goal.RangedBowAttackGoal;
import net.mayaan.world.entity.ai.goal.RestrictSunGoal;
import net.mayaan.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.mayaan.world.entity.ai.goal.target.HurtByTargetGoal;
import net.mayaan.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.mayaan.world.entity.animal.golem.IronGolem;
import net.mayaan.world.entity.animal.turtle.Turtle;
import net.mayaan.world.entity.animal.wolf.Wolf;
import net.mayaan.world.entity.monster.Monster;
import net.mayaan.world.entity.monster.RangedAttackMob;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.entity.projectile.Projectile;
import net.mayaan.world.entity.projectile.ProjectileUtil;
import net.mayaan.world.entity.projectile.arrow.AbstractArrow;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.ServerLevelAccessor;
import net.mayaan.world.level.block.Blocks;
import net.mayaan.world.level.block.state.BlockState;
import net.mayaan.world.level.storage.ValueInput;
import org.jspecify.annotations.Nullable;

public abstract class AbstractSkeleton
extends Monster
implements RangedAttackMob {
    private static final int HARD_ATTACK_INTERVAL = 20;
    private static final int NORMAL_ATTACK_INTERVAL = 40;
    protected static final int INCREASED_HARD_ATTACK_INTERVAL = 50;
    protected static final int INCREASED_NORMAL_ATTACK_INTERVAL = 70;
    private final RangedBowAttackGoal<AbstractSkeleton> bowGoal = new RangedBowAttackGoal<AbstractSkeleton>(this, 1.0, 20, 15.0f);
    private final MeleeAttackGoal meleeGoal = new MeleeAttackGoal(this, this, 1.2, false){
        final /* synthetic */ AbstractSkeleton this$0;
        {
            AbstractSkeleton abstractSkeleton = this$0;
            Objects.requireNonNull(abstractSkeleton);
            this.this$0 = abstractSkeleton;
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
        }

        @Override
        public void stop() {
            super.stop();
            this.this$0.setAggressive(false);
        }

        @Override
        public void start() {
            super.start();
            this.this$0.setAggressive(true);
        }
    };

    protected AbstractSkeleton(EntityType<? extends AbstractSkeleton> type, Level level) {
        super((EntityType<? extends Monster>)type, level);
        this.reassessWeaponGoal();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new RestrictSunGoal(this));
        this.goalSelector.addGoal(3, new FleeSunGoal(this, 1.0));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<Wolf>(this, Wolf.class, 6.0f, 1.0, 1.2));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, new Class[0]));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<Player>((Mob)this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<IronGolem>((Mob)this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<Turtle>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes().add(Attributes.MOVEMENT_SPEED, 0.25);
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState blockState) {
        this.playSound(this.getStepSound(), 0.15f, 1.0f);
    }

    abstract SoundEvent getStepSound();

    @Override
    public void rideTick() {
        super.rideTick();
        Entity entity = this.getControlledVehicle();
        if (entity instanceof PathfinderMob) {
            PathfinderMob entity2 = (PathfinderMob)entity;
            this.yBodyRot = entity2.yBodyRot;
        }
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource random, DifficultyInstance difficulty) {
        super.populateDefaultEquipmentSlots(random, difficulty);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData groupData) {
        groupData = super.finalizeSpawn(level, difficulty, spawnReason, groupData);
        RandomSource random = level.getRandom();
        this.populateDefaultEquipmentSlots(random, difficulty);
        this.populateDefaultEquipmentEnchantments(level, random, difficulty);
        this.reassessWeaponGoal();
        this.setCanPickUpLoot(random.nextFloat() < 0.55f * difficulty.getSpecialMultiplier());
        if (this.getItemBySlot(EquipmentSlot.HEAD).isEmpty() && SpecialDates.isHalloween() && random.nextFloat() < 0.25f) {
            this.setItemSlot(EquipmentSlot.HEAD, new ItemStack(random.nextFloat() < 0.1f ? Blocks.JACK_O_LANTERN : Blocks.CARVED_PUMPKIN));
            this.setDropChance(EquipmentSlot.HEAD, 0.0f);
        }
        return groupData;
    }

    public void reassessWeaponGoal() {
        if (this.level() == null || this.level().isClientSide()) {
            return;
        }
        this.goalSelector.removeGoal(this.meleeGoal);
        this.goalSelector.removeGoal(this.bowGoal);
        ItemStack usedWeapon = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW));
        if (usedWeapon.is(Items.BOW)) {
            int minAttackInterval = this.getHardAttackInterval();
            if (this.level().getDifficulty() != Difficulty.HARD) {
                minAttackInterval = this.getAttackInterval();
            }
            this.bowGoal.setMinAttackInterval(minAttackInterval);
            this.goalSelector.addGoal(4, this.bowGoal);
        } else {
            this.goalSelector.addGoal(4, this.meleeGoal);
        }
    }

    protected int getHardAttackInterval() {
        return 20;
    }

    protected int getAttackInterval() {
        return 40;
    }

    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        ItemStack bowItem = this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW));
        ItemStack projectile = this.getProjectile(bowItem);
        AbstractArrow arrow = this.getArrow(projectile, power, bowItem);
        double xd = target.getX() - this.getX();
        double yd = target.getY(0.3333333333333333) - arrow.getY();
        double zd = target.getZ() - this.getZ();
        double distanceToTarget = Math.sqrt(xd * xd + zd * zd);
        Level level = this.level();
        if (level instanceof ServerLevel) {
            ServerLevel serverLevel = (ServerLevel)level;
            Projectile.spawnProjectileUsingShoot(arrow, serverLevel, projectile, xd, yd + distanceToTarget * (double)0.2f, zd, 1.6f, 14 - serverLevel.getDifficulty().getId() * 4);
        }
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0f, 1.0f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
    }

    protected AbstractArrow getArrow(ItemStack projectile, float power, @Nullable ItemStack firingWeapon) {
        return ProjectileUtil.getMobArrow(this, projectile, power, firingWeapon);
    }

    @Override
    public boolean canUseNonMeleeWeapon(ItemStack item) {
        return item.getItem() == Items.BOW;
    }

    @Override
    public TagKey<Item> getPreferredWeaponType() {
        return ItemTags.SKELETON_PREFERRED_WEAPONS;
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.reassessWeaponGoal();
    }

    @Override
    public void onEquipItem(EquipmentSlot slot, ItemStack oldStack, ItemStack stack) {
        super.onEquipItem(slot, oldStack, stack);
        if (!this.level().isClientSide()) {
            this.reassessWeaponGoal();
        }
    }

    public boolean isShaking() {
        return this.isFullyFrozen();
    }

    @Override
    public boolean wantsToPickUp(ServerLevel level, ItemStack itemStack) {
        if (itemStack.is(ItemTags.SPEARS)) {
            return false;
        }
        return super.wantsToPickUp(level, itemStack);
    }
}

