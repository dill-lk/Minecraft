package net.mayaan.game.entity;

import net.mayaan.network.chat.Component;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.ai.goal.FloatGoal;
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
 * Hollow Knight — an ancient Mayaan Construct corrupted by The Maw's influence.
 *
 * <p>Hollow Knights are Mayaan Constructs — once soldiers, engineers, or temple guardians —
 * that wandered too close to a Maw-Rift. The Maw's entropic resonance has warped their
 * Core Shards, replacing structured personality with a relentless drive to consume
 * Anima from living beings.
 *
 * <p>Despite their corruption, Hollow Knights still follow their last known patrol routes.
 * A Hollow Knight guarding a Mayaan temple still guards that temple — it just destroys
 * anything that approaches now. This makes ruins with Hollow Knights more dangerous,
 * but also more authentic to the Mayaan world: the guards never stopped working.
 *
 * <h2>Anima Drain Attack</h2>
 * In addition to physical melee, Hollow Knights have an {@link #DRAIN_RANGE}-block
 * ranged Anima Drain: a beam of Maw-corruption that subtracts 15 Anima from the target
 * per activation. Managed at the server event layer calling
 * {@link net.mayaan.game.magic.AnimaManager#spend}.
 *
 * <h2>Loot</h2>
 * Drops 1 {@code mayaan:corrupted_core_shard} (can be refined into a Core Shard at
 * the Glyph Table using MEND magic) and 0–2 {@code mayaan:hollow_plate}.
 *
 * @see net.mayaan.game.entity.MayaanConstruct
 * @see net.mayaan.game.magic.AnimaManager
 */
public class HollowKnight extends Monster {

    /** The maximum range in blocks at which the Hollow Knight can activate its Anima Drain. */
    public static final float DRAIN_RANGE = 6.0f;

    /** Anima drained per Drain activation. Applied at the server event layer. */
    public static final int DRAIN_ANIMA_COST = 15;

    /**
     * Whether this Hollow Knight is in an Anima-drain charging state.
     * When true, particles should show the drain beam on the client.
     */
    private boolean drainingActive = false;

    /** Tick counter for drain cooldown (20 ticks = 1 second; drain recharges every 40 ticks). */
    private int drainCooldown = 0;

    public HollowKnight(EntityType<? extends HollowKnight> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0, false));
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.7));
        goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 12.0f));
        goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    /**
     * Creates the attribute set for a Hollow Knight.
     * Heavily armored, moderately fast, with a potent Anima drain — formidable alone,
     * overwhelming in groups.
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 80.0)
                .add(Attributes.MOVEMENT_SPEED, 0.28)
                .add(Attributes.ATTACK_DAMAGE, 9.0)
                .add(Attributes.FOLLOW_RANGE, 24.0)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.7)
                .add(Attributes.ARMOR, 12.0)
                .add(Attributes.ARMOR_TOUGHNESS, 2.0);
    }

    /** Returns whether this Hollow Knight is currently running a drain beam. */
    public boolean isDrainingActive() {
        return drainingActive;
    }

    /** Sets the drain-active state (triggered by server-tick drain logic). */
    public void setDrainingActive(boolean drainingActive) {
        this.drainingActive = drainingActive;
    }

    /** Returns the remaining drain cooldown in ticks. */
    public int getDrainCooldown() {
        return drainCooldown;
    }

    /** Sets the drain cooldown (server-side tick management). */
    public void setDrainCooldown(int drainCooldown) {
        this.drainCooldown = Math.max(0, drainCooldown);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("DrainingActive", drainingActive);
        output.putInt("DrainCooldown", drainCooldown);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.drainingActive = input.getBooleanOr("DrainingActive", false);
        this.drainCooldown = input.getIntOr("DrainCooldown", 0);
    }

    @Override
    protected Component getTypeName() {
        return Component.translatable("entity.mayaan.hollow_knight");
    }

    // ── Anima drain tick logic ────────────────────────────────────────────────

    /**
     * Tries to drain Anima from a nearby player every {@code DRAIN_COOLDOWN_TICKS} ticks.
     *
     * <p>If a player is within {@link #DRAIN_RANGE} blocks and the drain is off cooldown,
     * the Hollow Knight enters the draining-active state, spends {@link #DRAIN_ANIMA_COST}
     * from the nearest player's pool via {@link net.mayaan.game.magic.AnimaManager}, and
     * heals itself for half the drained amount.
     */
    private static final int DRAIN_COOLDOWN_TICKS = 40;

    @Override
    public void aiStep() {
        super.aiStep();
        if (level().isClientSide()) {
            return;
        }
        if (drainCooldown > 0) {
            drainCooldown--;
            drainingActive = false;
            return;
        }
        // Attempt to find a nearby player to drain
        Player nearest = level().getNearestPlayer(this, DRAIN_RANGE);
        if (nearest instanceof net.mayaan.server.level.ServerPlayer serverPlayer) {
            net.mayaan.game.magic.AnimaManager.INSTANCE.spend(serverPlayer.getUUID(), DRAIN_ANIMA_COST);
            // Heal the Hollow Knight for half the drained amount
            this.heal(DRAIN_ANIMA_COST * 0.5f);
            drainingActive = true;
            drainCooldown = DRAIN_COOLDOWN_TICKS;
            // Sync anima to the affected player's client
            net.mayaan.game.MayaanPacketSender.sendAnimaSync(serverPlayer);
        } else {
            drainingActive = false;
        }
    }
}
