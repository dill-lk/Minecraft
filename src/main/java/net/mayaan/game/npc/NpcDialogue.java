package net.mayaan.game.npc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import net.mayaan.game.faction.Faction;
import net.mayaan.game.faction.FactionManager;
import net.mayaan.game.faction.FactionStanding;
import net.mayaan.game.magic.GlyphKnowledgeManager;
import net.mayaan.game.magic.GlyphMastery;
import net.mayaan.game.magic.GlyphType;
import net.mayaan.game.story.StoryChapter;
import net.mayaan.game.story.StoryManager;

/**
 * NPC Dialogue system.
 *
 * <p>A {@link DialogueScript} is an ordered sequence of {@link DialogueLine} entries
 * associated with a named NPC. Each line carries a translation key for its text and an
 * optional {@link DialogueCondition} that gates whether the player sees that line.
 *
 * <h2>How dialogue resolves</h2>
 * When a player opens a conversation with an NPC, the game iterates the NPC's scripts
 * in script-priority order. The first script whose {@link DialogueScript#isAvailableTo}
 * passes for the player is the one played. Within the chosen script, lines whose conditions
 * do not pass for the player are replaced by their fallback line (or silently skipped if no
 * fallback exists).
 *
 * <h2>Translation key conventions</h2>
 * <pre>
 *   npc.&lt;npc_id&gt;.script.&lt;script_id&gt;.line.&lt;n&gt;
 *   npc.&lt;npc_id&gt;.script.&lt;script_id&gt;.line.&lt;n&gt;.fallback
 * </pre>
 *
 * <h2>Usage example</h2>
 * <pre>
 *   List&lt;NpcDialogue.DialogueLine&gt; lines = script.resolveFor(playerId);
 *   for (NpcDialogue.DialogueLine line : lines) {
 *       displayLine(line.speaker(), line.translationKey());
 *   }
 * </pre>
 *
 * @see MayaanNpcs
 */
public final class NpcDialogue {

    private NpcDialogue() {}

    // ── Condition ─────────────────────────────────────────────────────────────

    /**
     * A condition that gates whether a specific dialogue line or entire dialogue script
     * is available to a player. All non-null fields must match for the condition to pass.
     *
     * <p>Null fields are ignored (match any value). Use {@link Builder} to construct.
     */
    public static final class DialogueCondition {

        private final StoryChapter requiredChapterOrLater;
        private final Faction requiredFaction;
        private final FactionStanding minimumStanding;
        private final GlyphType requiredGlyph;
        private final GlyphMastery minimumMastery;
        private final int minimumKnowledgeScore;

        private DialogueCondition(
                StoryChapter requiredChapterOrLater,
                Faction requiredFaction,
                FactionStanding minimumStanding,
                GlyphType requiredGlyph,
                GlyphMastery minimumMastery,
                int minimumKnowledgeScore) {
            this.requiredChapterOrLater = requiredChapterOrLater;
            this.requiredFaction = requiredFaction;
            this.minimumStanding = minimumStanding;
            this.requiredGlyph = requiredGlyph;
            this.minimumMastery = minimumMastery;
            this.minimumKnowledgeScore = minimumKnowledgeScore;
        }

        /**
         * Returns {@code true} if the player meets all non-null conditions.
         *
         * @param playerId the player to evaluate
         */
        public boolean test(UUID playerId) {
            // Chapter gate
            if (requiredChapterOrLater != null) {
                StoryChapter current = StoryManager.INSTANCE.getProgress(playerId).getCurrentChapter();
                if (current.ordinal() < requiredChapterOrLater.ordinal()) {
                    return false;
                }
            }
            // Faction standing gate
            if (requiredFaction != null && minimumStanding != null) {
                FactionStanding standing = FactionManager.INSTANCE
                        .getFactionData(playerId)
                        .getStanding(requiredFaction);
                if (standing.ordinal() < minimumStanding.ordinal()) {
                    return false;
                }
            }
            // Glyph mastery gate
            if (requiredGlyph != null && minimumMastery != null) {
                GlyphMastery mastery = GlyphKnowledgeManager.INSTANCE
                        .getMastery(playerId, requiredGlyph);
                if (mastery.ordinal() < minimumMastery.ordinal()) {
                    return false;
                }
            }
            // Knowledge score gate
            if (minimumKnowledgeScore > 0) {
                int score = GlyphKnowledgeManager.INSTANCE.getKnowledgeScore(playerId);
                if (score < minimumKnowledgeScore) {
                    return false;
                }
            }
            return true;
        }

