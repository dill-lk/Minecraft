package net.mayaan.game.story;

/**
 * The five acts of the Mayaan story.
 *
 * <p>Acts provide the high-level narrative structure. Each act contains one or more
 * {@link StoryChapter chapters}, and each chapter contains a set of {@link StoryGoal goals}
 * the player must accomplish.
 *
 * <h2>Story Structure</h2>
 * <ol>
 *   <li>{@link #PROLOGUE} — Isle of First Light; meeting Ix; the first temple</li>
 *   <li>{@link #ACT_I} — The Shattered Lands; four factions; the Axis Temple revelation</li>
 *   <li>{@link #ACT_II} — Collecting the Ixchelic Stone shards; rebuilding the Astral Gate</li>
 *   <li>{@link #ACT_III} — Yaan; the truth about The Unraveling; the Final Choice</li>
 *   <li>{@link #EPILOGUE} — Three worlds, three time-jumps, the cycle continues</li>
 * </ol>
 */
public enum StoryAct {

    /**
     * The beginning — waking on the Isle of First Light with no memories,
     * finding the Stone Shard, meeting Ix, and discovering the buried temple.
     */
    PROLOGUE("prologue", "Prologue"),

    /**
     * The Shattered Lands — arriving on the continent of Xibalkaal, meeting all four
     * surviving factions, and learning the truth of The Unraveling at the Axis Temple.
     */
    ACT_I("act_i", "The Shattered Lands"),

    /**
     * The Gate of Stars — collecting the three shards of the Ixchelic Stone scattered
     * across the world's most dangerous places, then rebuilding the Astral Gate.
     */
    ACT_II("act_ii", "The Gate of Stars"),

    /**
     * Beyond the Veil — entering Yaan (the Mayaan afterworld), learning the full truth,
     * meeting Camazotz, and making the Final Choice that determines the world's fate.
     */
    ACT_III("act_iii", "Beyond the Veil"),

    /**
     * Three Worlds — the epilogue; three time-jumps showing how the world changed.
     * Each of the three endings produces a different epilogue sequence.
     * The final image is the same in all three: the cycle continues.
     */
    EPILOGUE("epilogue", "Three Worlds");

    private final String id;
    private final String displayName;

    StoryAct(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    /** Returns the unique identifier string for this act (e.g., {@code "act_i"}). */
    public String getId() {
        return id;
    }

    /** Returns the human-readable display name for this act. */
    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return "mayaan:act/" + id;
    }
}
