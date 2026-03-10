package net.mayaan.game.echo;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import net.mayaan.game.magic.GlyphMastery;
import net.mayaan.game.magic.GlyphType;

/**
 * Registry of all major {@link TimelineEcho Timeline Echoes} in the Mayaan story.
 *
 * <p>Timeline Echoes are the primary method by which Mayaan history is delivered to the
 * player. Each echo is built once at class-load time and stored by ID for fast lookup.
 *
 * <h2>Canonical Echoes</h2>
 * <pre>
 *   SCOUTS_WARNING          — Prologue. The ancient Scout's message; the first call to action.
 *   WARDEN_STANDS_DOWN      — Act I Chapter 2. The three dormant Wardens respond to the Stone Shard.
 *   YAAN_SIGNAL             — Act I Chapter 3. The terrified voice from the afterworld.
 *   COUNCILS_FINAL_MEMORY   — Act I Climax. The Axis Temple. The full truth of The Unraveling.
 *   WARDEN_OF_ABYSS_MEMORY  — Act II Chapter 5. The Council's farewell message to the lonely Warden.
 *   TIDE_KEEPERS_VIGIL      — Act II Chapter 6. The Tide Keeper's purpose, told in its own words.
 *   FORGOTTEN_KINGS_DAY     — Act II Chapter 7. The King reliving his last day; the loop mechanism.
 *   GATE_ACTIVATION_WARNING — Act II Climax. The frightened Yaan signal vs. Ix-Chel's voice.
 *   IX_LEAVES_YAAN          — Act III Chapter 9. Ix's impossible departure from the afterworld.
 *   GLYPH_COUNCIL_VOTE      — Act III Chapter 10. The Council chamber: the vote, the alternative.
 *   SCOUTS_DEFIANCE         — Act III Chapter 10. The Scout follows the failed plan without blessing.
 *   CAMAZOTZ_SPEAKS         — Act III Chapter 11. Entropy communicates directly.
 * </pre>
 *
 * @see TimelineEcho
 * @see EchoLine
 */
public final class TimelineEchoRegistry {

    private static final Map<String, TimelineEcho> BY_ID;

    // ── Echo definitions ──────────────────────────────────────────────────────

    /**
     * Prologue echo — triggered at {@code read_first_message} in the Isle of First Light temple.
     * The ancient Scout speaks directly across the centuries, setting the entire main quest in motion.
     * No knowledge requirement — this is the player's very first timeline echo experience.
     */
    public static final TimelineEcho SCOUTS_WARNING = TimelineEcho.builder("scouts_warning")
            .displayName("The Scout's Warning")
            .location("Isle of First Light — Temple Receiving Chamber")
            .triggeredBy("read_first_message")
            .narration(
                    "The Stone Shard pulses. Light floods the chamber — not the light of torches "
                    + "but of memory, concentrated and ancient. A woman stands before you, "
                    + "translucent and perfectly still. She is wearing Mayaan Scout colours. "
                    + "She is placing this Codex Fragment on the pedestal where you found it.")
            .dialogue("Ix-Channa (Scout)",
                    "If you are reading this — if the island's lock recognized you — then you "
                    + "carry the Wanderer's Mark. We don't know who you will be, or when you "
                    + "will come. We only know the ley-lines told us someone would.")
            .dialogue("Ix-Channa (Scout)",
                    "Xibalkaal is in danger again. The Maw is eroding its seal. You must go "
                    + "to the continent. Find the Axis Temple. The Council's final memory is "
                    + "preserved there. You will understand everything when you hear it.")
            .narration(
                    "She pauses. In the preserved light of the memory, she turns and looks "
                    + "directly at you — not at the chamber, not at where she placed the "
                    + "fragment. At you. As if she can see you across three thousand years.")
            .dialogue("Ix-Channa (Scout)", "We're sorry for what you're about to learn.")
            .narration("The echo fades. The chamber is dark again. Ix is watching from the doorway.")
            .build();

