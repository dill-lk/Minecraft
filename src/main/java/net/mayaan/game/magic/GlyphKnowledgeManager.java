package net.mayaan.game.magic;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Central manager for all players' glyph fragment collections and mastery.
 *
 * <p>Maintains per-player {@link PlayerGlyphKnowledge} and provides the primary API
 * for glyph knowledge interactions:
 * <ul>
 *   <li>{@link #getKnowledge(UUID)} — retrieve a player's glyph knowledge</li>
 *   <li>{@link #getMastery(UUID, GlyphType)} — get mastery tier for one glyph</li>
 *   <li>{@link #awardFragment(UUID, GlyphType)} — award one fragment to a player</li>
 *   <li>{@link #getKnowledgeScore(UUID)} — get the Glyph Knowledge score (0–7)</li>
 *   <li>{@link #hasKnowledgeScore(UUID, int)} — check story-gate score requirement</li>
 *   <li>{@link #loadKnowledge(UUID, Map)} / {@link #unloadKnowledge(UUID)} — session lifecycle</li>
 * </ul>
 *
 * <p>This class is a singleton. Use {@link #INSTANCE} to obtain it.
 * Player data is held in memory during a session and must be persisted by the server
 * layer using {@link PlayerGlyphKnowledge#save()} and {@link PlayerGlyphKnowledge#load(Map)}.
 *
 * <h2>Tier Transitions</h2>
 * When {@link #awardFragment} causes a player to cross a {@link GlyphMastery} tier boundary,
 * the method returns the new tier so callers can react (e.g., display a notification, unlock
 * new dialogue options, or trigger a Timeline Echo).
 */
public final class GlyphKnowledgeManager {

    /** The singleton instance. */
    public static final GlyphKnowledgeManager INSTANCE = new GlyphKnowledgeManager();

    private final Map<UUID, PlayerGlyphKnowledge> knowledgeMap = new HashMap<>();

    private GlyphKnowledgeManager() {}

    // ── Session lifecycle ─────────────────────────────────────────────────────

    /**
     * Returns the glyph knowledge for the given player.
     * Creates fresh new-game knowledge if none exists yet.
     *
     * @param playerId the player's unique UUID
     */
    public PlayerGlyphKnowledge getKnowledge(UUID playerId) {
        return knowledgeMap.computeIfAbsent(playerId, id -> PlayerGlyphKnowledge.newGame());
    }

    /**
     * Registers pre-loaded glyph knowledge for a player.
     * Call this on player login, after reading saved data from disk.
     *
     * @param playerId  the player's UUID
     * @param savedData the serialized data map from a previous {@link PlayerGlyphKnowledge#save()};
     *                  pass an empty map or {@code null} for a new player
     */
    public void loadKnowledge(UUID playerId, Map<String, String> savedData) {
        knowledgeMap.put(playerId, PlayerGlyphKnowledge.load(savedData));
    }

    /**
     * Removes a player's in-memory glyph knowledge after their data has been saved.
     * Call this on player logout.
     *
     * @param playerId the player's UUID
     */
    public void unloadKnowledge(UUID playerId) {
        knowledgeMap.remove(playerId);
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    /**
     * Returns the raw fragment count for the given player and glyph type.
     *
     * @param playerId  the player's UUID
     * @param glyphType the glyph type to query
     */
    public int getFragmentCount(UUID playerId, GlyphType glyphType) {
        return getKnowledge(playerId).getFragmentCount(glyphType);
    }

    /**
     * Returns the {@link GlyphMastery} tier for the given player and glyph type.
     *
     * @param playerId  the player's UUID
     * @param glyphType the glyph type to query
     */
    public GlyphMastery getMastery(UUID playerId, GlyphType glyphType) {
        return getKnowledge(playerId).getMastery(glyphType);
    }

    /**
     * Returns the Glyph Knowledge score for the given player (0–7).
     * A score point is earned for each glyph type at {@link GlyphMastery#PRACTICED} or higher.
     *
     * @param playerId the player's UUID
     */
    public int getKnowledgeScore(UUID playerId) {
        return getKnowledge(playerId).getKnowledgeScore();
    }

    /**
     * Returns {@code true} if the player has a Glyph Knowledge score of at least
     * {@code minimumScore}. Used for story gate checks.
     *
     * @param playerId     the player's UUID
     * @param minimumScore the minimum required score (0–7)
     */
    public boolean hasKnowledgeScore(UUID playerId, int minimumScore) {
        return getKnowledge(playerId).hasKnowledgeScore(minimumScore);
    }

    /**
     * Returns {@code true} if the player has at least {@link GlyphMastery#PRACTICED}
     * in the given glyph type.
     *
     * @param playerId  the player's UUID
     * @param glyphType the glyph type to test
     */
    public boolean hasPracticedGlyph(UUID playerId, GlyphType glyphType) {
        return getKnowledge(playerId).hasPracticedGlyph(glyphType);
    }

    /**
     * Returns {@code true} if the player has {@link GlyphMastery#MASTERED} the given glyph type.
     *
     * @param playerId  the player's UUID
     * @param glyphType the glyph type to test
     */
    public boolean hasMasteredGlyph(UUID playerId, GlyphType glyphType) {
        return getKnowledge(playerId).hasMasteredGlyph(glyphType);
    }

    // ── Fragment awards ───────────────────────────────────────────────────────

    /**
     * Awards one fragment of the given glyph type to the player.
     *
     * <p>Returns the new {@link GlyphMastery} tier. Callers can compare to the previous tier
     * (obtained via {@link #getMastery}) to detect tier transitions and react accordingly.
     *
     * @param playerId  the player's UUID
     * @param glyphType the glyph type to award
     * @return the new mastery tier
     */
    public GlyphMastery awardFragment(UUID playerId, GlyphType glyphType) {
        return getKnowledge(playerId).awardFragment(glyphType);
    }

    /**
     * Awards the specified number of fragments of the given glyph type to the player.
     *
     * @param playerId  the player's UUID
     * @param glyphType the glyph type to award
     * @param count     the number of fragments to award (must be positive)
     * @return the new mastery tier
     */
    public GlyphMastery awardFragments(UUID playerId, GlyphType glyphType, int count) {
        return getKnowledge(playerId).awardFragments(glyphType, count);
    }
}
