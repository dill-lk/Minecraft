package net.mayaan.game.entity;

import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.ai.attributes.AttributeSupplier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.entity.ai.goal.FloatGoal;
import net.mayaan.world.entity.ai.goal.LookAtPlayerGoal;
import net.mayaan.world.entity.ai.goal.RandomLookAroundGoal;
import net.mayaan.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.mayaan.world.entity.animal.golem.AbstractGolem;
import net.mayaan.world.entity.player.Player;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.storage.ValueInput;
import net.mayaan.world.level.storage.ValueOutput;

/**
 * Construct — an autonomous being animated by a Core Shard of crystallized Anima.
 *
 * Constructs were the Mayaan's greatest engineering achievement. Each is animated
 * by a fragment of Anima Prime that carries a subtle crystallization of personality.
 * After The Unraveling, Constructs continue to wander Xibalkaal — guarding, waiting,
 * running down, or (in some cases) going slowly mad.
 *
 * <h2>Ix</h2>
 * The first Construct the player encounters. Ancient, damaged, and frustratingly cryptic.
 * Ix carries fragments of memories from the day of The Unraveling. Though it rarely
 * speaks plainly, its guidance is genuine.
 *
 * <h2>Behavior</h2>
 * <ul>
 *   <li>Neutral by default — will not attack unless provoked or ordered</li>
 *   <li>Follows a bonded player (bonded via Core Shard use)</li>
 *   <li>Detects Anima Drought in the surrounding area and warns the player</li>
 *   <li>Can be repaired using Anima Shards and Core Shards</li>
 * </ul>
 */
public class MayaanConstruct extends AbstractGolem {
    /** NBT key for the unique construct designation (e.g., "Ix", "Scout-7", etc.). */
    private static final String TAG_DESIGNATION = "Designation";

    /** NBT key for whether this construct has bonded with the player. */
    private static final String TAG_BONDED = "Bonded";

    /** Default designation for a Construct whose identity is not yet known. */
    private static final String DEFAULT_DESIGNATION = "Unknown";

    /** The designation name of this individual Construct. */
    private String designation = DEFAULT_DESIGNATION;

    /** Whether this Construct has bonded with a player. */
    private boolean bonded = false;

    public MayaanConstruct(EntityType<? extends MayaanConstruct> type, Level level) {
        super(type, level);
    }

    /** Returns the designation (individual name) of this Construct. */
    public String getDesignation() {
        return designation;
    }

    /** Sets the designation (individual name) of this Construct. */
    public void setDesignation(String designation) {
        this.designation = designation;
    }

    /** Returns whether this Construct has bonded with a player. */
    public boolean isBonded() {
        return bonded;
    }

    /** Bonds this Construct to a player via Core Shard. */
    public void bond() {
        this.bonded = true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0f));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.8));
        this.goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    /**
     * Creates the attribute set for a standard Construct.
     * Constructs are durable and moderately mobile — built to last millennia.
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60.0)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.ARMOR, 8.0);
    }

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putString(TAG_DESIGNATION, designation);
        output.putBoolean(TAG_BONDED, bonded);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.designation = input.getStringOr(TAG_DESIGNATION, DEFAULT_DESIGNATION);
        this.bonded = input.getBooleanOr(TAG_BONDED, false);
    }
}
