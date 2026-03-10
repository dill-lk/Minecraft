package net.mayaan.game.magic;

import java.util.HashMap;
import java.util.Map;

/**
 * Tracks a single player's Anima — the life-force currency of Mayaan magic.
 *
 * <h2>The Anima Pool</h2>
 * Every player has a pool of Anima that starts at {@link AnimaSystem#DEFAULT_MAX_ANIMA}.
 * Casting Glyph magic, powering Constructs, and activating Mayaan machinery all
 * {@link #spend(int) spend} Anima. The pool regenerates naturally over time at a rate
 * determined by the player's current terrain (ley-line vs. normal ground).
 *
 * <h2>Anima Drought</h2>
 * Cumulative Anima expenditure in a single in-game day is tracked via {@link #droughtPoints}.
 * When the total surpasses {@link AnimaSystem#DROUGHT_THRESHOLD}, the player enters an
 * <em>Anima Drought</em> state for that day: glyph effects weaken, the environment starts
 * to visually desiccate, and some powerful NPCs react negatively.
 *
 * <h2>Persistence</h2>
 * Use {@link #save()} and {@link #load(Map)} to serialize/deserialize to a flat string map
 * (compatible with NBT compound tag storage).
 *
 * @see AnimaSystem
 * @see AnimaManager
 */
public final class PlayerAnimaData {

    // ── NBT / persistence keys ────────────────────────────────────────────────

    private static final String KEY_CURRENT = "anima:current";
    private static final String KEY_MAX = "anima:max";
    private static final String KEY_DROUGHT = "anima:drought_points";

    // ── State ─────────────────────────────────────────────────────────────────

    /** Current Anima. Always in range [0, maxAnima]. */
    private float currentAnima;

    /** Maximum Anima the player can hold. Starts at {@link AnimaSystem#DEFAULT_MAX_ANIMA}. */
    private int maxAnima;

    /**
     * Cumulative Anima spent in the current in-game day.
     * Compared against {@link AnimaSystem#DROUGHT_THRESHOLD} to detect Anima Drought.
     * Reset to 0 at the start of each new in-game day.
     */
    private int droughtPoints;

    // ── Runtime-only fields (not persisted) ───────────────────────────────────

    /**
     * Whether the drought-onset sound has been played since entering the current drought.
     * Reset to {@code false} when the drought clears.
     */
    private boolean droughtSoundPlayed;

    /**
     * Number of consecutive leyline-contact checks (at {@code LEYLINE_CHECK_INTERVAL} ticks)
     * since the player last left a Leyline Conduit.
     * Used by {@link net.mayaan.game.MayaanServerEvents} to gate the Anima Surge effect.
     */
    private int leylineContactTicks;

    private PlayerAnimaData(float currentAnima, int maxAnima, int droughtPoints) {
        this.maxAnima = maxAnima;
        this.droughtPoints = droughtPoints;
        this.currentAnima = Math.max(0f, Math.min(currentAnima, maxAnima));
    }

    // ── Factory methods ───────────────────────────────────────────────────────

    /**
     * Creates a new {@link PlayerAnimaData} for a first-time player.
     * The pool starts full at {@link AnimaSystem#DEFAULT_MAX_ANIMA}.
     */
    public static PlayerAnimaData newGame() {
        return new PlayerAnimaData(AnimaSystem.DEFAULT_MAX_ANIMA, AnimaSystem.DEFAULT_MAX_ANIMA, 0);
    }