        /** Returns the chapter required before this condition can pass. May be null. */
        public StoryChapter requiredChapterOrLater() {
            return requiredChapterOrLater;
        }

        /** Returns the faction required for standing checks. May be null. */
        public Faction requiredFaction() {
            return requiredFaction;
        }

        /** Returns the minimum faction standing required. May be null. */
        public FactionStanding minimumStanding() {
            return minimumStanding;
        }

        /** Builder for {@link DialogueCondition}. */
        public static final class Builder {

            private StoryChapter chapter;
            private Faction faction;
            private FactionStanding standing;
            private GlyphType glyph;
            private GlyphMastery mastery;
            private int knowledgeScore;

            /** Gates on story chapter: condition only passes at this chapter or later. */
            public Builder chapter(StoryChapter chapter) {
                this.chapter = chapter;
                return this;
            }

            /** Gates on faction standing: requires at least {@code standing} with {@code faction}. */
            public Builder standing(Faction faction, FactionStanding standing) {
                this.faction = Objects.requireNonNull(faction);
                this.standing = Objects.requireNonNull(standing);
                return this;
            }

            /** Gates on glyph mastery: requires at least {@code mastery} for {@code glyph}. */
            public Builder mastery(GlyphType glyph, GlyphMastery mastery) {
                this.glyph = Objects.requireNonNull(glyph);
                this.mastery = Objects.requireNonNull(mastery);
                return this;
            }

            /** Gates on total knowledge score (sum of all glyph knowledge). */
            public Builder knowledgeScore(int minimum) {
                this.knowledgeScore = minimum;
                return this;
            }

            /** Builds the condition. */
            public DialogueCondition build() {
                return new DialogueCondition(chapter, faction, standing, glyph, mastery, knowledgeScore);
            }
        }
    }

    // ── DialogueLine ──────────────────────────────────────────────────────────

    /**
     * A single line of dialogue.
     *
     * <p>The {@code translationKey} maps to a translation in {@code lang/en_us.json}.
     * If {@code condition} is non-null and does not pass for the player, the line is either
     * replaced by {@code fallbackKey} (if set) or silently omitted.
     */
    public static final class DialogueLine {

        private final String speaker;
        private final String translationKey;
        private final String fallbackKey;           // null = omit if condition fails
        private final DialogueCondition condition;  // null = always visible

        private DialogueLine(String speaker, String translationKey,
                String fallbackKey, DialogueCondition condition) {
            this.speaker = Objects.requireNonNull(speaker);
            this.translationKey = Objects.requireNonNull(translationKey);
            this.fallbackKey = fallbackKey;
            this.condition = condition;
        }

        /**
         * Creates an unconditional line (always displayed).
         *
         * @param speaker        the NPC's short identifier (e.g., {@code "elder_cenote"})
         * @param translationKey full translation key for the line text
         */
        public static DialogueLine always(String speaker, String translationKey) {
            return new DialogueLine(speaker, translationKey, null, null);
        }

        /**
         * Creates a conditional line with an optional fallback.
         *
         * @param speaker        speaker short ID
         * @param translationKey the preferred line (shown when condition passes)
         * @param fallbackKey    shown when condition fails (may be null to omit entirely)
         * @param condition      the condition to test
         */
        public static DialogueLine conditional(String speaker, String translationKey,
                String fallbackKey, DialogueCondition condition) {
            return new DialogueLine(speaker, translationKey, fallbackKey,
                    Objects.requireNonNull(condition));
        }

