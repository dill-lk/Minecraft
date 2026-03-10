package net.mayaan.game.faction;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Tracks a single player's reputation standing with all four Mayaan factions.
 *
 * <p>Each faction starts at {@link FactionStanding#NEUTRAL} (0 points).
 * Standing is modified through quests, dialogue choices, combat, and events.
 *
 * <h2>Story Gating</h2>
 * {@link #hasAccessTo(Faction)} checks whether the player's current standing with
 * a faction meets that faction's {@link Faction#getMinimumAccessStanding()} threshold.
 * The story manager uses this to determine whether the player can complete the
 * faction-gated portions of Act I chapters.
 *
 * <h2>Persistence</h2>
 * Use {@link #save()} and {@link #load(Map)} to serialize/deserialize to a flat
 * string map (compatible with NBT compound tag storage).
 */
public final class PlayerFactionData {

    /** The point value assigned to every faction at the start of a new game. */
    public static final int STARTING_POINTS = 0;

    /** Hard floor — no faction standing can go below this. */
    public static final int MIN_POINTS = -2000;

    /** Hard ceiling — no faction standing can go above this. */
    public static final int MAX_POINTS = 2000;

    private static final String STANDING_PREFIX = "faction:";

    /** Raw reputation point totals for each faction. */
    private final EnumMap<Faction, Integer> points = new EnumMap<>(Faction.class);

    private PlayerFactionData() {
        for (Faction faction : Faction.values()) {
            points.put(faction, STARTING_POINTS);
        }
    }

    // ── Factory Methods ───────────────────────────────────────────────────────

    /**
     * Creates a new {@link PlayerFactionData} with all factions at {@link #STARTING_POINTS}.
     */
    public static PlayerFactionData newGame() {
        return new PlayerFactionData();
    }

    /**
     * Loads faction data from a previously {@linkplain #save() saved} string map.
     * Returns a fresh {@link #newGame()} instance if the data is null, empty, or corrupt.
     *
     * @param data the serialized data map; may be null or empty
     * @return restored faction data, or fresh new-game data on failure
     */
    public static PlayerFactionData load(Map<String, String> data) {
        PlayerFactionData factionData = new PlayerFactionData();
        if (data == null || data.isEmpty()) {
            return factionData;
        }
        data.forEach((key, value) -> {
            if (!key.startsWith(STANDING_PREFIX)) {
                return;
            }
            String factionId = key.substring(STANDING_PREFIX.length());
            Faction faction = Faction.byId(factionId);
            if (faction == null) {
                return; // Unknown faction — forward-compat, skip silently
            }
            try {
                int rawPoints = Integer.parseInt(value);
                factionData.points.put(faction, clamp(rawPoints));
            } catch (NumberFormatException ignored) {
                // Corrupt value — leave at default
            }
        });
        return factionData;
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    /**
     * Returns the raw reputation point total for the given faction.
     * Range: [{@link #MIN_POINTS}, {@link #MAX_POINTS}].
     *
     * @param faction the faction to query
     */
    public int getPoints(Faction faction) {
        return points.getOrDefault(faction, STARTING_POINTS);
    }

    /**
     * Returns the {@link FactionStanding} tier for the given faction.
     *
     * @param faction the faction to query
     */
    public FactionStanding getStanding(Faction faction) {
        return FactionStanding.fromPoints(getPoints(faction));
    }

    /**
     * Returns {@code true} if the player's current standing with the given faction is at
     * least equal to that faction's required minimum standing for story access.
     *
     * <p>This is the primary check used by the story system to gate faction quest completion.
     *
     * @param faction the faction whose story-gate to test
     */
    public boolean hasAccessTo(Faction faction) {
        return getStanding(faction).isAtLeast(faction.getMinimumAccessStanding());
    }

    /**
     * Returns {@code true} if the player has reached the minimum required standing with
     * <em>all four</em> factions. This condition must be met before the path to the
     * Axis Temple fully opens at the end of Act I.
     */
    public boolean hasAccessToAllFactions() {
        for (Faction faction : Faction.values()) {
            if (!hasAccessTo(faction)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns an unmodifiable snapshot of all faction point totals, keyed by faction.
     */
    public Map<Faction, Integer> getAllPoints() {
        return Collections.unmodifiableMap(points);
    }

    // ── Mutation ──────────────────────────────────────────────────────────────

    /**
     * Adjusts the player's standing with the given faction by the specified delta.
     * The resulting point total is clamped to [{@link #MIN_POINTS}, {@link #MAX_POINTS}].
     *
     * <p>Use positive deltas for friendly actions (completed quests, diplomatic successes)
     * and negative deltas for hostile actions (attacking NPCs, betraying faction goals).
     *
     * @param faction the faction whose standing to modify
     * @param delta   the point change (positive or negative)
     * @return the new {@link FactionStanding} tier after the change
     */
    public FactionStanding adjustStanding(Faction faction, int delta) {
        int current = points.getOrDefault(faction, STARTING_POINTS);
        int updated = clamp(current + delta);
        points.put(faction, updated);
        return FactionStanding.fromPoints(updated);
    }

    /**
     * Directly sets the standing points for a faction.
     * The value is clamped to [{@link #MIN_POINTS}, {@link #MAX_POINTS}].
     *
     * @param faction    the faction to update
     * @param pointValue the new raw point value
     */
    public void setStanding(Faction faction, int pointValue) {
        points.put(faction, clamp(pointValue));
    }

    // ── Serialization ─────────────────────────────────────────────────────────

    /**
     * Serializes this faction data into a flat string-to-string map for storage.
     *
     * @return the serialized data map; suitable for NBT or JSON persistence
     */
    public Map<String, String> save() {
        Map<String, String> data = new java.util.HashMap<>();
        points.forEach((faction, pts) -> data.put(STANDING_PREFIX + faction.getId(), String.valueOf(pts)));
        return data;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private static int clamp(int value) {
        return Math.max(MIN_POINTS, Math.min(MAX_POINTS, value));
    }
}