    /**
     * Loads Anima data from a previously {@linkplain #save() saved} string map.
     * Returns fresh new-game data on null, empty, or corrupt input.
     *
     * @param data the serialized data map; may be null or empty
     */
    public static PlayerAnimaData load(Map<String, String> data) {
        if (data == null || data.isEmpty()) {
            return newGame();
        }
        try {
            float current = Float.parseFloat(data.getOrDefault(KEY_CURRENT,
                    String.valueOf((float) AnimaSystem.DEFAULT_MAX_ANIMA)));
            int max = Integer.parseInt(data.getOrDefault(KEY_MAX,
                    String.valueOf(AnimaSystem.DEFAULT_MAX_ANIMA)));
            int drought = Integer.parseInt(data.getOrDefault(KEY_DROUGHT, "0"));
            // Sanitize: max must be positive; drought must be non-negative
            max = Math.max(1, max);
            drought = Math.max(0, drought);
            return new PlayerAnimaData(current, max, drought);
        } catch (NumberFormatException ignored) {
            return newGame();
        }
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    /** Returns the player's current Anima. Always in [0, {@link #getMaxAnima()}]. */
    public float getCurrentAnima() {
        return currentAnima;
    }

    /** Returns the player's maximum Anima capacity. */
    public int getMaxAnima() {
        return maxAnima;
    }

    /**
     * Returns the cumulative Anima spent in the current in-game day.
     * When this reaches or exceeds {@link AnimaSystem#DROUGHT_THRESHOLD}, the player
     * is in an Anima Drought.
     */
    public int getDroughtPoints() {
        return droughtPoints;
    }

    /**
     * Returns {@code true} if the player is currently in an Anima Drought —
     * they have spent more than {@link AnimaSystem#DROUGHT_THRESHOLD} Anima today.
     */
    public boolean isInDrought() {
        return droughtPoints >= AnimaSystem.DROUGHT_THRESHOLD;
    }

    /**
     * Returns {@code true} if the player has enough current Anima to cover {@code cost}.
     *
     * @param cost the Anima cost to check (non-negative)
     */
    public boolean canSpend(int cost) {
        return cost >= 0 && currentAnima >= cost;
    }

    /**
     * Returns the current Anima as a fraction of the maximum, in [0.0, 1.0].
     * Useful for rendering UI fill bars.
     */
    public float getFillFraction() {
        return maxAnima > 0 ? Math.min(1.0f, currentAnima / maxAnima) : 0f;
    }

    // ── Mutation ──────────────────────────────────────────────────────────────

    /**
     * Spends the given amount of Anima if the player has enough.
     *
     * @param cost the Anima to spend (non-negative)
     * @return {@code true} if the spend succeeded; {@code false} if insufficient Anima
     */
    public boolean spend(int cost) {
        if (!canSpend(cost)) {
            return false;
        }
        currentAnima -= cost;
        droughtPoints += cost;
        return true;
    }

    /**
     * Regenerates the given amount of Anima, clamped to {@link #maxAnima}.
     * Use {@link AnimaSystem#LEYLINE_REGEN_PER_TICK} or
     * {@link AnimaSystem#NORMAL_REGEN_PER_TICK} as the regen amount.
     *
     * @param amount the Anima to restore (non-negative; clamped internally)
     */
    public void regen(float amount) {
        if (amount > 0) {
            currentAnima = Math.min(maxAnima, currentAnima + amount);
        }
    }

    /**
     * Resets the daily drought tracking counter.
     * Call this at the start of each new in-game day.
     */
    public void resetDroughtPoints() {
        droughtPoints = 0;
    }

    /**
     * Increases the player's maximum Anima capacity.
     * Useful for permanent upgrades from Faction rewards or story milestones.
     *
     * @param bonus the amount to add to the maximum (must be positive)
     */
    public void increaseMaxAnima(int bonus) {
        if (bonus > 0) {
            maxAnima += bonus;
        }
    }

    /**
     * Sets the current Anima directly — for server-side corrections or death-recovery logic.
     * The value is clamped to [0, maxAnima].
     *
     * @param value the new current Anima value
     */
    public void setCurrentAnima(float value) {
        currentAnima = Math.max(0f, Math.min(maxAnima, value));
    }

    // ── Runtime (non-persisted) state ─────────────────────────────────────────

    /** Returns {@code true} if the drought-onset sound has already been played this drought. */
    public boolean isDroughtSoundPlayed() {
        return droughtSoundPlayed;
    }

    /** Marks the drought-onset sound as having been played. */
    public void markDroughtSoundPlayed() {
        droughtSoundPlayed = true;
    }

    /** Clears the drought-sound-played flag (called when drought ends). */
    public void clearDroughtSoundPlayed() {
        droughtSoundPlayed = false;
    }

    /** Returns the number of consecutive leyline-contact checks accumulated. */
    public int getLeylineContactTicks() {
        return leylineContactTicks;
    }

    /** Increments the leyline-contact counter by one check. */
    public void incrementLeylineContactTicks() {
        leylineContactTicks++;
    }

    /** Resets the leyline-contact counter to zero (called when player leaves a conduit). */
    public void resetLeylineContactTicks() {
        leylineContactTicks = 0;
    }

    // ── Serialization ─────────────────────────────────────────────────────────

    /**
     * Serializes this Anima data into a flat string-to-string map for storage.
     *
     * @return the serialized data map; suitable for NBT or JSON persistence
     */
    public Map<String, String> save() {
        Map<String, String> data = new HashMap<>();
        data.put(KEY_CURRENT, String.valueOf(currentAnima));
        data.put(KEY_MAX, String.valueOf(maxAnima));
        data.put(KEY_DROUGHT, String.valueOf(droughtPoints));
        return data;
    }
}
