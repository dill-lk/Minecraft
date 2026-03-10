package net.mayaan.game.magic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Central manager for all players' Anima pools.
 *
 * <p>Maintains per-player {@link PlayerAnimaData} and provides the primary API
 * for Anima interactions:
 * <ul>
 *   <li>{@link #getAnimaData(UUID)} — retrieve a player's Anima state</li>
 *   <li>{@link #canSpend(UUID, int)} / {@link #spend(UUID, int)} — spend-gate checks and execution</li>
 *   <li>{@link #onTick(UUID, boolean)} — regen tick, called once per game tick per online player</li>
 *   <li>{@link #onDayChange(UUID)} — resets daily drought tracking at dawn</li>
 *   <li>{@link #loadAnimaData(UUID, Map)} / {@link #unloadAnimaData(UUID)} — session lifecycle</li>
 * </ul>
 *
 * <p>This class is a singleton. Use {@link #INSTANCE} to obtain it.
 *
 * <h2>Regen Rates</h2>
 * <ul>
 *   <li>On a ley-line: {@link AnimaSystem#LEYLINE_REGEN_PER_TICK} per tick</li>
 *   <li>Normal terrain: {@link AnimaSystem#NORMAL_REGEN_PER_TICK} per tick</li>
 * </ul>
 * There are 20 game ticks per second and roughly 1200 ticks (60 seconds) per in-game minute.
 *
 * <h2>Drought Detection</h2>
 * {@link #isInDrought(UUID)} returns true when a player has spent ≥
 * {@link AnimaSystem#DROUGHT_THRESHOLD} Anima in the current in-game day.
 * The drought counter resets via {@link #onDayChange(UUID)}.
 */
public final class AnimaManager {

    /** The singleton instance. */
    public static final AnimaManager INSTANCE = new AnimaManager();

    private final Map<UUID, PlayerAnimaData> animaMap = new HashMap<>();

    private AnimaManager() {}

    // ── Session lifecycle ─────────────────────────────────────────────────────

    /**
     * Returns the Anima data for the given player.
     * Creates fresh new-game data if none exists yet.
     *
     * @param playerId the player's UUID
     */
    public PlayerAnimaData getAnimaData(UUID playerId) {
        return animaMap.computeIfAbsent(playerId, id -> PlayerAnimaData.newGame());
    }

    /**
     * Registers pre-loaded Anima data for a player.
     * Call this on player login, after reading saved data from disk.
     *
     * @param playerId  the player's UUID
     * @param savedData the serialized data map from a previous {@link PlayerAnimaData#save()};
     *                  pass an empty map or {@code null} for a new player
     */
    public void loadAnimaData(UUID playerId, Map<String, String> savedData) {
        animaMap.put(playerId, PlayerAnimaData.load(savedData));
    }

    /**
     * Removes a player's in-memory Anima data after it has been saved.
     * Call this on player logout.
     *
     * @param playerId the player's UUID
     */
    public void unloadAnimaData(UUID playerId) {
        animaMap.remove(playerId);
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    /**
     * Returns the player's current Anima.
     *
     * @param playerId the player's UUID
     */
    public float getCurrentAnima(UUID playerId) {
        return getAnimaData(playerId).getCurrentAnima();
    }

    /**
     * Returns the player's maximum Anima.
     *
     * @param playerId the player's UUID
     */
    public int getMaxAnima(UUID playerId) {
        return getAnimaData(playerId).getMaxAnima();
    }

    /**
     * Returns {@code true} if the player has enough Anima to cover the given cost.
     *
     * @param playerId the player's UUID
     * @param cost     the Anima cost (non-negative)
     */
    public boolean canSpend(UUID playerId, int cost) {
        return getAnimaData(playerId).canSpend(cost);
    }

    /**
     * Returns {@code true} if the player is currently in an Anima Drought.
     * A drought begins when cumulative daily Anima expenditure reaches
     * {@link AnimaSystem#DROUGHT_THRESHOLD}.
     *
     * @param playerId the player's UUID
     */
    public boolean isInDrought(UUID playerId) {
        return getAnimaData(playerId).isInDrought();
    }

    // ── Actions ───────────────────────────────────────────────────────────────

    /**
     * Attempts to spend the given amount of Anima.
     * Returns {@code false} (without modifying state) if the player lacks sufficient Anima.
     * Updates the daily drought counter if successful.
     *
     * @param playerId the player's UUID
     * @param cost     the Anima cost (non-negative)
     * @return {@code true} if the spend succeeded; {@code false} if insufficient Anima
     */
    public boolean spend(UUID playerId, int cost) {
        return getAnimaData(playerId).spend(cost);
    }

    /**
     * Restores the given amount of Anima to the player's pool, up to their maximum.
     *
     * @param playerId the player's UUID
     * @param amount   the Anima to restore (non-negative)
     */
    public void regen(UUID playerId, float amount) {
        getAnimaData(playerId).regen(amount);
    }

    /**
     * Restores a fixed amount of Anima to the given player.
     *
     * <p>Unlike {@link #spend}, this succeeds only if the player's pool is not full.
     * Returns {@code true} if at least some Anima was restored (i.e., the pool was not
     * already at maximum), {@code false} if the pool was already full.
     *
     * @param playerId the player's UUID
     * @param amount   the integer amount of Anima to restore
     * @return {@code true} if restoration was applied; {@code false} if already full
     */
    public boolean restore(UUID playerId, int amount) {
        PlayerAnimaData data = getAnimaData(playerId);
        if (data.isFull()) {
            return false;
        }
        data.regen(amount);
        return true;
    }

    /**
     * Per-tick regen update. Call once per game tick per online player.
     *
     * <p>If {@code onLeyline} is {@code true}, uses {@link AnimaSystem#LEYLINE_REGEN_PER_TICK};
     * otherwise uses {@link AnimaSystem#NORMAL_REGEN_PER_TICK}.
     *
     * @param playerId  the player's UUID
     * @param onLeyline {@code true} if the player is currently standing on a ley-line block
     */
    public void onTick(UUID playerId, boolean onLeyline) {
        float rate = onLeyline
                ? AnimaSystem.LEYLINE_REGEN_PER_TICK
                : AnimaSystem.NORMAL_REGEN_PER_TICK;
        getAnimaData(playerId).regen(rate);
    }

    /**
     * Resets the daily drought counter for the given player.
     * Call this when the in-game day changes (e.g., at sunrise).
     *
     * @param playerId the player's UUID
     */
    public void onDayChange(UUID playerId) {
        getAnimaData(playerId).resetDroughtPoints();
    }

    /**
     * Resets the daily drought counter for all currently loaded players.
     * Convenience method for server-side day-change events.
     */
    public void onDayChangeAll() {
        animaMap.values().forEach(PlayerAnimaData::resetDroughtPoints);
    }

    /**
     * Permanently increases a player's maximum Anima capacity.
     * Use for faction rewards, story milestones, and equipment upgrades.
     *
     * @param playerId the player's UUID
     * @param bonus    the amount to add to the maximum (must be positive)
     */
    public void increaseMaxAnima(UUID playerId, int bonus) {
        getAnimaData(playerId).increaseMaxAnima(bonus);
    }
}
