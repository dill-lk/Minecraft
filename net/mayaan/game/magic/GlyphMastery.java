package net.mayaan.game.magic;

/**
 * The four mastery tiers that describe how well a player knows a single {@link GlyphType}.
 *
 * <p>Mastery is gained by collecting {@link net.mayaan.game.item.GlyphFragment Glyph Fragments}
 * of the relevant type. Fragments are found in ruins, embedded in Construct memories, and
 * triggered by {@link net.mayaan.game.echo.TimelineEcho Timeline Echoes}. Combining fragments
 * at a Glyph Table permanently increases mastery.
 *
 * <h2>Fragment Thresholds</h2>
 * <pre>
 *   UNLEARNED   (0 fragments) — Default. The player cannot use this glyph at all.
 *   FRAGMENTARY (1–2 frags)   — Partial understanding; the glyph can be invoked at reduced effect.
 *   PRACTICED   (3–4 frags)   — Full basic use unlocked.
 *   MASTERED    (5+ frags)    — Advanced use, Prime amplification, and teaching others unlocked.
 * </pre>
 *
 * <h2>Story Gating</h2>
 * The total number of {@link #PRACTICED} or {@link #MASTERED} glyphs makes up the player's
 * overall <em>Glyph Knowledge score</em> (0–7, one point per glyph type at PRACTICED+).
 * Several story events and Timeline Echoes require a minimum score before they can be triggered.
 *
 * @see PlayerGlyphKnowledge
 * @see GlyphKnowledgeManager
 */
public enum GlyphMastery {

    /**
     * UNLEARNED — the player has never encountered this glyph.
     * The glyph cannot be invoked. Its Mayaan script name is unknown to the player.
     */
    UNLEARNED(0, 0),

    /**
     * FRAGMENTARY — the player holds 1–2 fragments of this glyph.
     * The glyph can be invoked but at reduced potency and higher Anima cost.
     * The player recognizes the script name but cannot read the full glyph's form.
     */
    FRAGMENTARY(1, 2),

    /**
     * PRACTICED — the player holds 3–4 fragments of this glyph.
     * Full basic use is unlocked. Anima cost is standard.
     * New dialogue options with knowledgeable NPCs become available.
     */
    PRACTICED(3, 4),

    /**
     * MASTERED — the player holds 5 or more fragments of this glyph.
     * Advanced use is unlocked: Prime amplification, complex sequence building,
     * and the ability to teach the glyph to Construct companions.
     * Required for the most powerful interactions in Act III.
     */
    MASTERED(5, Integer.MAX_VALUE);

    private final int minFragments;
    private final int maxFragments;

    GlyphMastery(int minFragments, int maxFragments) {
        this.minFragments = minFragments;
        this.maxFragments = maxFragments;
    }

    /** Returns the minimum fragment count (inclusive) required to be at this tier. */
    public int getMinFragments() {
        return minFragments;
    }

    /** Returns the maximum fragment count (inclusive) for this tier, or {@link Integer#MAX_VALUE} for MASTERED. */
    public int getMaxFragments() {
        return maxFragments;
    }

    /**
     * Returns {@code true} if this mastery level is at least as high as the required minimum.
     *
     * @param minimum the minimum required tier
     */
    public boolean isAtLeast(GlyphMastery minimum) {
        return this.ordinal() >= minimum.ordinal();
    }

    /**
     * Returns {@code true} if this tier counts toward the Glyph Knowledge score.
     * A glyph contributes to the score only when it is {@link #PRACTICED} or {@link #MASTERED}.
     */
    public boolean countsTowardKnowledgeScore() {
        return this.ordinal() >= PRACTICED.ordinal();
    }

    /**
     * Derives the mastery tier from a raw fragment count.
     * Returns {@link #UNLEARNED} for 0 fragments and {@link #MASTERED} for 5 or more.
     *
     * @param fragmentCount the number of fragments the player has for a given glyph type
     * @return the corresponding mastery tier
     */
    public static GlyphMastery fromFragmentCount(int fragmentCount) {
        for (GlyphMastery tier : values()) {
            if (fragmentCount >= tier.minFragments && fragmentCount <= tier.maxFragments) {
                return tier;
            }
        }
        return fragmentCount < UNLEARNED.minFragments ? UNLEARNED : MASTERED;
    }

    @Override
    public String toString() {
        return "mayaan:glyph_mastery/" + name().toLowerCase();
    }
}
