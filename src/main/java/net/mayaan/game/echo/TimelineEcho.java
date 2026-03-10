package net.mayaan.game.echo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.mayaan.game.magic.GlyphMastery;
import net.mayaan.game.magic.GlyphType;

/**
 * A Timeline Echo — a preserved memory that the player can witness by interacting with
 * a Glyph Shard or entering an Anima-saturated location.
 *
 * <p>Timeline Echoes are the primary method by which Mayaan history is delivered to the
 * player. They present as a cinematic sequence of {@link EchoLine} objects — dialogue
 * and narration from the past — playing out in the player's space without pausing gameplay.
 *
 * <h2>Trigger</h2>
 * Echoes have an associated story goal ID ({@link #getTriggerGoalId()}) that corresponds
 * to the {@link net.mayaan.game.story.StoryGoal} whose completion causes this echo to play.
 * The echo plays on first trigger; subsequent visits replay it if the player has the
 * Stone Shard equipped.
 *
 * <h2>Knowledge Gating</h2>
 * Some echoes require a minimum {@link net.mayaan.game.magic.PlayerGlyphKnowledge Glyph Knowledge score}
 * before they can be fully experienced. Low-knowledge players may see the echo begin but
 * the language fragments if they lack sufficient context. A required specific glyph at a
 * specific mastery level can also be set ({@link #getRequiredGlyph()} / {@link #getRequiredMastery()}).
 *
 * <h2>Location</h2>
 * Every echo has a human-readable {@link #getLocation()} description used for the "Echo
 * unlocked" notification and the journal entry.
 *
 * @see EchoLine
 * @see TimelineEchoRegistry
 */
public final class TimelineEcho {

    private final String id;
    private final String displayName;
    private final String location;
    private final String triggerGoalId;
    private final int requiredKnowledgeScore;
    private final GlyphType requiredGlyph;
    private final GlyphMastery requiredMastery;
    private final List<EchoLine> lines;

    private TimelineEcho(Builder builder) {
        this.id = builder.id;
        this.displayName = builder.displayName;
        this.location = builder.location;
        this.triggerGoalId = builder.triggerGoalId;
        this.requiredKnowledgeScore = builder.requiredKnowledgeScore;
        this.requiredGlyph = builder.requiredGlyph;
        this.requiredMastery = builder.requiredMastery;
        this.lines = Collections.unmodifiableList(new ArrayList<>(builder.lines));
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    /** Returns the unique identifier for this echo (e.g., {@code "scouts_warning"}). */
    public String getId() {
        return id;
    }

    /** Returns the human-readable display name for this echo (shown in the journal). */
    public String getDisplayName() {
        return displayName;
    }

    /** Returns a brief description of where this echo occurs. */
    public String getLocation() {
        return location;
    }

    /**
     * Returns the story goal ID whose completion triggers this echo.
     * Corresponds to a {@link net.mayaan.game.story.StoryGoal#getId()}.
     */
    public String getTriggerGoalId() {
        return triggerGoalId;
    }

    /**
     * Returns the minimum Glyph Knowledge score required to experience this echo in full.
     * Players below this threshold witness the echo but some lines are fragmented.
     * Returns 0 if no score is required.
     */
    public int getRequiredKnowledgeScore() {
        return requiredKnowledgeScore;
    }

    /**
     * Returns the specific glyph type required for full echo experience, or {@code null}
     * if no specific glyph requirement exists.
     */
    public GlyphType getRequiredGlyph() {
        return requiredGlyph;
    }

    /**
     * Returns the mastery level required for the {@link #getRequiredGlyph()}, or
     * {@link GlyphMastery#UNLEARNED} if no specific mastery is required.
     */
    public GlyphMastery getRequiredMastery() {
        return requiredMastery;
    }

    /** Returns the ordered list of lines that make up this echo. Never empty. */
    public List<EchoLine> getLines() {
        return lines;
    }

    /**
     * Returns {@code true} if the given knowledge score and optional glyph mastery are
     * sufficient to experience this echo in full.
     *
     * @param knowledgeScore  the player's Glyph Knowledge score (0–7)
     * @param glyphType       the glyph type to check against the specific requirement;
     *                        may be null if no specific glyph needs checking
     * @param mastery         the player's mastery in {@code glyphType}; ignored if
     *                        {@code glyphType} is null or this echo has no glyph requirement
     */
    public boolean canExperienceInFull(int knowledgeScore,
                                        GlyphType glyphType,
                                        GlyphMastery mastery) {
        if (knowledgeScore < requiredKnowledgeScore) {
            return false;
        }
        if (requiredGlyph != null) {
            // A specific glyph is required: the caller must supply the matching type AND mastery.
            return requiredGlyph.equals(glyphType)
                    && mastery != null
                    && mastery.isAtLeast(requiredMastery);
        }
        return true;
    }

    @Override
    public String toString() {
        return "mayaan:echo/" + id;
    }

    // ── Builder ───────────────────────────────────────────────────────────────

    /**
     * Creates a new builder for a {@link TimelineEcho}.
     *
     * @param id the unique identifier (e.g., {@code "scouts_warning"})
     */
    public static Builder builder(String id) {
        return new Builder(id);
    }

    /** Fluent builder for {@link TimelineEcho}. */
    public static final class Builder {

        private final String id;
        private String displayName = "";
        private String location = "";
        private String triggerGoalId = "";
        private int requiredKnowledgeScore = 0;
        private GlyphType requiredGlyph = null;
        private GlyphMastery requiredMastery = GlyphMastery.UNLEARNED;
        private final List<EchoLine> lines = new ArrayList<>();

        private Builder(String id) {
            if (id == null || id.isEmpty()) {
                throw new IllegalArgumentException("TimelineEcho id must not be null or empty");
            }
            this.id = id;
        }

        /** Sets the human-readable display name. */
        public Builder displayName(String displayName) {
            this.displayName = displayName;
            return this;
        }

        /** Sets a brief description of the location where this echo occurs. */
        public Builder location(String location) {
            this.location = location;
            return this;
        }

        /** Sets the story goal ID whose completion triggers this echo. */
        public Builder triggeredBy(String goalId) {
            this.triggerGoalId = goalId;
            return this;
        }

        /**
         * Sets the minimum Glyph Knowledge score needed for full echo experience.
         * Players below this score will find some lines fragmented.
         */
        public Builder requiresKnowledgeScore(int score) {
            this.requiredKnowledgeScore = score;
            return this;
        }

        /**
         * Sets a specific glyph and mastery level required for full echo experience.
         *
         * @param glyphType the required glyph type
         * @param mastery   the minimum required mastery level
         */
        public Builder requiresGlyph(GlyphType glyphType, GlyphMastery mastery) {
            this.requiredGlyph = glyphType;
            this.requiredMastery = mastery;
            return this;
        }

        /** Appends a narration line (no attributed speaker). */
        public Builder narration(String text) {
            lines.add(EchoLine.narration(text));
            return this;
        }

        /** Appends a dialogue line attributed to the given speaker. */
        public Builder dialogue(String speaker, String text) {
            lines.add(EchoLine.dialogue(speaker, text));
            return this;
        }

        /**
         * Builds the {@link TimelineEcho}.
         *
         * @throws IllegalStateException if no lines have been added
         */
        public TimelineEcho build() {
            if (lines.isEmpty()) {
                throw new IllegalStateException("TimelineEcho '" + id + "' must have at least one line");
            }
            return new TimelineEcho(this);
        }
    }
}
