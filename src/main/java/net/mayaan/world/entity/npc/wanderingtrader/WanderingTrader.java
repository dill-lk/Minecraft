/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.world.entity.npc.wanderingtrader;

import java.util.EnumSet;
import java.util.Objects;
import net.mayaan.core.BlockPos;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.sounds.SoundEvent;
import net.mayaan.sounds.SoundEvents;
import net.mayaan.stats.Stats;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.InteractionResult;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.entity.AgeableMob;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.ExperienceOrb;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.ai.goal.AvoidEntityGoal;
import net.mayaan.world.entity.ai.goal.FloatGoal;
import net.mayaan.world.entity.ai.goal.Goal;
import net.mayaan.world.entity.ai.goal.InteractGoal;
import net.mayaan.world.entity.ai.goal.LookAtPlayerGoal;
import net.mayaan.world.entity.ai.goal.LookAtTradingPlayerGoal;
import net.mayaan.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.mayaan.world.entity.ai.goal.PanicGoal;
import net.mayaan.world.entity.ai.goal.TradeWithPlayerGoal;
import net.mayaan.world.entity.ai.goal.UseItemGoal;
import net.mayaan.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.mayaan.world.entity.monster.Vex;
import net.mayaan.world.entity.monster.Zoglin;
import net.mayaan.world.entity.monster.illager.Evoker;
import net.mayaan.world.entity.monster.illager.Illusioner;
import net.mayaan.world.entity.monster.illager.Pillager;
import net.mayaan.world.entity.monster.illager.Vindicator;
import net.mayaan.world.entity.monster.zombie.Zombie;
import net.mayaan.world.entity.npc.villager.AbstractVillager;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.Items;
import net.mayaan.world.item.alchemy.PotionContents;
import net.mayaan.world.item.alchemy.Potions;
import net.mayaan.world.item.component.Consumable;
import net.mayaan.world.item.trading.MerchantOffer;
import net.mayaan.world.item.trading.MerchantOffers;
import net.mayaan.world.item.trading.TradeSets;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class WanderingTrader
extends AbstractVillager
implements Consumable.OverrideConsumeSound {
    private static final int DEFAULT_DESPAWN_DELAY = 0;
    private @Nullable BlockPos wanderTarget;
    private int despawnDelay = 0;

    public WanderingTrader(EntityType<? extends WanderingTrader> type, Level level) {
        super((EntityType<? extends AbstractVillager>)type, level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(0, new UseItemGoal<WanderingTrader>(this, PotionContents.createItemStack(Items.POTION, Potions.INVISIBILITY), SoundEvents.WANDERING_TRADER_DISAPPEARED, e -> this.level().isDarkOutside() && !e.isInvisible()));
        this.goalSelector.addGoal(0, new UseItemGoal<WanderingTrader>(this, new ItemStack(Items.MILK_BUCKET), SoundEvents.WANDERING_TRADER_REAPPEARED, e -> this.level().isBrightOutside() && e.isInvisible()));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<Zombie>(this, Zombie.class, 8.0f, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<Evoker>(this, Evoker.class, 12.0f, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<Vindicator>(this, Vindicator.class, 8.0f, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<Vex>(this, Vex.class, 8.0f, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<Pillager>(this, Pillager.class, 15.0f, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<Illusioner>(this, Illusioner.class, 12.0f, 0.5, 0.5));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<Zoglin>(this, Zoglin.class, 10.0f, 0.5, 0.5));
        this.goalSelector.addGoal(1, new PanicGoal(this, 0.5));
        this.goalSelector.addGoal(1, new LookAtTradingPlayerGoal(this));
        this.goalSelector.addGoal(2, new WanderToPositionGoal(this, this, 2.0, 0.35));
        this.goalSelector.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.35));
        this.goalSelector.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.35));
        this.goalSelector.addGoal(9, new InteractGoal(this, Player.class, 3.0f, 1.0f));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0f));
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel level, AgeableMob partner) {
        return null;
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!itemStack.is(Items.VILLAGER_SPAWN_EGG) && this.isAlive() && !this.isTrading() && !this.isBaby()) {
            if (hand == InteractionHand.MAIN_HAND) {
                player.awardStat(Stats.TALKED_TO_VILLAGER);
            }
            if (!this.level().isClientSide()) {
                if (this.getOffers().isEmpty()) {
                    return InteractionResult.CONSUME;
                }
                this.setTradingPlayer(player);
                this.openTradingScreen(player, this.getDisplayName(), 1);
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    protected void updateTrades(ServerLevel level) {
        MerchantOffers offers = this.getOffers();
        this.addOffersFromTradeSet(level, offers, TradeSets.WANDERING_TRADER_BUYING);
        this.addOffersFromTradeSet(level, offers, TradeSets.WANDERING_TRADER_UNCOMMON);
        this.addOffersFromTradeSet(level, offers, TradeSets.WANDERING_TRADER_COMMON);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt("DespawnDelay", this.despawnDelay);
        output.storeNullable("wander_target", BlockPos.CODEC, this.wanderTarget);
    }

    @Override
    protected void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.despawnDelay = input.getIntOr("DespawnDelay", 0);
        this.wanderTarget = input.read("wander_target", BlockPos.CODEC).orElse(null);
        this.setAge(Math.max(0, this.getAge()));
    }

    @Override
    public boolean removeWhenFarAway(double distSqr) {
        return false;
    }

    @Override
    protected void rewardTradeXp(MerchantOffer offer) {
        if (offer.shouldRewardExp()) {
            int popXp = 3 + this.random.nextInt(4);
            this.level().addFreshEntity(new ExperienceOrb(this.level(), this.getX(), this.getY() + 0.5, this.getZ(), popXp));
        }
    }

    @Override
    protected SoundEvent getAmbientSound() {
        if (this.isTrading()) {
            return SoundEvents.WANDERING_TRADER_TRADE;
        }
        return SoundEvents.WANDERING_TRADER_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.WANDERING_TRADER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WANDERING_TRADER_DEATH;
    }

    @Override
    public SoundEvent getConsumeSound(ItemStack itemStack) {
        if (itemStack.is(Items.MILK_BUCKET)) {
            return SoundEvents.WANDERING_TRADER_DRINK_MILK;
        }
        return SoundEvents.WANDERING_TRADER_DRINK_POTION;
    }

    @Override
    protected SoundEvent getTradeUpdatedSound(boolean validTrade) {
        return validTrade ? SoundEvents.WANDERING_TRADER_YES : SoundEvents.WANDERING_TRADER_NO;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.WANDERING_TRADER_YES;
    }

    public void setDespawnDelay(int despawnDelay) {
        this.despawnDelay = despawnDelay;
    }

    public int getDespawnDelay() {
        return this.despawnDelay;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level().isClientSide()) {
            this.maybeDespawn();
        }
    }

    private void maybeDespawn() {
        if (this.despawnDelay > 0 && !this.isTrading() && --this.despawnDelay == 0) {
            this.discard();
        }
    }

    public void setWanderTarget(@Nullable BlockPos pos) {
        this.wanderTarget = pos;
    }

    private @Nullable BlockPos getWanderTarget() {
        return this.wanderTarget;
    }

    private class WanderToPositionGoal
    extends Goal {
        final WanderingTrader trader;
        final double stopDistance;
        final double speedModifier;
        final /* synthetic */ WanderingTrader this$0;

        WanderToPositionGoal(WanderingTrader wanderingTrader, WanderingTrader trader, double stopDistance, double speedModifier) {
            WanderingTrader wanderingTrader2 = wanderingTrader;
            Objects.requireNonNull(wanderingTrader2);
            this.this$0 = wanderingTrader2;
            this.trader = trader;
            this.stopDistance = stopDistance;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public void stop() {
            this.trader.setWanderTarget(null);
            this.this$0.navigation.stop();
        }

        @Override
        public boolean canUse() {
            BlockPos wanderPosition = this.trader.getWanderTarget();
            return wanderPosition != null && this.isTooFarAway(wanderPosition, this.stopDistance);
        }

        @Override
        public void tick() {
            BlockPos wanderPosition = this.trader.getWanderTarget();
            if (wanderPosition != null && this.this$0.navigation.isDone()) {
                if (this.isTooFarAway(wanderPosition, 10.0)) {
                    Vec3 dir = new Vec3((double)wanderPosition.getX() - this.trader.getX(), (double)wanderPosition.getY() - this.trader.getY(), (double)wanderPosition.getZ() - this.trader.getZ()).normalize();
                    Vec3 targetPos = dir.scale(10.0).add(this.trader.getX(), this.trader.getY(), this.trader.getZ());
                    this.this$0.navigation.moveTo(targetPos.x, targetPos.y, targetPos.z, this.speedModifier);
                } else {
                    this.this$0.navigation.moveTo(wanderPosition.getX(), wanderPosition.getY(), wanderPosition.getZ(), this.speedModifier);
                }
            }
        }

        private boolean isTooFarAway(BlockPos pos, double distance) {
            return !pos.closerToCenterThan(this.trader.position(), distance);
        }
    }
}

