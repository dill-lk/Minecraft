package net.mayaan.game.magic;

import java.util.UUID;

/**
 * Glyph Casting — the core logic for invoking a Mayaan Glyph.
 *
 * <p>This class resolves whether a player can cast a given glyph, determines its Anima
 * cost, checks mastery prerequisites, deducts Anima, and returns a {@link CastResult}
 * that downstream code uses to apply effects.
 *
 * <h2>Usage</h2>
 * <pre>
 *   GlyphCasting.CastResult result = GlyphCasting.tryCast(
 *       playerId, GlyphType.SEEK, CastTier.BASIC);
 *
 *   switch (result.outcome()) {
 *       case SUCCESS    -> applySeekEffect(playerId);
 *       case NO_ANIMA   -> sendMessage(player, "Not enough Anima.");
 *       case NO_MASTERY -> sendMessage(player, "You haven't learned this glyph well enough.");
 *       case IN_DROUGHT -> sendMessage(player, "The land is drained. Wait for Anima to return.");
 *   }
 * </pre>
 *
 * <h2>Cast Tiers</h2>
 * <ul>
 *   <li>{@link CastTier#BASIC} — minor glyph effect (e.g., a brief SEEK pulse).
 *       Costs {@link AnimaSystem#BASIC_GLYPH_COST}. Requires {@link GlyphMastery#AWARE}.</li>
 *   <li>{@link CastTier#MAJOR} — significant effect (e.g., TRANSLATE opens a portal).
 *       Costs {@link AnimaSystem#MAJOR_GLYPH_COST}. Requires {@link GlyphMastery#PRACTICED}.</li>
 *   <li>{@link CastTier#PRIME} — world-altering effect. Consumes all Anima.
 *       Requires {@link GlyphMastery#MASTERED}. Council strength needed (story gate).</li>
 * </ul>
 *
 * <h2>Drought Behaviour</h2>
 * Casting during Anima Drought does not immediately fail — a drought is a warning,
 * not a hard block. However:
 * <ul>
 *   <li>Glyph effects are weakened (range/duration halved; determined by caller)</li>
 *   <li>The {@link CastOutcome} will be {@link CastOutcome#SUCCESS_DROUGHT} to signal
 *       that effects should be applied at reduced power</li>
 * </ul>
 *
 * @see AnimaManager
 * @see GlyphKnowledgeManager
 * @see AnimaSystem
 */
public final class GlyphCasting {

    private GlyphCasting() {}

    // ── Public API ────────────────────────────────────────────────────────────

    /**
     * Attempts to cast a glyph for the given player.
     *
     * <p>This method:
     * <ol>
     *   <li>Checks the player has at least the required mastery tier</li>
     *   <li>Checks the player has enough Anima (even during a drought)</li>
     *   <li>Deducts the Anima cost if all checks pass</li>
     *   <li>Returns a {@link CastResult} describing the outcome</li>
     * </ol>
     *
     * @param playerId the casting player's UUID
     * @param glyph    the glyph to cast
     * @param tier     the tier of the cast ({@link CastTier})
     * @return the result of the cast attempt — never null
     */
    public static CastResult tryCast(UUID playerId, GlyphType glyph, CastTier tier) {
        // ── Mastery check ─────────────────────────────────────────────────────
        GlyphMastery mastery = GlyphKnowledgeManager.INSTANCE.getMastery(playerId, glyph);
        GlyphMastery required = tier.requiredMastery();
        if (mastery.ordinal() < required.ordinal()) {
            return CastResult.failure(CastOutcome.NO_MASTERY, mastery, 0);
        }

        // ── Anima check ───────────────────────────────────────────────────────
        int cost = resolvedCost(tier, playerId);
        PlayerAnimaData anima = AnimaManager.INSTANCE.getAnimaData(playerId);

        if (!anima.canSpend(cost)) {
            return CastResult.failure(CastOutcome.NO_ANIMA, mastery, cost);
        }

        // ── Deduct Anima ──────────────────────────────────────────────────────
        boolean spent = anima.spend(cost);
        if (!spent) {
            // Should not happen — canSpend checked above — but guard defensively
            return CastResult.failure(CastOutcome.NO_ANIMA, mastery, cost);
        }

        // ── Drought outcome ───────────────────────────────────────────────────
        if (anima.isInDrought()) {
            return CastResult.success(CastOutcome.SUCCESS_DROUGHT, mastery, cost, glyph, tier);
        }
        return CastResult.success(CastOutcome.SUCCESS, mastery, cost, glyph, tier);
    }