    /**
     * Act I Chapter 2 echo — triggered at {@code resolve_warden_crisis} in Tz'ikin.
     * The three dormant Wardens recognize the Stone Shard and stand down,
     * revealing that the Shard is a Warden's Key.
     * Requires FRAGMENTARY knowledge of SEEK (the Shard's glyph).
     */
    public static final TimelineEcho WARDEN_STANDS_DOWN = TimelineEcho.builder("warden_stands_down")
            .displayName("The Key is Recognized")
            .location("Tz'ikin — Resonance Pillar Chamber")
            .triggeredBy("resolve_warden_crisis")
            .requiresGlyph(GlyphType.SEEK, GlyphMastery.FRAGMENTARY)
            .narration(
                    "The three Warden Constructs are still. Their weapon-limbs have not retracted. "
                    + "Then you raise the Stone Shard — and they see it. A frequency hum rises from "
                    + "all three simultaneously. Their targeting systems shift. Then, one by one, "
                    + "they lower their weapons.")
            .narration(
                    "A brief timeline echo pulses through the chamber — not a full replay, but a "
                    + "flash: a Glyph Council member, urgent, pressing the Shard against a stone "
                    + "reader. A voice, too fragmented to understand, giving instructions. "
                    + "The Shard glowing the same way it glows now.")
            .narration(
                    "The echo is gone as quickly as it came. The Wardens are waiting. "
                    + "Tzon is staring at the Shard in your hand with an expression you've never "
                    + "seen on his face before. Behind you, Ix has gone completely still.")
            .build();

    /**
     * Act I Chapter 3 echo — triggered at {@code hear_frightened_message} at the Star Callers' observatory.
     * The amplified signal from Yaan resolves into a terrified voice warning against opening the Gate.
     * Requires knowledge score 1 (player has practiced at least one glyph).
     */
    public static final TimelineEcho YAAN_SIGNAL = TimelineEcho.builder("yaan_signal")
            .displayName("The Voice from Yaan")
            .location("Serpent Highlands — Star Callers' High Observatory")
            .triggeredBy("hear_frightened_message")
            .requiresKnowledgeScore(1)
            .narration(
                    "Ek's instruments have been tuned for weeks. The Stone Shard acts as an "
                    + "amplifier the observatory towers were never designed to use alone. The "
                    + "signal resolves — static becoming syllables, syllables becoming a voice.")
            .narration(
                    "It speaks in Old Mayaan. You understand fragments — enough. The voice is "
                    + "not calm. It is not the measured broadcast of a message placed deliberately. "
                    + "It is frightened.")
            .dialogue("Voice from Yaan (fragmented)",
                    "...the seal is weakening... do not... do not open the Gate until... "
                    + "the key count is complete... we are still here... we can still hold... "
                    + "but the Maw is...")
            .narration("The signal cuts. The instruments go dark. Ek does not move for a long moment.")
            .dialogue("Ek", "That is not the signal I have been receiving for twenty years.")
            .dialogue("Ek", "That signal was afraid of me.")
            .build();

    /**
     * Act I Climax echo — triggered at {@code councils_final_memory} in the Axis Temple.
     * The full truth of The Unraveling, delivered as a complete council session Timeline Echo.
     * Requires knowledge score 2 (practicing glyph sequences is necessary to enter).
     */
    public static final TimelineEcho COUNCILS_FINAL_MEMORY = TimelineEcho.builder("councils_final_memory")
            .displayName("The Council's Final Memory")
            .location("Axis Temple — Central Memory Chamber")
            .triggeredBy("councils_final_memory")
            .requiresKnowledgeScore(2)
            .narration(
                    "The chamber at the heart of the Axis Temple is exactly as the Codex Fragment "
                    + "described it: built for this. The Stone Shard unlocks the final seal. "
                    + "The room fills with preserved light — seven figures around a stone table, "
                    + "mid-argument. The Glyph Council, in the hours before The Unraveling.")
            .dialogue("Glyph-Keeper Haubal (Council Chair)",
                    "The Maw is feeding through the Gate. We have hours, not days. "
                    + "The Sacrifice is the only option that closes it in time.")
            .dialogue("Glyph-Keeper Xalon",
                    "The Seven Keys method is theoretically sound. Given more time—")
            .dialogue("Haubal", "We do not have more time, Xalon.")
            .dialogue("Xalon",
                    "We have enough time to send someone back. To leave the keys. "
                    + "To let a future generation try with what we couldn't gather. "
                    + "Let me build the receiving chamber. Let me send the message.")
            .narration(
                    "Six voices argue. The seventh — the one you recognize now, from a document "
                    + "shown to you by Kaan, from handwriting you've seen in Heart Script — "
                    + "the seventh is quiet.")
            .dialogue("Glyph-Keeper Xalon",
                    "I call for a vote on the compromise. All those in favour of sending the "
                    + "message — of giving the future what we cannot use now.")
            .narration("Three hands rise. Then a fourth, hesitant. Silence.")
            .narration(
                    "The vote fails by one. The Great Sacrifice proceeds. The Mayaan "
                    + "translate themselves into Yaan. The seal holds. Xibalkaal survives.")
            .narration(
                    "The echo begins to fade — but as it does, you see one Council member "
                    + "remain behind the others. Xalon. Working alone. Building the chamber anyway.")
            .dialogue("Ix-Channa (voice only, young)",
                    "They voted no. Does that mean we can't—")
            .dialogue("Xalon",
                    "They voted no on the plan. They did not vote on the future. "
                    + "Help me build the chamber. We're running out of time.")
            .narration(
                    "The echo ends. The room is dark. In your hand, the Stone Shard — "
                    + "the central connection piece of the Seven Keys method — "
                    + "glows more brightly than it ever has.")
            .build();