        /**
         * Resolves this line for a specific player.
         *
         * @return the translation key to display, or {@code null} if this line should be skipped
         */
        public String resolveFor(UUID playerId) {
            if (condition == null || condition.test(playerId)) {
                return translationKey;
            }
            return fallbackKey; // may be null — caller should skip null
        }

        /** Returns the speaker identifier. */
        public String speaker() {
            return speaker;
        }

        /** Returns the primary translation key. */
        public String translationKey() {
            return translationKey;
        }

        /** Returns the fallback key, or {@code null} if none. */
        public String fallbackKey() {
            return fallbackKey;
        }

        /** Returns the condition, or {@code null} if unconditional. */
        public DialogueCondition condition() {
            return condition;
        }
    }

    // ── DialogueScript ────────────────────────────────────────────────────────

    /**
     * An ordered sequence of {@link DialogueLine}s forming one NPC response script.
     *
     * <p>A single NPC may have multiple scripts (e.g., one per story chapter). The NPC
     * manager iterates scripts in order and plays the first one whose top-level condition
     * passes (or the first unconditional script if none have conditions).
     */
    public static final class DialogueScript {

        private final String scriptId;
        private final DialogueCondition condition;     // null = always eligible
        private final List<DialogueLine> lines;

        private DialogueScript(String scriptId, DialogueCondition condition,
                List<DialogueLine> lines) {
            this.scriptId = Objects.requireNonNull(scriptId);
            this.condition = condition;
            this.lines = List.copyOf(lines);
        }

        /**
         * Returns the list of lines for this player, with gated lines resolved or omitted.
         *
         * @param playerId the player to resolve for
         * @return immutable resolved line list (may be shorter than the raw list)
         */
        public List<DialogueLine> resolveFor(UUID playerId) {
            List<DialogueLine> resolved = new ArrayList<>(lines.size());
            for (DialogueLine line : lines) {
                String key = line.resolveFor(playerId);
                if (key != null) {
                    // Build a resolved (unconditional) copy so callers don't need to re-check
                    resolved.add(DialogueLine.always(line.speaker(), key));
                }
            }
            return Collections.unmodifiableList(resolved);
        }

        /**
         * Returns {@code true} if this script's top-level condition passes for the player,
         * or if the script has no top-level condition.
         */
        public boolean isAvailableTo(UUID playerId) {
            return condition == null || condition.test(playerId);
        }

        /** Returns the unique ID of this script. */
        public String scriptId() {
            return scriptId;
        }

        /** Returns the top-level condition (may be null). */
        public DialogueCondition condition() {
            return condition;
        }

        /** Returns the raw (unresolved) lines. */
        public List<DialogueLine> lines() {
            return lines;
        }

        /** Builder for {@link DialogueScript}. */
        public static final class Builder {

            private final String scriptId;
            private DialogueCondition condition;
            private final List<DialogueLine> lines = new ArrayList<>();

            public Builder(String scriptId) {
                this.scriptId = Objects.requireNonNull(scriptId);
            }

            /** Sets the top-level condition that gates this entire script. */
            public Builder condition(DialogueCondition condition) {
                this.condition = condition;
                return this;
            }

            /** Appends an unconditional line. */
            public Builder line(String speaker, String key) {
                lines.add(DialogueLine.always(speaker, key));
                return this;
            }

            /** Appends a conditional line with fallback. */
            public Builder conditionalLine(String speaker, String key,
                    String fallbackKey, DialogueCondition lineCondition) {
                lines.add(DialogueLine.conditional(speaker, key, fallbackKey, lineCondition));
                return this;
            }

            /** Builds the script. */
            public DialogueScript build() {
                return new DialogueScript(scriptId, condition, lines);
            }
        }
    }
}
