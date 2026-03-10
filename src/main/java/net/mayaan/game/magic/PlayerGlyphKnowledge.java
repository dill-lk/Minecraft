package net.mayaan.game.magic;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

/**
 * Tracks a single player's glyph fragment collection and mastery for all seven
 * {@link GlyphType Glyph types}.
 *
 * <h2>Glyph Knowledge Score</h2>
 * The <em>Glyph Knowledge score</em> is a number from 0–7 representing how many glyph types
 * the player has reached at least {@link GlyphMastery#PRACTICED} in. It is the second main
 * story-progression gate (alongside
 * {@link net.mayaan.game.faction.PlayerFactionData faction standing}) used to unlock
 * Timeline Echoes, NPC dialogue options, and Act II/III content.
 *
 * <h2>Persistence</h2>
 * Use {@link #save()} and {@link #load(Map)} to serialize/deserialize to a flat
 * string map (compatible with NBT compound tag storage).
 */
public final class PlayerGlyphKnowledge {

    /** The starting fragment count for every glyph type. */
    public static final int STARTING_FRAGMENTS = 0;

    /** Hard ceiling on fragments stored per glyph type (prevents overflow on repeated collection). */
    public static final int MAX_FRAGMENTS_PER_GLYPH = 99;

    private static final String FRAGMENT_PREFIX = "glyph_fragments:";

    /** Raw fragment counts per glyph type. */
    private final EnumMap<GlyphType, Integer> fragments = new EnumMap<>(GlyphType.class);

    private PlayerGlyphKnowledge() {
        for (GlyphType type : GlyphType.values()) {
            fragments.put(type, STARTING_FRAGMENTS);
        }
    }

    // ── Factory Methods ───────────────────────────────────────────────────────

    /**
     * Creates a new {@link PlayerGlyphKnowledge} with all glyphs at {@link #STARTING_FRAGMENTS}.
     */
    public static PlayerGlyphKnowledge newGame() {
        return new PlayerGlyphKnowledge();
    }

    /**
     * Loads glyph knowledge from a previously {@linkplain #save() saved} string map.
     * Returns fresh new-game data on null, empty, or corrupt input.
     *
     * @param data the serialized data map; may be null or empty
     * @return restored glyph knowledge, or fresh new-game data on failure
     */
    public static PlayerGlyphKnowledge load(Map<String, String> data) {
        PlayerGlyphKnowledge knowledge = new PlayerGlyphKnowledge();
        if (data == null || data.isEmpty()) {
            return knowledge;
        }
        data.forEach((key, value) -> {
            if (!key.startsWith(FRAGMENT_PREFIX)) {
                return;
            }
            String glyphId = key.substring(FRAGMENT_PREFIX.length());
            GlyphType glyph = GlyphType.byId(glyphId);
            if (glyph == null) {
                return; // Unknown glyph type — forward-compat, skip silently
            }
            try {
                int count = Integer.parseInt(value);
                knowledge.fragments.put(glyph, clampFragmentCount(count));
            } catch (NumberFormatException ignored) {
                // Corrupt value — leave at default
            }
        });
        return knowledge;
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    /**
     * Returns the raw fragment count for the given glyph type.
     *
     * @param glyphType the glyph type to query
     */
    public int getFragmentCount(GlyphType glyphType) {
        return fragments.getOrDefault(glyphType, STARTING_FRAGMENTS);
    }

    /**
     * Returns the {@link GlyphMastery} tier for the given glyph type.
     *
     * @param glyphType the glyph type to query
     */
    public GlyphMastery getMastery(GlyphType glyphType) {
        return GlyphMastery.fromFragmentCount(getFragmentCount(glyphType));
    }

    /**
     * Returns the Glyph Knowledge score: the count of glyph types at which the player
     * has reached at least {@link GlyphMastery#PRACTICED}. Range: 0–7.
     *
     * <p>This score is used as a story gate; certain Timeline Echoes and NPC dialogues
     * require a minimum score before they become available.
     */
    public int getKnowledgeScore() {
        int score = 0;
        for (GlyphType type : GlyphType.values()) {
            if (getMastery(type).countsTowardKnowledgeScore()) {
                score++;
            }
        }
        return score;
    }

    /**
     * Returns {@code true} if the player has a Glyph Knowledge score of at least
     * {@code minimumScore}. Used for story gate checks.
     *
     * @param minimumScore the minimum required score (0–7)
     */
    public boolean hasKnowledgeScore(int minimumScore) {
        return getKnowledgeScore() >= minimumScore;
    }

    /**
     * Returns {@code true} if the player has at least {@link GlyphMastery#PRACTICED}
     * in the specified glyph type.
     *
     * @param glyphType the glyph type to test
     */
    public boolean hasPracticedGlyph(GlyphType glyphType) {
        return getMastery(glyphType).isAtLeast(GlyphMastery.PRACTICED);
    }

    /**
     * Returns {@code true} if the player has {@link GlyphMastery#MASTERED} the specified glyph.
     *
     * @param glyphType the glyph type to test
     */
    public boolean hasMasteredGlyph(GlyphType glyphType) {
        return getMastery(glyphType).isAtLeast(GlyphMastery.MASTERED);
    }

    /**
     * Returns an unmodifiable snapshot of all fragment counts, keyed by glyph type.
     */
    public Map<GlyphType, Integer> getAllFragmentCounts() {
        return Collections.unmodifiableMap(fragments);
    }

    // ── Mutation ──────────────────────────────────────────────────────────────

    /**
     * Awards the player one fragment of the given glyph type.
     * The count is capped at {@link #MAX_FRAGMENTS_PER_GLYPH}.
     *
     * @param glyphType the glyph type to award
     * @return the new {@link GlyphMastery} tier after adding the fragment
     */
    public GlyphMastery awardFragment(GlyphType glyphType) {
        return awardFragments(glyphType, 1);
    }

    /**
     * Awards the player the specified number of fragments of the given glyph type.
     * The total count is capped at {@link #MAX_FRAGMENTS_PER_GLYPH}.
     *
     * @param glyphType the glyph type to award
     * @param count     the number of fragments to award (must be positive)
     * @return the new {@link GlyphMastery} tier after the addition
     */
    public GlyphMastery awardFragments(GlyphType glyphType, int count) {
        if (count <= 0) {
            return getMastery(glyphType);
        }
        int current = fragments.getOrDefault(glyphType, STARTING_FRAGMENTS);
        int updated = clampFragmentCount(current + count);
        fragments.put(glyphType, updated);
        return GlyphMastery.fromFragmentCount(updated);
    }

    // ── Serialization ─────────────────────────────────────────────────────────

    /**
     * Serializes this glyph knowledge data into a flat string-to-string map for storage.
     *
     * @return the serialized data map; suitable for NBT or JSON persistence
     */
    public Map<String, String> save() {
        Map<String, String> data = new java.util.HashMap<>();
        fragments.forEach((type, count) ->
                data.put(FRAGMENT_PREFIX + type.getId(), String.valueOf(count)));
        return data;
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /** Clamps a raw fragment count to [{@code 0}, {@link #MAX_FRAGMENTS_PER_GLYPH}]. */
    private static int clampFragmentCount(int count) {
        return Math.max(0, Math.min(MAX_FRAGMENTS_PER_GLYPH, count));
    }
}