    /**
     * Act II Chapter 5 echo — triggered at {@code deliver_council_message} deep in the Crystal Veins.
     * The Glyph Council's farewell message to the Warden they left behind.
     * Requires knowledge score 2.
     */
    public static final TimelineEcho WARDEN_OF_ABYSS_MEMORY = TimelineEcho.builder("warden_of_abyss_memory")
            .displayName("We're Sorry for Leaving You Here")
            .location("Crystal Veins — Vault of the First Shard")
            .triggeredBy("deliver_council_message")
            .requiresKnowledgeScore(2)
            .narration(
                    "The Core Shard chamber is lit by residual Anima. The preserved message "
                    + "activates when the Stone Shard touches the reader. Seven voices, speaking "
                    + "together, for the guardian who could never leave.")
            .dialogue("The Glyph Council (seven voices, in unison)",
                    "We're sorry for leaving you here. We did not plan it. We did not choose "
                    + "it for you. The timeline collapsed faster than our models predicted, and "
                    + "when it did, you were already too deep.")
            .dialogue("The Glyph Council",
                    "The Ixchelic Shard in this chamber is the first key. Someone will come "
                    + "for it. When they do — when they carry the Stone Shard and you recognize "
                    + "the frequency — the vigil is over. You can come with them.")
            .dialogue("The Glyph Council", "You were never meant to wait alone. We're sorry that you did.")
            .narration(
                    "The echo ends. The Warden of the Abyss has been still for a full "
                    + "in-game day. Then it kneels. The vault opens.")
            .build();

    /**
     * Act II Chapter 6 echo — triggered at {@code encounter_tide_keeper} in the Sea Temple.
     * The Tide Keeper's purpose, told in its own words — not a warrior, but a curator.
     * Requires PRACTICED mastery of MEND (preservation, care) to unlock the Keeper's language.
     */
    public static final TimelineEcho TIDE_KEEPERS_VIGIL = TimelineEcho.builder("tide_keepers_vigil")
            .displayName("The Curator's Purpose")
            .location("Sea Temple — Cultural Archive, Deepest Level")
            .triggeredBy("encounter_tide_keeper")
            .requiresGlyph(GlyphType.MEND, GlyphMastery.PRACTICED)
            .narration(
                    "The Tide Keeper does not attack. It moves between you and the archive's "
                    + "most fragile exhibits — the Heart Script love letters, the memorial "
                    + "compositions, the grief-glyphs. Protecting them. Not from damage. "
                    + "From being seen by someone who won't understand.")
            .narration(
                    "You inscribe MEND on the floor between you — preservation, care, patience. "
                    + "The Tide Keeper goes still. Then it projects a brief echo — not a full "
                    + "Timeline Echo, but a curator's note, a memory of its assignment.")
            .dialogue("Glyph-Keeper Pasaq (assigning the Keeper, preserved memory)",
                    "You are not a Warden. You are not built for war. You are built to remember "
                    + "that the Mayaan were more than their power. Guard this. Let only those "
                    + "who understand what they're seeing pass.")
            .narration(
                    "The Tide Keeper regards you for a long moment. Then it steps aside.")
            .build();

