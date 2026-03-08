package net.mayaan.game.faction;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Central manager for all players' faction standing data.
 *
 * <p>Maintains per-player {@link PlayerFactionData} and provides the primary API
 * for faction interactions:
 * <ul>
 *   <li>{@link #getFactionData(UUID)} — retrieve a player's faction data</li>
 *   <li>{@link #getStanding(UUID, Faction)} — get a player's standing tier with a faction</li>
 *   <li>{@link #adjustStanding(UUID, Faction, int)} — change a player's standing</li>
 *   <li>{@link #hasAccessTo(UUID, Faction)} — check story-gate standing requirement</li>
 *   <li>{@link #hasAccessToAllFactions(UUID)} — check all four gates for Act I conclusion</li>
 *   <li>{@link #loadData(UUID, Map)} / {@link #unloadData(UUID)} — session lifecycle</li>
 * </ul>
 *
 * <p>This class is a singleton. Use {@link #INSTANCE} to obtain it.
 * Player data is held in memory during a session and must be persisted by the server
 * layer using {@link PlayerFactionData#save()} and {@link PlayerFactionData#load(Map)}.
 *
 * <h2>Standing Change Events</h2>
 * When {@link #adjustStanding} causes a player to cross a {@link FactionStanding} tier
 * boundary, the method returns the new tier so callers can react (e.g., display a
 * notification, trigger dialogue, or unlock new content).
 */
public final class FactionManager {

    /** The singleton instance. */
    public static final FactionManager INSTANCE = new FactionManager();

    private final Map<UUID, PlayerFactionData> dataMap = new HashMap<>();

    private FactionManager() {}

    // ── Session lifecycle ─────────────────────────────────────────────────────

    /**
     * Returns the faction data for the given player.
     * Creates a fresh new-game data set if none exists yet.
     *
     * @param playerId the player's unique UUID
     */
    public PlayerFactionData getFactionData(UUID playerId) {
        return dataMap.computeIfAbsent(playerId, id -> PlayerFactionData.newGame());
    }

    /**
     * Registers pre-loaded faction data for a player.
     * Call this on player login, after reading saved data from disk.
     *
     * @param playerId  the player's UUID
     * @param savedData the serialized data map from a previous {@link PlayerFactionData#save()};
     *                  pass an empty map or {@code null} for a new player
     */
    public void loadData(UUID playerId, Map<String, String> savedData) {
        dataMap.put(playerId, PlayerFactionData.load(savedData));
    }

    /**
     * Removes a player's in-memory faction data after their data has been saved.
     * Call this on player logout.
     *
     * @param playerId the player's UUID
     */
    public void unloadData(UUID playerId) {
        dataMap.remove(playerId);
    }

    // ── Standing queries ──────────────────────────────────────────────────────

    /**
     * Returns the raw reputation point total for the player with the given faction.
     *
     * @param playerId the player's UUID
     * @param faction  the faction to query
     */
    public int getPoints(UUID playerId, Faction faction) {
        return getFactionData(playerId).getPoints(faction);
    }

    /**
     * Returns the {@link FactionStanding} tier for the player with the given faction.
     *
     * @param playerId the player's UUID
     * @param faction  the faction to query
     */
    public FactionStanding getStanding(UUID playerId, Faction faction) {
        return getFactionData(playerId).getStanding(faction);
    }

    /**
     * Returns {@code true} if the player's standing with the given faction meets
     * the faction's minimum requirement for story access.
     *
     * @param playerId the player's UUID
     * @param faction  the faction whose story-gate to test
     */
    public boolean hasAccessTo(UUID playerId, Faction faction) {
        return getFactionData(playerId).hasAccessTo(faction);
    }

    /**
     * Returns {@code true} if the player has met the minimum standing requirement
     * with <em>all four</em> factions, which is the condition that fully opens the
     * path to the Axis Temple at the end of Act I.
     *
     * @param playerId the player's UUID
     */
    public boolean hasAccessToAllFactions(UUID playerId) {
        return getFactionData(playerId).hasAccessToAllFactions();
    }

    // ── Standing changes ──────────────────────────────────────────────────────

    /**
     * Adjusts the player's standing with the given faction by the specified delta.
     *
     * <p>Returns the new {@link FactionStanding} tier after the change. Callers can
     * compare the returned tier to the previous one (obtained via {@link #getStanding})
     * to detect tier transitions and react accordingly (notifications, unlocks, etc.).
     *
     * <p>The point total is clamped to
     * [{@link PlayerFactionData#MIN_POINTS}, {@link PlayerFactionData#MAX_POINTS}].
     *
     * @param playerId the player's UUID
     * @param faction  the faction whose standing to modify
     * @param delta    the point change (positive = more friendly, negative = more hostile)
     * @return the new standing tier
     */
    public FactionStanding adjustStanding(UUID playerId, Faction faction, int delta) {
        return getFactionData(playerId).adjustStanding(faction, delta);
    }

    /**
     * Directly sets the standing points for a player with a faction.
     * Clamps to [{@link PlayerFactionData#MIN_POINTS}, {@link PlayerFactionData#MAX_POINTS}].
     *
     * @param playerId   the player's UUID
     * @param faction    the faction to set
     * @param pointValue the exact point value to assign
     */
    public void setStanding(UUID playerId, Faction faction, int pointValue) {
        getFactionData(playerId).setStanding(faction, pointValue);
    }

    /**
     * Applies a standing change to <em>all four</em> factions simultaneously.
     * Useful for global reputation events (e.g., the player does something that
     * the entire world notices).
     *
     * @param playerId the player's UUID
     * @param delta    the point change to apply to every faction
     */
    public void adjustAllFactions(UUID playerId, int delta) {
        for (Faction faction : Faction.values()) {
            getFactionData(playerId).adjustStanding(faction, delta);
        }
    }
}