    /**
     * Returns the Anima cost for the given tier, accounting for any player-specific
     * modifiers. Currently no player-specific modifiers exist, but this provides a
     * future hook for faction perks, glyph upgrades, etc.
     *
     * @param tier     the cast tier
     * @param playerId the player's UUID
     */
    public static int resolvedCost(CastTier tier, UUID playerId) {
        return switch (tier) {
            case BASIC -> AnimaSystem.BASIC_GLYPH_COST;
            case MAJOR -> AnimaSystem.MAJOR_GLYPH_COST;
            case PRIME -> AnimaManager.INSTANCE.getMaxAnima(playerId); // consumes all Anima
        };
    }

    // ── Nested types ──────────────────────────────────────────────────────────

    /**
     * The tier of a glyph cast. Determines Anima cost and required mastery.
     */
    public enum CastTier {

        /**
         * Basic cast — brief, targeted effect.
         * Anima cost: {@link AnimaSystem#BASIC_GLYPH_COST}.
         * Required mastery: {@link GlyphMastery#AWARE}.
         */
        BASIC(GlyphMastery.AWARE),

        /**
         * Major cast — significant, area or sustained effect.
         * Anima cost: {@link AnimaSystem#MAJOR_GLYPH_COST}.
         * Required mastery: {@link GlyphMastery#PRACTICED}.
         */
        MAJOR(GlyphMastery.PRACTICED),

        /**
         * Prime cast — world-altering. Consumes all Anima; requires full mastery.
         * Usable only at full Glyph Council strength (story gate).
         */
        PRIME(GlyphMastery.MASTERED);

        private final GlyphMastery requiredMastery;

        CastTier(GlyphMastery requiredMastery) {
            this.requiredMastery = requiredMastery;
        }

        /** Returns the minimum mastery tier required to cast at this level. */
        public GlyphMastery requiredMastery() {
            return requiredMastery;
        }
    }

    /**
     * The outcome of a cast attempt.
     */
    public enum CastOutcome {

        /** Cast succeeded normally. Apply effects at full power. */
        SUCCESS,

        /**
         * Cast succeeded but the player is in an Anima Drought.
         * Apply effects at reduced power (range and duration halved).
         */
        SUCCESS_DROUGHT,

        /**
         * Cast failed: the player does not have enough Anima.
         * Do not apply any effects. Notify the player.
         */
        NO_ANIMA,

        /**
         * Cast failed: the player's mastery of this glyph is too low.
         * Do not apply any effects. Suggest collecting more glyph fragments.
         */
        NO_MASTERY;

        /** Returns {@code true} if the cast produced any effect (successful or drought). */
        public boolean isSuccess() {
            return this == SUCCESS || this == SUCCESS_DROUGHT;
        }
    }

    /**
     * The result of a {@link GlyphCasting#tryCast} call.
     *
     * <p>Contains the outcome, the player's current mastery of the cast glyph,
     * the Anima cost (even on failure, to allow UI display), and on success,
     * the glyph type and tier that were cast.
     */
    public static final class CastResult {

        private final CastOutcome outcome;
        private final GlyphMastery mastery;
        private final int animaCost;
        private final GlyphType glyph;   // null on failure
        private final CastTier tier;     // null on failure

        private CastResult(CastOutcome outcome, GlyphMastery mastery, int animaCost,
                GlyphType glyph, CastTier tier) {
            this.outcome = outcome;
            this.mastery = mastery;
            this.animaCost = animaCost;
            this.glyph = glyph;
            this.tier = tier;
        }

        static CastResult failure(CastOutcome outcome, GlyphMastery mastery, int cost) {
            return new CastResult(outcome, mastery, cost, null, null);
        }

        static CastResult success(CastOutcome outcome, GlyphMastery mastery, int cost,
                GlyphType glyph, CastTier tier) {
            return new CastResult(outcome, mastery, cost, glyph, tier);
        }

        /** Returns the outcome of the cast attempt. */
        public CastOutcome outcome() {
            return outcome;
        }

        /** Returns the player's mastery of the glyph at cast time. */
        public GlyphMastery mastery() {
            return mastery;
        }

        /** Returns the Anima cost that was (or would have been) spent. */
        public int animaCost() {
            return animaCost;
        }

        /**
         * Returns the glyph that was cast.
         * Only valid when {@link #outcome()} {@link CastOutcome#isSuccess()}.
         * Returns {@code null} on failure.
         */
        public GlyphType glyph() {
            return glyph;
        }

        /**
         * Returns the tier of the cast.
         * Only valid when {@link #outcome()} {@link CastOutcome#isSuccess()}.
         * Returns {@code null} on failure.
         */
        public CastTier tier() {
            return tier;
        }

        /** Convenience shorthand for {@link CastOutcome#isSuccess()}. */
        public boolean succeeded() {
            return outcome.isSuccess();
        }
    }
}
