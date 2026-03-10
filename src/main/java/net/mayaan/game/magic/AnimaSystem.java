package net.mayaan.game.magic;

/**
 * Tracks and manages Anima — the life-force of Xibalkaal.
 *
 * Anima is drawn from ley-lines and the environment. Using too much too fast causes
 * Anima Drought: a localized deadening of the surrounding ecosystem.
 * Players expend Anima to cast Glyph magic; the world regenerates it naturally over time.
 */
public final class AnimaSystem {
    /** Maximum Anima a player can hold by default. */
    public static final int DEFAULT_MAX_ANIMA = 100;

    /**
     * The amount of Anima naturally regenerated per game tick when standing on a ley-line.
     */
    public static final float LEYLINE_REGEN_PER_TICK = 0.5f;

    /**
     * The amount of Anima naturally regenerated per game tick in normal terrain.
     */
    public static final float NORMAL_REGEN_PER_TICK = 0.05f;

    /**
     * Threshold at which an Anima Drought begins affecting the local environment.
     * If cumulative Anima drawn in a region exceeds this per day, ecological damage occurs.
     */
    public static final int DROUGHT_THRESHOLD = 500;

    /**
     * The Anima cost of a basic Glyph invocation (e.g., SEEK, ILLUMINATE).
     */
    public static final int BASIC_GLYPH_COST = 10;

    /**
     * The Anima cost of a major Glyph invocation (e.g., TRANSLATE, CHANNEL).
     */
    public static final int MAJOR_GLYPH_COST = 30;

    /**
     * The Anima cost of a Prime Glyph (alters an entire dimension's rules).
     * Only usable at full Glyph Council strength; consumes all available Anima.
     */
    public static final int PRIME_GLYPH_COST = Integer.MAX_VALUE;

    private AnimaSystem() {}
}