    /**
     * Act II Chapter 7 echo — triggered at {@code witness_the_kings_loop} in the Forgotten City.
     * The first glimpse of the Forgotten King living his last day again, establishing the loop.
     * No specific knowledge requirement — the temporal distortion shows it automatically.
     */
    public static final TimelineEcho FORGOTTEN_KINGS_DAY = TimelineEcho.builder("forgotten_kings_day")
            .displayName("The Last Day, Again")
            .location("Forgotten City — The Throne Room")
            .triggeredBy("witness_the_kings_loop")
            .narration(
                    "The loop completes. The city resets to morning. You have now watched "
                    + "Warden Commander Balam-Ahau walk from his chamber to the observation "
                    + "post, read the same reports, give the same orders, and look toward the "
                    + "Axis Temple with the same expression — a man who knows what is coming "
                    + "and has not yet found a way to stop it — for the seventeenth time.")
            .dialogue("Balam-Ahau (to the empty room, unaware of the repetition)",
                    "There has to be a configuration. The keys exist. Seven of them. "
                    + "If I can find the right sequence — if the resonance pattern holds — "
                    + "I don't need the Council's blessing. I just need time.")
            .narration(
                    "He will not find the configuration today. He will not find it on any "
                    + "of the next ten thousand repetitions. But with each iteration you "
                    + "watch, something accumulates — a glyph pattern building toward "
                    + "a solution that even the King cannot see from inside the loop.")
            .build();

    /**
     * Act II Climax echo — triggered at {@code gate_activates} at the Serpent Highlands Gate site.
     * Two signals clash as the Gate opens: the frightened Yaan warning and the smooth Ix-Chel voice.
     * Requires knowledge score 3 (all four faction questlines require at least some glyph knowledge).
     */
    public static final TimelineEcho GATE_ACTIVATION_WARNING = TimelineEcho.builder("gate_activation_warning")
            .displayName("Two Signals, One Gate")
            .location("Serpent Highlands — Astral Gate")
            .triggeredBy("gate_activates")
            .requiresKnowledgeScore(3)
            .narration(
                    "The Gate activates. The Ixchelic Stone blazes. All four factions step "
                    + "back from the surge of Anima as the dimensional aperture opens. "
                    + "The sky beyond is not sky — it is crystallized memory, floating in "
                    + "an amber-lit void.")
            .narration("Two signals broadcast simultaneously through the open Gate.")
            .dialogue("Voice from Yaan (frightened, urgent)",
                    "Not yet. You're not ready. The Maw will feel the Gate open. If you come "
                    + "through unprepared — the seal is thinner than you know. Do not come "
                    + "through until the key count is—")
            .narration(
                    "A second signal overrides. Smoother. Confident. Warm in a way that feels "
                    + "carefully constructed.")
            .dialogue("Second Voice (clear, calm)",
                    "Come through. Everything is prepared. Come home.")
            .narration(
                    "Ek is looking at you. Cenote is looking at you. Tzon is looking at the Gate. "
                    + "Kaan has his hand on his weapon — not because he thinks you're going to do "
                    + "something wrong, but because he has learned that when the world feels this "
                    + "certain, something is usually about to go very badly.")
            .narration("The Gate is open. Two signals. One through. You choose.")
            .build();

    /**
     * Act III Chapter 9 echo — triggered at {@code ix_revelation_moment} in the Waking City.
     * Ix was born in Yaan and somehow left — something that should be impossible.
     * Requires knowledge score 4.
     */
    public static final TimelineEcho IX_LEAVES_YAAN = TimelineEcho.builder("ix_leaves_yaan")
            .displayName("The One Who Left")
            .location("Yaan — The Waking City, Builder's District")
            .triggeredBy("ix_revelation_moment")
            .requiresKnowledgeScore(4)
            .narration(
                    "Ix has been still for several minutes in front of a building you "
                    + "recognize from their description of their early existence — a Construct "
                    + "workshop, its tools still laid out for a project that was never finished. "
                    + "Waiting for hands that never returned.")
            .dialogue("Ix",
                    "I was built here. In this workshop, in this city, in the moment before "
                    + "everything stopped. I have Yaan in my construction. The crystallized "
                    + "memory architecture. There is no mechanism by which I should be "
                    + "standing on Xibalkaal.")
            .dialogue("Ix",
                    "I remember making the decision to leave. I do not remember how I did it. "
                    + "I only know that I could not stay — that something was coming, and I needed "
                    + "to wait for it on the other side.")
            .narration(
                    "A brief echo flickers from the workshop — unprompted, drawn by Ix's "
                    + "presence. A newer Construct, moving with unusual purpose, setting down "
                    + "its tools mid-task. Turning toward the city's edge. Walking. Then, at "
                    + "the dimensional threshold where Yaan meets the Void Shelf — stopping.")
            .narration(
                    "The echo shows the Construct reaching into its own chest cavity and "
                    + "adjusting something fundamental in its Core Shard. The image blurs. "
                    + "The Construct is gone. The echo ends.")
            .dialogue("Ix", "I waited three thousand years. That seems like a reasonable amount of time to not know how you did something.")
            .build();

