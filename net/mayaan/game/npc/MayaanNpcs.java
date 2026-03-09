package net.mayaan.game.npc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import net.mayaan.game.faction.Faction;
import net.mayaan.game.faction.FactionStanding;
import net.mayaan.game.magic.GlyphMastery;
import net.mayaan.game.magic.GlyphType;
import net.mayaan.game.npc.NpcDialogue.DialogueCondition;
import net.mayaan.game.npc.NpcDialogue.DialogueScript;
import net.mayaan.game.story.StoryChapter;

/**
 * Catalogue of all named NPCs in Xibalkaal, Yaan, and beyond.
 *
 * <p>Each NPC is represented as a {@link NpcEntry} that carries:
 * <ul>
 *   <li>A unique string ID (used as the speaker key and translation key base)</li>
 *   <li>A display name translation key</li>
 *   <li>The faction they belong to (may be {@code null} for unaffiliated)</li>
 *   <li>An ordered list of {@link DialogueScript}s</li>
 * </ul>
 *
 * <p>Scripts are tried in order; the first script whose condition passes for the
 * interacting player is played. The last script in every NPC's list is always
 * unconditional and serves as the catch-all default.
 *
 * <h2>The eight named NPCs</h2>
 * <ol>
 *   <li>{@link #ELDER_CENOTE} — Elder of the Rootweavers; the first faction leader the player meets</li>
 *   <li>{@link #TZON} — Leader of the Forgeborn; pragmatic, territorial, occasionally menacing</li>
 *   <li>{@link #EK} — The Star Callers' representative; ancient, watchful, often cryptic</li>
 *   <li>{@link #KAAN} — Iron Pact captain; the reluctant ally who knows more than she admits</li>
 *   <li>{@link #FORGOTTEN_KING} — Time-looped Mayaan king in Yaan; both the obstacle and the key</li>
 *   <li>{@link #TIDE_KEEPER} — Guardian of the Abyssal Coast sea temple; trades Ixchelic Shard Two</li>
 *   <li>{@link #THE_WARDEN} — Ancient Crystal Veins guardian; requires the Warden's Farewell codex to surrender</li>
 *   <li>{@link #THE_DREAMER} — The final optional boss; an ancient entity at the Dream Sea's deepest point</li>
 * </ol>
 *
 * @see NpcDialogue
 */
public final class MayaanNpcs {

    // ── 1. Elder Cenote — Rootweaver Elder ───────────────────────────────────

