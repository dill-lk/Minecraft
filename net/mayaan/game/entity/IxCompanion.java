package net.mayaan.game.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.mayaan.network.chat.Component;
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
 * Ix — the ancient Construct companion who guides the player throughout the story.
 *
 * <p>Ix is the most important non-player entity in Mayaan. It is 3,000 years old,
 * heavily damaged (its left arm is missing, its voice module is distorted), and carries
 * fragmented memories of the day The Unraveling occurred. Ix cannot say everything it
 * remembers — not because it refuses, but because its memory crystal is fractured.
 * As the player repairs Ix using Core Shards and Anima Shards, more memories unlock.
 *
 * <p>Ix is named after the Mayaan MEND glyph (script name: {@code "Ix"}) — the will to
 * heal. Whether that is coincidence or design depends on who built it and why.
 *
 * <h2>Memory System</h2>
 * Ix's memories are indexed from 0 to {@link #TOTAL_MEMORIES}. Initially only
 * {@link #MEMORY_START_UNLOCKED} memories are available. Repairing Ix at repair
 * thresholds (every 20 max-health worth of repair) unlocks the next memory.
 *
 * <p>Each memory is referenced by a translation key:
 * {@code "ix.memory.mayaan.N"} where N is the 1-based memory index. These are
 * displayed via Timeline Echo sequences when the player interacts with Ix at
 * memory-unlocking moments.
 *
 * <h2>Repair</h2>
 * <ul>
 *   <li>Repair with a Core Shard: restores 20 max-health; may unlock a memory</li>
 *   <li>Repair with 5 Anima Shards: restores 10 max-health; does not unlock memories</li>
 *   <li>Max health increases with each repair to a ceiling of {@link #MAX_REPAIRED_HEALTH}</li>
 * </ul>
 *
 * <h2>Companion Behavior</h2>
 * <ul>
 *   <li>Follows the bonded player when more than 4 blocks away</li>
 *   <li>Stays within 12 blocks of the bonded player when possible</li>
 *   <li>Does not attack enemies unless the bonded player is struck</li>
 *   <li>Emits a subtle golden particle trail on ley-line surfaces (cosmetic)</li>
 * </ul>
 *
 * @see net.mayaan.game.magic.GlyphType#MEND
 * @see net.mayaan.game.item.CoreShard
 */
public class IxCompanion extends AbstractGolem {

    // ── Repair constants ──────────────────────────────────────────────────────

    /** The maximum health Ix can reach after full repair. */
    public static final double MAX_REPAIRED_HEALTH = 120.0;

    /** The starting health of Ix (heavily damaged). */
    public static final double STARTING_HEALTH = 30.0;

    /** Health restored per Core Shard repair use. */
    public static final double CORE_SHARD_REPAIR_AMOUNT = 20.0;

    /** Health restored per 5 Anima Shards repair. */
    public static final double ANIMA_SHARD_REPAIR_AMOUNT = 10.0;

    // ── Memory constants ──────────────────────────────────────────────────────

    /** Total number of Ix memory fragments that can be unlocked. */
    public static final int TOTAL_MEMORIES = 9;

    /** Number of memories available at the start (the Scout's memory is always present). */
    public static final int MEMORY_START_UNLOCKED = 1;

    /** Max-health threshold between memory unlocks. Every {@code MEMORY_UNLOCK_INTERVAL}
     *  max-health restored via Core Shard unlocks the next memory. */
    public static final double MEMORY_UNLOCK_INTERVAL = 10.0;

    // ── NBT keys ─────────────────────────────────────────────────────────────

    private static final String TAG_MEMORIES_UNLOCKED = "IxMemoriesUnlocked";
    private static final String TAG_REPAIRED_MAX_HEALTH = "IxRepairedMaxHealth";
    private static final String TAG_BONDED = "IxBonded";

    // ── State ─────────────────────────────────────────────────────────────────

    /** How many of Ix's memories the player has unlocked. */
    private int memoriesUnlocked = MEMORY_START_UNLOCKED;

    /** The current maximum health of Ix (increases with repair). */
    private double repairedMaxHealth = STARTING_HEALTH;

    /** Whether Ix has bonded with (and will follow) a player. */
    private boolean bonded = false;

    public IxCompanion(EntityType<? extends IxCompanion> type, Level level) {
        super(type, level);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(0, new FloatGoal(this));
        goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0f));
        goalSelector.addGoal(2, new WaterAvoidingRandomStrollGoal(this, 0.5));
        goalSelector.addGoal(3, new RandomLookAroundGoal(this));
    }

    /**
     * Creates the initial attribute set for Ix.
     * Note: max health starts at {@link #STARTING_HEALTH}. The server-side repair
     * logic must call {@link net.mayaan.world.entity.ai.attributes.AttributeInstance#setBaseValue}
     * to increase it after repairs.
     */
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, STARTING_HEALTH)
                .add(Attributes.MOVEMENT_SPEED, 0.25)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.ARMOR, 6.0);
    }

    // ── Memory management ─────────────────────────────────────────────────────

    /** Returns the number of Ix memories currently unlocked. */
    public int getMemoriesUnlocked() {
        return memoriesUnlocked;
    }

    /** Returns all currently unlocked memory translation keys (1-based indices). */
    public List<String> getUnlockedMemoryKeys() {
        List<String> keys = new ArrayList<>(memoriesUnlocked);
        for (int i = 1; i <= memoriesUnlocked; i++) {
            keys.add("ix.memory.mayaan." + i);
        }
        return Collections.unmodifiableList(keys);
    }

    /**
     * Returns {@code true} if there are more memories left to unlock.
     */
    public boolean hasLockedMemories() {
        return memoriesUnlocked < TOTAL_MEMORIES;
    }

    /**
     * Attempts to unlock the next Ix memory.
     * Call this when the repair threshold is crossed.
     *
     * @return the translation key of the newly unlocked memory,
     *         or {@code null} if all memories are already unlocked
     */
    public String unlockNextMemory() {
        if (memoriesUnlocked >= TOTAL_MEMORIES) {
            return null;
        }
        memoriesUnlocked++;
        return "ix.memory.mayaan." + memoriesUnlocked;
    }

    // ── Repair ────────────────────────────────────────────────────────────────

    /**
     * Returns the current tracked maximum health of Ix (distinct from the attribute
     * value — this is the persisted ceiling used to gate memory unlock progress).
     */
    public double getRepairedMaxHealth() {
        return repairedMaxHealth;
    }

    /**
     * Applies a repair increase to Ix's tracked max-health and checks whether
     * a new memory should be unlocked.
     *
     * <p>Call this from the server-side use-item event after a Core Shard or Anima Shard
     * repair. Adjust the entity's actual attribute value separately.
     *
     * @param amount  the health increase applied (e.g., {@link #CORE_SHARD_REPAIR_AMOUNT})
     * @param fromCore {@code true} if this repair came from a Core Shard (triggers memory check)
     * @return the newly unlocked memory key if a threshold was crossed, or {@code null}
     */
    public String applyRepair(double amount, boolean fromCore) {
        double before = repairedMaxHealth;
        repairedMaxHealth = Math.min(MAX_REPAIRED_HEALTH, repairedMaxHealth + amount);

        if (!fromCore || !hasLockedMemories()) {
            return null;
        }
        // Unlock a memory for every MEMORY_UNLOCK_INTERVAL of repair health added
        double beforeThreshold = Math.floor(before / MEMORY_UNLOCK_INTERVAL);
        double afterThreshold  = Math.floor(repairedMaxHealth / MEMORY_UNLOCK_INTERVAL);
        if (afterThreshold > beforeThreshold) {
            return unlockNextMemory();
        }
        return null;
    }

    // ── Bonding ───────────────────────────────────────────────────────────────

    /** Returns whether Ix has bonded with a player and will follow them. */
    public boolean isBonded() {
        return bonded;
    }

    /**
     * Bonds Ix to the player. Called from the server-side spawn handler when
     * Ix first appears on the Isle of First Light.
     */
    public void bond() {
        this.bonded = true;
    }

    // ── Persistence ───────────────────────────────────────────────────────────

    @Override
    public void addAdditionalSaveData(ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putInt(TAG_MEMORIES_UNLOCKED, memoriesUnlocked);
        output.putDouble(TAG_REPAIRED_MAX_HEALTH, repairedMaxHealth);
        output.putBoolean(TAG_BONDED, bonded);
    }

    @Override
    public void readAdditionalSaveData(ValueInput input) {
        super.readAdditionalSaveData(input);
        this.memoriesUnlocked = Math.max(MEMORY_START_UNLOCKED,
                Math.min(TOTAL_MEMORIES, input.getIntOr(TAG_MEMORIES_UNLOCKED, MEMORY_START_UNLOCKED)));
        this.repairedMaxHealth = Math.max(STARTING_HEALTH,
                Math.min(MAX_REPAIRED_HEALTH, input.getDoubleOr(TAG_REPAIRED_MAX_HEALTH, STARTING_HEALTH)));
        this.bonded = input.getBooleanOr(TAG_BONDED, false);
    }

    @Override
    protected Component getTypeName() {
        return Component.translatable("entity.mayaan.ix");
    }
}