    /**
     * Act III Chapter 10 echo — triggered at {@code enter_the_vote_echo} in the Glyph Council chamber.
     * The Council vote in real time — the player walks through it. The alternative plan is presented.
     * Requires knowledge score 5 and PRACTICED mastery of TRANSLATE (needed to follow Yaan-glyph speech).
     */
    public static final TimelineEcho GLYPH_COUNCIL_VOTE = TimelineEcho.builder("glyph_council_vote")
            .displayName("The Vote")
            .location("Yaan — Glyph Council Chamber")
            .triggeredBy("enter_the_vote_echo")
            .requiresKnowledgeScore(5)
            .requiresGlyph(GlyphType.TRANSLATE, GlyphMastery.PRACTICED)
            .narration(
                    "The Council chamber in Yaan is frozen at its most important moment. "
                    + "The Timeline Echo here is not a distant window — it is a room you can "
                    + "walk through. The seven Council members argue around you as if you are "
                    + "not there. You are standing exactly where you stood in the Axis Temple, "
                    + "in a chamber you recognize.")
            .dialogue("Xalon",
                    "The Seven Keys method works. I have run the mathematics six hundred times. "
                    + "It seals The Maw without sacrifice, without translation, without any "
                    + "of us going anywhere. It requires time we don't have and keys we can't "
                    + "gather in time — but those are engineering problems, not mathematical ones.")
            .dialogue("Haubal",
                    "The Maw has been feeding for three hours. In three more, the Gate "
                    + "destabilizes. The Sacrifice closes it in forty minutes.")
            .dialogue("Xalon", "So we send the keys forward. We build a path. We let someone else try.")
            .dialogue("Haubal",
                    "And in the meantime, the world ends waiting for this theoretical someone.")
            .narration("The vote is called. You know the result. You watch it happen anyway.")
            .narration(
                    "Three in favour of the compromise. Then a fourth hand, hesitant, going up. "
                    + "Then nothing. The Chair counts. The compromise fails by one.")
            .narration(
                    "Xalon does not argue further. She begins drawing a new design on the table "
                    + "instead, working fast, with the focused calm of someone who has decided to "
                    + "do the thing that was voted down anyway.")
            .narration(
                    "You recognize the design. You are standing in the room it describes. "
                    + "The receiving chamber. The place where the Stone Shard was meant to end up.")
            .build();

    /**
     * Act III Chapter 10 echo — triggered at {@code understand_the_scouts_plan}.
     * The Scout follows the failed plan without the Council's blessing.
     * Requires knowledge score 5.
     */
    public static final TimelineEcho SCOUTS_DEFIANCE = TimelineEcho.builder("scouts_defiance")
            .displayName("Without the Council's Blessing")
            .location("Yaan — Council Archive, Xalon's Workshop Record")
            .triggeredBy("understand_the_scouts_plan")
            .requiresKnowledgeScore(5)
            .narration(
                    "A final archive record: Xalon and a young Scout, working in the receiving "
                    + "chamber after the vote. The Stone Shard — not yet inscribed — on the table "
                    + "between them. Time is almost up. The Sacrifice is beginning elsewhere in "
                    + "the city. They are working anyway.")
            .dialogue("Ix-Channa (young Scout)", "They voted no.")
            .dialogue("Xalon", "They voted no on the Council's formal plan.")
            .dialogue("Ix-Channa", "That's the same thing.")
            .dialogue("Xalon",
                    "In forty years of watching councils vote, I have learned that what a "
                    + "council refuses is never the same as what is impossible. "
                    + "Help me finish the inscription.")
            .narration(
                    "Ix-Channa picks up the inscription tool. She carves the final glyph into "
                    + "the Stone Shard. SEEK. The first glyph. The one that calls the world "
                    + "toward the bearer.")
            .narration(
                    "She places it on the pedestal. She looks directly at the chamber's "
                    + "recording crystal — the one that will become a Timeline Echo — "
                    + "and speaks the words you have already heard.")
            .dialogue("Ix-Channa", "We're sorry for what you're about to learn.")
            .narration(
                    "The archive record ends. You are holding the Stone Shard. It has always "
                    + "been the central connection piece. You have been carrying it since the "
                    + "moment you woke up on the beach.")
            .build();