    /**
     * Elder Cenote — the oldest living Rootweaver and keeper of the sacred cenote where
     * the first ley-line was discovered. Sees the player as a disruption before a necessary one.
     *
     * <p>Cenote's dialogue advances through four states:
     * <ol>
     *   <li>First meeting: cautious, probing ("The jungle knows you. But do you know the jungle?")</li>
     *   <li>After Canopy Wall: grateful but reserved ("You moved the wall. Now what?")</li>
     *   <li>After gifting Root-Bound Crystal (Key 5): warm, confessional ("I should tell you what Ix really is.")</li>
     *   <li>Act III: sombre, resigned ("We did this once before. We called it progress.")</li>
     * </ol>
     */
    public static final NpcEntry ELDER_CENOTE = NpcEntry.builder("elder_cenote")
            .faction(Faction.ROOTWEAVERS)
            .script(new DialogueScript.Builder("act_iii")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.WAKING_CITY)
                            .build())
                    .line("elder_cenote", "npc.elder_cenote.script.act_iii.line.1")
                    .line("elder_cenote", "npc.elder_cenote.script.act_iii.line.2")
                    .line("elder_cenote", "npc.elder_cenote.script.act_iii.line.3")
                    .build())
            .script(new DialogueScript.Builder("key_given")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.GATE_REBUILT)
                            .standing(Faction.ROOTWEAVERS, FactionStanding.HONORED)
                            .build())
                    .line("elder_cenote", "npc.elder_cenote.script.key_given.line.1")
                    .line("elder_cenote", "npc.elder_cenote.script.key_given.line.2")
                    .conditionalLine("elder_cenote",
                            "npc.elder_cenote.script.key_given.line.3.lore",
                            "npc.elder_cenote.script.key_given.line.3.fallback",
                            new DialogueCondition.Builder()
                                    .knowledgeScore(3)
                                    .build())
                    .build())
            .script(new DialogueScript.Builder("post_canopy_wall")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.FORGEBORN_GAMBIT)
                            .build())
                    .line("elder_cenote", "npc.elder_cenote.script.post_canopy_wall.line.1")
                    .line("elder_cenote", "npc.elder_cenote.script.post_canopy_wall.line.2")
                    .build())
            .script(new DialogueScript.Builder("first_meeting")
                    .line("elder_cenote", "npc.elder_cenote.script.first_meeting.line.1")
                    .line("elder_cenote", "npc.elder_cenote.script.first_meeting.line.2")
                    .line("elder_cenote", "npc.elder_cenote.script.first_meeting.line.3")
                    .build())
            .build();

    // ── 2. Tzon — Forgeborn Leader ────────────────────────────────────────────

    /**
     * Tzon — founder and leader of Tz'ikin, the Forgeborn's forge-city carved into the
     * cooled volcanic formations of the Ember Wastes. Pragmatic, direct, proud.
     * Tzon's people salvaged Mayaan machinery for 800 years — she knows more about
     * how it works than she lets on.
     *
     * <p>Tzon's dialogue advances through three states:
     * <ol>
     *   <li>First meeting: territorial ("Tz'ikin doesn't need outsiders.")</li>
     *   <li>After Forgeborn Gambit: business-like ("You fixed something I couldn't. I owe you nothing. I'll help anyway.")</li>
     *   <li>Act III: rare vulnerability ("The machines were always counting down to something. I just didn't want to know what.")</li>
     * </ol>
     */
    public static final NpcEntry TZON = NpcEntry.builder("tzon")
            .faction(Faction.FORGEBORN)
            .script(new DialogueScript.Builder("act_iii")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.WAKING_CITY)
                            .build())
                    .line("tzon", "npc.tzon.script.act_iii.line.1")
                    .line("tzon", "npc.tzon.script.act_iii.line.2")
                    .build())
            .script(new DialogueScript.Builder("post_gambit")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.VOICES_IN_STATIC)
                            .standing(Faction.FORGEBORN, FactionStanding.ACCEPTED)
                            .build())
                    .line("tzon", "npc.tzon.script.post_gambit.line.1")
                    .line("tzon", "npc.tzon.script.post_gambit.line.2")
                    .conditionalLine("tzon",
                            "npc.tzon.script.post_gambit.line.3.technical",
                            "npc.tzon.script.post_gambit.line.3.basic",
                            new DialogueCondition.Builder()
                                    .mastery(GlyphType.CHANNEL, GlyphMastery.AWARE)
                                    .build())
                    .build())
            .script(new DialogueScript.Builder("first_meeting")
                    .line("tzon", "npc.tzon.script.first_meeting.line.1")
                    .line("tzon", "npc.tzon.script.first_meeting.line.2")
                    .build())
            .build();

    // ── 3. Ek — Star Callers Emissary ─────────────────────────────────────────

    /**
     * Ek — the Star Callers' representative in the physical world. An androgynous figure
     * of uncertain age who appears and disappears without warning at high-elevation locations.
     * The Star Callers have been watching the ley-lines for 3,000 years and understand
     * The Unraveling better than anyone alive — but sharing that knowledge is against
     * their traditions, and Ek walks a difficult line.
     *
     * <p>Ek's dialogue is unusually short — each line is carefully chosen. Ek never speaks
     * more than 3 lines at once. Most of the meaning is in what Ek does not say.
     */
    public static final NpcEntry EK = NpcEntry.builder("ek")
            .faction(Faction.STAR_CALLERS)
            .script(new DialogueScript.Builder("star_chart_given")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.GATE_REBUILT)
                            .standing(Faction.STAR_CALLERS, FactionStanding.TRUSTED)
                            .build())
                    .line("ek", "npc.ek.script.star_chart_given.line.1")
                    .line("ek", "npc.ek.script.star_chart_given.line.2")
                    .build())
            .script(new DialogueScript.Builder("late_act_ii")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.SEA_TEMPLES_DEPTH)
                            .build())
                    .line("ek", "npc.ek.script.late_act_ii.line.1")
                    .conditionalLine("ek",
                            "npc.ek.script.late_act_ii.line.2.lore",
                            "npc.ek.script.late_act_ii.line.2.basic",
                            new DialogueCondition.Builder()
                                    .knowledgeScore(5)
                                    .build())
                    .build())
            .script(new DialogueScript.Builder("first_meeting")
                    .line("ek", "npc.ek.script.first_meeting.line.1")
                    .line("ek", "npc.ek.script.first_meeting.line.2")
                    .line("ek", "npc.ek.script.first_meeting.line.3")
                    .build())
            .build();

    // ── 4. Kaan — Iron Pact Captain ───────────────────────────────────────────

    /**
     * Kaan — captain of the Iron Pact's forward unit and the only surviving Pact soldier
     * who was present at the original Maw sealing attempt 30 years ago. She's seen things
     * she can't explain, and she knows the player's arrival is connected to what she saw.
     * The Iron Pact's job is to patrol and contain, not to understand — a job she finds
     * increasingly insufficient.
     *
     * <p>Kaan gives the player the Codex Fragment {@code kaans_margin} after the Maw Breach,
     * and her dialogue in Act III is the closest any NPC comes to explaining what really
     * happened at The Unraveling.
     */
    public static final NpcEntry KAAN = NpcEntry.builder("kaan")
            .faction(Faction.IRON_PACT)
            .script(new DialogueScript.Builder("act_iii")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.WAKING_CITY)
                            .standing(Faction.IRON_PACT, FactionStanding.TRUSTED)
                            .build())
                    .line("kaan", "npc.kaan.script.act_iii.line.1")
                    .line("kaan", "npc.kaan.script.act_iii.line.2")
                    .line("kaan", "npc.kaan.script.act_iii.line.3")
                    .build())
            .script(new DialogueScript.Builder("post_maw_breach")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.IRON_PACT_BURIES)
                            .standing(Faction.IRON_PACT, FactionStanding.ACCEPTED)
                            .build())
                    .line("kaan", "npc.kaan.script.post_maw_breach.line.1")
                    .line("kaan", "npc.kaan.script.post_maw_breach.line.2")
                    .line("kaan", "npc.kaan.script.post_maw_breach.line.3")
                    .build())
            .script(new DialogueScript.Builder("first_meeting")
                    .line("kaan", "npc.kaan.script.first_meeting.line.1")
                    .line("kaan", "npc.kaan.script.first_meeting.line.2")
                    .build())
            .build();

    // ── 5. The Forgotten King ─────────────────────────────────────────────────

    /**
     * The Forgotten King — the last Glyph Council king, frozen in a temporal loop inside
     * Yaan. He is re-experiencing the same 72-hour window — the final three days before
     * The Unraveling — on an infinite cycle. He knows it is a loop. He has known for 3,000
     * years. He keeps choosing the same outcome anyway.
     *
     * <p>The Forgotten King's dialogue is Act III's emotional core. Breaking his loop
     * requires the player to give him the Council Vote Record codex and speak the seven
     * Glyph names in their Mayaan script — the first time any living person has done so
     * in three millennia.
     *
     * <p>His dialogue is gated by Yaan access (chapter {@link StoryChapter#WAKING_CITY}).
     * He does not speak to players who have not reached Yaan yet — he simply turns away.
     */
    public static final NpcEntry FORGOTTEN_KING = NpcEntry.builder("forgotten_king")
            .faction(null)
            .script(new DialogueScript.Builder("loop_broken")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.TRUTH_IN_AMBER)
                            .build())
                    .line("forgotten_king", "npc.forgotten_king.script.loop_broken.line.1")
                    .line("forgotten_king", "npc.forgotten_king.script.loop_broken.line.2")
                    .line("forgotten_king", "npc.forgotten_king.script.loop_broken.line.3")
                    .line("forgotten_king", "npc.forgotten_king.script.loop_broken.line.4")
                    .build())
            .script(new DialogueScript.Builder("council_vote_held")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.WAKING_CITY)
                            .knowledgeScore(5)
                            .build())
                    .line("forgotten_king", "npc.forgotten_king.script.council_vote_held.line.1")
                    .line("forgotten_king", "npc.forgotten_king.script.council_vote_held.line.2")
                    .conditionalLine("forgotten_king",
                            "npc.forgotten_king.script.council_vote_held.line.3.deep",
                            "npc.forgotten_king.script.council_vote_held.line.3.surface",
                            new DialogueCondition.Builder()
                                    .mastery(GlyphType.TRANSLATE, GlyphMastery.PRACTICED)
                                    .build())
                    .build())
            .script(new DialogueScript.Builder("in_yaan")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.WAKING_CITY)
                            .build())
                    .line("forgotten_king", "npc.forgotten_king.script.in_yaan.line.1")
                    .line("forgotten_king", "npc.forgotten_king.script.in_yaan.line.2")
                    .build())
            .script(new DialogueScript.Builder("turns_away")
                    .line("forgotten_king", "npc.forgotten_king.script.turns_away.line.1")
                    .build())
            .build();

    // ── 6. The Tide Keeper ────────────────────────────────────────────────────

    /**
     * The Tide Keeper — a semi-aquatic Mayaan being who has been maintaining the sea temple
     * for 3,000 years. Unlike the Hollow Knights, the Tide Keeper is not corrupted — it has
     * simply been waiting. For the one who carries the right fragment of Anima.
     *
     * <p>It traded away the Ixchelic Shard Two and Wanderer's Key 6 to the player on the
     * understanding that they would do what the Keeper itself cannot: leave the sea.
     *
     * <p>The Tide Keeper speaks in short declarative fragments. It has forgotten how to
     * conjugate verbs correctly after 3,000 years alone.
     */
    public static final NpcEntry TIDE_KEEPER = NpcEntry.builder("tide_keeper")
            .faction(null)
            .script(new DialogueScript.Builder("post_trade")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.FORGOTTEN_KING)
                            .build())
                    .line("tide_keeper", "npc.tide_keeper.script.post_trade.line.1")
                    .line("tide_keeper", "npc.tide_keeper.script.post_trade.line.2")
                    .build())
            .script(new DialogueScript.Builder("first_meeting")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.SEA_TEMPLES_DEPTH)
                            .build())
                    .line("tide_keeper", "npc.tide_keeper.script.first_meeting.line.1")
                    .line("tide_keeper", "npc.tide_keeper.script.first_meeting.line.2")
                    .line("tide_keeper", "npc.tide_keeper.script.first_meeting.line.3")
                    .build())
            .script(new DialogueScript.Builder("not_ready")
                    .line("tide_keeper", "npc.tide_keeper.script.not_ready.line.1")
                    .build())
            .build();

    // ── 7. The Warden ─────────────────────────────────────────────────────────

    /**
     * The Warden of the Abyss — the Crystal Veins' ancient guardian Construct.
     * The most powerful Construct still operational. It was given one instruction
     * 3,000 years ago and has been following it without deviation: "Guard everything below."
     *
     * <p>The Warden will not negotiate. It will not stand down because the player asks.
     * It will only stand down when presented with the Warden's Farewell codex fragment —
     * the Council's preserved message, which contains an override phrase that only
     * the Warden recognizes.
     *
     * <p>Without the codex, the Warden says nothing. With it, it says exactly three lines
     * and then never speaks again.
     */
    public static final NpcEntry THE_WARDEN = NpcEntry.builder("the_warden")
            .faction(null)
            .script(new DialogueScript.Builder("farewell_presented")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.SEA_TEMPLES_DEPTH)
                            .knowledgeScore(2)
                            .build())
                    .line("the_warden", "npc.the_warden.script.farewell_presented.line.1")
                    .line("the_warden", "npc.the_warden.script.farewell_presented.line.2")
                    .line("the_warden", "npc.the_warden.script.farewell_presented.line.3")
                    .build())
            .script(new DialogueScript.Builder("silence")
                    .line("the_warden", "npc.the_warden.script.silence.line.1")
                    .build())
            .build();

    // ── 8. The Dreamer ────────────────────────────────────────────────────────

    /**
     * The Dreamer — an entity that has been living at the bottom of the Dream Sea since
     * before the Mayaan civilization existed. It is not a Maw entity, not a Mayaan, not
     * anything with a name. It is simply old and watching.
     *
     * <p>The Dreamer is the game's final optional encounter. It does not attack the player
     * on sight — it converses first. Whether it becomes a boss fight depends on what the
     * player says and whether they have accumulated high enough Glyph knowledge score (6+).
     *
     * <p>With score 6+: the player can speak with the Dreamer as an equal, receive the
     * final truth about what The Unraveling was actually trying to accomplish, and leave.
     * Below score 6: the Dreamer decides the player is "not ready" and forces the
     * boss encounter anyway.
     *
     * <p>The Dreamer's dialogue represents the game's ultimate lore payoff.
     */
    public static final NpcEntry THE_DREAMER = NpcEntry.builder("the_dreamer")
            .faction(null)
            .script(new DialogueScript.Builder("full_understanding")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.MAW_STIRS)
                            .knowledgeScore(6)
                            .mastery(GlyphType.TRANSLATE, GlyphMastery.MASTERED)
                            .build())
                    .line("the_dreamer", "npc.the_dreamer.script.full_understanding.line.1")
                    .line("the_dreamer", "npc.the_dreamer.script.full_understanding.line.2")
                    .line("the_dreamer", "npc.the_dreamer.script.full_understanding.line.3")
                    .line("the_dreamer", "npc.the_dreamer.script.full_understanding.line.4")
                    .line("the_dreamer", "npc.the_dreamer.script.full_understanding.line.5")
                    .build())
            .script(new DialogueScript.Builder("partial_understanding")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.MAW_STIRS)
                            .knowledgeScore(3)
                            .build())
                    .line("the_dreamer", "npc.the_dreamer.script.partial_understanding.line.1")
                    .line("the_dreamer", "npc.the_dreamer.script.partial_understanding.line.2")
                    .line("the_dreamer", "npc.the_dreamer.script.partial_understanding.line.3")
                    .build())
            .script(new DialogueScript.Builder("not_ready")
                    .condition(new DialogueCondition.Builder()
                            .chapter(StoryChapter.MAW_STIRS)
                            .build())
                    .line("the_dreamer", "npc.the_dreamer.script.not_ready.line.1")
                    .line("the_dreamer", "npc.the_dreamer.script.not_ready.line.2")
                    .build())
            .script(new DialogueScript.Builder("silence")
                    .line("the_dreamer", "npc.the_dreamer.script.silence.line.1")
                    .build())
            .build();

    // ── NpcEntry ─────────────────────────────────────────────────────────────

    private MayaanNpcs() {}

    /**
     * Represents a named NPC: their ID, display name key, faction, and ordered dialogue scripts.
     */
    public static final class NpcEntry {

        private final String npcId;
        private final String displayNameKey;
        private final Faction faction;
        private final List<DialogueScript> scripts;

        private NpcEntry(String npcId, Faction faction, List<DialogueScript> scripts) {
            this.npcId = npcId;
            this.displayNameKey = "npc." + npcId + ".name";
            this.faction = faction;
            this.scripts = List.copyOf(scripts);
        }

        /**
         * Returns the first dialogue script available to the given player, in priority order.
         * Falls back to the last script (always unconditional by convention) if no other
         * script's condition passes.
         *
         * @param playerId the player interacting with this NPC
         * @return a non-null script (the last script is always the fallback)
         */
        public DialogueScript scriptFor(UUID playerId) {
            for (DialogueScript script : scripts) {
                if (script.isAvailableTo(playerId)) {
                    return script;
                }
            }
            // The last script is always unconditional — this path is a safety guard
            return scripts.get(scripts.size() - 1);
        }

        /** Returns this NPC's unique ID. */
        public String npcId() {
            return npcId;
        }

        /** Returns the translation key for this NPC's display name. */
        public String displayNameKey() {
            return displayNameKey;
        }

        /** Returns the faction this NPC belongs to, or {@code null} if unaffiliated. */
        public Faction faction() {
            return faction;
        }

        /** Returns all dialogue scripts in priority order. */
        public List<DialogueScript> scripts() {
            return scripts;
        }

        static Builder builder(String npcId) {
            return new Builder(npcId);
        }

        static final class Builder {
            private final String npcId;
            private Faction faction;
            private final List<DialogueScript> scripts = new ArrayList<>();

            Builder(String npcId) {
                this.npcId = npcId;
            }

            Builder faction(Faction faction) {
                this.faction = faction;
                return this;
            }

            Builder script(DialogueScript script) {
                scripts.add(script);
                return this;
            }

            NpcEntry build() {
                return new NpcEntry(npcId, faction, scripts);
            }
        }
    }
}
