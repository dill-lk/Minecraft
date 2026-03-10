package net.mayaan.game.entity;

import net.mayaan.network.chat.Component;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.damagesource.DamageSource;
import net.mayaan.world.effect.MobEffectInstance;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.ai.goal.FloatGoal;
import net.mayaan.world.entity.ai.goal.LeapAtTargetGoal;
import net.mayaan.world.entity.ai.goal.LookAtPlayerGoal;
import net.mayaan.world.entity.ai.goal.MeleeAttackGoal;
import net.mayaan.world.entity.ai.goal.RandomLookAroundGoal;
import net.mayaan.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.mayaan.world.entity.ai.goal.target.HurtByTargetGoal;
import net.mayaan.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.mayaan.world.entity.monster.Monster;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;

/**
 * Canopy Stalker — a six-limbed ambush predator of the Eternal Canopy.
 *
 * <p>Canopy Stalkers move through jungle canopies with fluid, almost liquid grace.
 * They are primarily ambush hunters: they locate prey from above, then drop directly
 * onto them with a paralytic pounce. Their paralytic venom causes the Anima Slow effect —
 * halving the target's Anima regeneration for 30 seconds.
 *
 * <h2>Combat Behavior</h2>
 * <ul>
 *   <li>Stays at range until a {@link #AMBUSH_LEAP_RANGE}-block threshold is crossed</li>
 *   <li>Then uses {@link LeapAtTargetGoal} for a high-velocity ambush leap</li>
 *   <li>Applies venom on first melee hit (implemented at the server event layer)</li>
 *   <li>Retreats and re-stalks if the target breaks visual contact for more than 5 seconds</li>
 * </ul>
 *
 * <h2>Loot</h2>
 * Drops 0–2 {@code mayaan:canopy_stalker_venom_gland} (rare) and 1–3 {@code mayaan:stalker_scale}.
 * Used in paralytic potions and lightweight armor crafting.
 *
 * @see net.mayaan.game.biome.MayaanBiomes#ETERNAL_CANOPY
 */
public class CanopyStalker extends Monster {

    /** Within this many blocks, the Stalker will attempt a leap-pounce attack. */
    public static final float AMBUSH_LEAP_RANGE = 4.0f;

    /** Whether this Stalker is currently in ambush (hiding) mode. */
    private boolean inAmbush = false;

    public CanopyStalker(EntityType<? extends CanopyStalker> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new LeapAtTargetGoal(this, AMBUSH_LEAP_RANGE));
        goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.2, true));
        goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.9));
        goalSelector.addGoal(4, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(5, new RandomLookAroundGoal(this));

        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    /**
     * Creates the attribute set for a Canopy Stalker.
     * Fast, fragile, venomous — built for ambush, not sustained combat.
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 24.0)
                .add(Attributes.MOVEMENT_SPEED, 0.38)
                .add(Attributes.ATTACK_DAMAGE, 4.0)
                .add(Attributes.FOLLOW_RANGE, 20.0)
                .add(Attributes.ARMOR, 2.0);
    }

    /** Returns whether this Stalker is currently hiding in ambush mode. */
    public boolean isInAmbush() {
        return inAmbush;
    }

    /** Sets whether this Stalker is in ambush mode. */
    public void setInAmbush(boolean inAmbush) {
        this.inAmbush = inAmbush;
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("InAmbush", inAmbush);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.inAmbush = input.getBooleanOr("InAmbush", false);
    }

    @Override
    protected Component getTypeName() {
        return Component.translatable("entity.mayaan.canopy_stalker");
    }

    // ── Venom on hit ─────────────────────────────────────────────────────────

    /**
     * Duration (ticks) of the ANIMA_SLOW venom effect: 30 seconds = 600 ticks.
     */
    private static final int VENOM_DURATION_TICKS = 600;

    /**
     * Applies paralytic venom ({@link net.mayaan.game.MayaanMobEffects#ANIMA_SLOW}) to the
     * target when a melee hit lands.
     *
     * <p>The venom halves the target's Anima regeneration for {@link #VENOM_DURATION_TICKS}
     * ticks (30 seconds). Only living entities are affected.
     */
    @Override
    public boolean doHurtTarget(ServerLevel level, Entity target) {
        boolean hit = super.doHurtTarget(level, target);
        if (hit && target instanceof LivingEntity living) {
            living.addEffect(new MobEffectInstance(
                    net.mayaan.game.MayaanMobEffects.ANIMA_SLOW, VENOM_DURATION_TICKS, 0, false, true));
        }
        return hit;
    }
}