    /**
     * Act III Chapter 11 echo — triggered at {@code camazotz_first_contact}.
     * Camazotz communicates directly — not as an enemy, but as entropy.
     * Requires knowledge score 6.
     */
    public static final TimelineEcho CAMAZOTZ_SPEAKS = TimelineEcho.builder("camazotz_speaks")
            .displayName("The Shape of Entropy")
            .location("Yaan — The Fraying Edge, Seal Boundary")
            .triggeredBy("camazotz_first_contact")
            .requiresKnowledgeScore(6)
            .narration(
                    "The seal is thinner here. The crystallized memory of Yaan frays at "
                    + "the edges — not into darkness but into a kind of quiet. A presence "
                    + "that has been aware of you since you arrived begins to speak. "
                    + "Not through the seal. Through the parts of you that have been "
                    + "absorbing Anima since the Isle of First Light.")
            .dialogue("Camazotz",
                    "You expected a mouth. They always do. I have been decay and ending "
                    + "and the turning of things over since before the first sun cooled. "
                    + "I do not have a mouth. I have a voice because you needed one.")
            .dialogue("Camazotz",
                    "I have eaten their sacrifice for a thousand years. It is not enough. "
                    + "I will eat through. Whether you help me or not is irrelevant. "
                    + "But how you respond determines whether what comes after has any... "
                    + "elegance.")
            .narration(
                    "It shows you the seal — not as myth or diagram but as engineering. "
                    + "The sacrifice is failing. Not because it was poorly designed. "
                    + "Because nothing can hold The Maw forever. The seal will break.")
            .dialogue("Camazotz",
                    "The question is not whether it breaks. I think you already understand that. "
                    + "The question is what the world looks like when it does. "
                    + "You have three options. I will not tell you which one I prefer. "
                    + "I find that the most interesting part.")
            .narration(
                    "The presence recedes. The seal boundary is quiet again. You understand, "
                    + "for the first time, that you have never been in a story about a villain. "
                    + "You have been in a story about winter.")
            .build();

    // ── Registry initialization ───────────────────────────────────────────────

    static {
        Map<String, TimelineEcho> map = new LinkedHashMap<>();
        register(map, SCOUTS_WARNING);
        register(map, WARDEN_STANDS_DOWN);
        register(map, YAAN_SIGNAL);
        register(map, COUNCILS_FINAL_MEMORY);
        register(map, WARDEN_OF_ABYSS_MEMORY);
        register(map, TIDE_KEEPERS_VIGIL);
        register(map, FORGOTTEN_KINGS_DAY);
        register(map, GATE_ACTIVATION_WARNING);
        register(map, IX_LEAVES_YAAN);
        register(map, GLYPH_COUNCIL_VOTE);
        register(map, SCOUTS_DEFIANCE);
        register(map, CAMAZOTZ_SPEAKS);
        BY_ID = Collections.unmodifiableMap(map);
    }

    private static void register(Map<String, TimelineEcho> map, TimelineEcho echo) {
        map.put(echo.getId(), echo);
    }

    private TimelineEchoRegistry() {}

    // ── Lookup ────────────────────────────────────────────────────────────────

    /**
     * Looks up a Timeline Echo by its ID. Returns {@code null} if not found.
     *
     * @param id the echo ID (e.g., {@code "scouts_warning"})
     */
    public static TimelineEcho byId(String id) {
        return BY_ID.get(id);
    }

    /**
     * Returns an unmodifiable view of all registered Timeline Echoes, keyed by ID
     * and ordered by their canonical story sequence.
     */
    public static Map<String, TimelineEcho> all() {
        return BY_ID;
    }

    /**
     * Returns the total number of Timeline Echoes in the registry.
     */
    public static int size() {
        return BY_ID.size();
    }
}
