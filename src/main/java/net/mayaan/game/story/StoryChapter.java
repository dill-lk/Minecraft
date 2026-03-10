package net.mayaan.game.story;

import com.mojang.serialization.Codec;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * All chapters of the Mayaan story, ordered by their position in the narrative.
 *
 * <p>Each chapter belongs to a {@link StoryAct} and contains an ordered list of
 * {@link StoryGoal goals}. Required goals must all be completed before the story
 * advances to the next chapter. Optional goals provide extra lore and rewards.
 *
 * <h2>Chapter Order</h2>
 * <pre>
 *   Prologue:  DREAMERS_SHORE
 *   Act I:     FIRST_ROOTS → FORGEBORN_GAMBIT → VOICES_IN_STATIC → IRON_PACT_BURIES
 *   Act II:    FIRST_SHARD → SEA_TEMPLES_DEPTH → FORGOTTEN_KING → GATE_REBUILT
 *   Act III:   WAKING_CITY → TRUTH_IN_AMBER → MAW_STIRS → FINAL_CHOICE
 *   Epilogue:  THREE_WORLDS
 * </pre>
 *
 * <p>Within Acts I and II the four questlines can be completed in any order, but
 * all must be finished before the act-transition chapter (IRON_PACT_BURIES and
 * GATE_REBUILT respectively) can conclude.
 */
public enum StoryChapter {

    // ── Prologue ──────────────────────────────────────────────────────────────

    /**
     * The Dreamer's Shore — the tutorial chapter. Linear and gentle by design.
     * Player wakes on the Isle of First Light, finds the Stone Shard, meets Ix,
     * and receives the Scout's ancient message before sailing to the continent.
     */
    DREAMERS_SHORE(StoryAct.PROLOGUE, "dreamers_shore", "The Dreamer's Shore", 0),

    // ── Act I: The Shattered Lands ────────────────────────────────────────────

    /**
     * First Roots — arriving on Xibalkaal; meeting Elder Cenote and the Rootweavers;
     * resolving the Canopy Wall to open the path into the continent's interior.
     */
    FIRST_ROOTS(StoryAct.ACT_I, "first_roots", "First Roots", 1),

    /**
     * The Forgeborn's Gambit — Tzon's city of Tz'ikin; the buried Resonance Pillar;
     * discovering the Stone Shard is a Warden's Key; Ix's reaction to the vault.
     */
    FORGEBORN_GAMBIT(StoryAct.ACT_I, "forgeborn_gambit", "The Forgeborn's Gambit", 2),

    /**
     * Voices in the Static — the Star Callers' mountain observatory; amplifying the
     * terrified signal from Yaan; two conflicting signals and the faction schism.
     */
    VOICES_IN_STATIC(StoryAct.ACT_I, "voices_in_static", "Voices in the Static", 3),

    /**
     * What the Iron Pact Buries — Xaan Hold; the Maw Breach battle; Kaan's
     * Glyph-Keeper secret; the Codex Fragment that rewrites Mayaan history.
     * Climax: the Axis Temple and the Council's Final Memory.
     */
    IRON_PACT_BURIES(StoryAct.ACT_I, "iron_pact_buries", "What the Iron Pact Buries", 4),

    // ── Act II: The Gate of Stars ─────────────────────────────────────────────

    /**
     * The First Shard — the Crystal Veins mining network; the Anima-pressure zone;
     * the lonely Warden of the Abyss; retrieving the first piece of the Ixchelic Stone.
     */
    FIRST_SHARD(StoryAct.ACT_II, "first_shard", "The First Shard", 5),

    /**
     * The Sea Temple's Depth — the submerged Abyssal Coast temple; the cultural archive;
     * the Tide Keeper who guards beauty instead of power; the second Ixchelic Shard.
     */
    SEA_TEMPLES_DEPTH(StoryAct.ACT_II, "sea_temples_depth", "The Sea Temple's Depth", 6),

    /**
     * The Forgotten King — the temporal-distortion city in the Serpent Highlands;
     * the hybrid king trapped in an endless loop; breaking the loop; the third shard.
     */
    FORGOTTEN_KING(StoryAct.ACT_II, "forgotten_king", "The Forgotten King", 7),

    /**
     * The Gate Rebuilt — all four factions unite for the first time; the Astral Gate
     * reconstructed; two competing signals; the choice to step through.
     * Climax of Act II.
     */
    GATE_REBUILT(StoryAct.ACT_II, "gate_rebuilt", "The Gate Rebuilt", 8),

    // ── Act III: Beyond the Veil ──────────────────────────────────────────────

    /**
     * The Waking City — arriving in Yaan to find a living civilization frozen in
     * their last day; Ix's revelation about leaving Yaan against all possibility.
     */
    WAKING_CITY(StoryAct.ACT_III, "waking_city", "The Waking City", 9),

    /**
     * The Truth in Amber — the Glyph Council chamber; witnessing the vote;
     * learning the Seven Keys method; recognizing the receiving chamber from the Prologue.
     */
    TRUTH_IN_AMBER(StoryAct.ACT_III, "truth_in_amber", "The Truth in Amber", 10),

    /**
     * The Maw Stirs — Camazotz makes contact; the truth about entropy; the seal's
     * true state revealed. The question is not whether it breaks — but what comes after.
     */
    MAW_STIRS(StoryAct.ACT_III, "maw_stirs", "The Maw Stirs", 11),

    /**
     * The Final Choice — RESTORE (bring the Mayaan home), ASCEND (become the new
     * Architect), or SEAL (take the Long Watch). Three endings, one cycle.
     */
    FINAL_CHOICE(StoryAct.ACT_III, "final_choice", "The Final Choice", 12),

    // ── Epilogue ──────────────────────────────────────────────────────────────

    /**
     * Three Worlds — one year later, one generation later, one century later.
     * The world changes in response to the choice. The final image is the same
     * in all three: somewhere, a child finds a glowing stone shard. The cycle continues.
     */
    THREE_WORLDS(StoryAct.EPILOGUE, "three_worlds", "Three Worlds", 13);

    private final StoryAct act;
    private final String id;
    private final String displayName;
    private final int order;
    private final List<StoryGoal> goals = new ArrayList<>();

    StoryChapter(StoryAct act, String id, String displayName, int order) {
        this.act = act;
        this.id = id;
        this.displayName = displayName;
        this.order = order;
        initGoals();
    }

    /** Populates the goal list for this chapter. Called once from the constructor. */
    @SuppressWarnings("DuplicateBranchesInSwitch")
    private void initGoals() {
        switch (this) {

            case DREAMERS_SHORE -> {
                // The tutorial sequence — gentle, linear, story-establishing
                req("wake_on_beach");             // The spawn event: waking with no memories
                req("pick_up_stone_shard");       // Find the Stone Shard at the monolith circle
                req("explore_island");            // Learn basic survival on the gentle island
                req("first_night_monoliths");     // Witness the monoliths light up on night one
                req("follow_shards_pull");        // Follow the Stone Shard's directional pulse inland
                req("find_ix");                   // Discover the vine-covered Construct in the jungle
                req("enter_buried_temple");       // Enter the unlocked temple at the island's heart
                req("solve_construct_guardian");  // Demonstrate Stone Shard use to the guardian
                req("read_first_message");        // Trigger the Scout's Timeline Echo; hear the warning
                req("sail_to_continent");         // Depart with Ix aboard the ley-line vessel
            }

            case FIRST_ROOTS -> {
                // Rootweaver faction questline — negotiation and nature
                req("arrive_abyssal_coast");      // Land on Xibalkaal's shore
                req("approach_rootweaver_village");
                req("meet_elder_cenote");         // Cenote recognizes the Wanderer's Shard
                req("learn_of_canopy_wall");      // Understand the Tender Construct's protocol
                req("investigate_canopy_wall");   // Explore the dense barrier
                req("resolve_canopy_wall");       // Negotiate with or deactivate the Tender Construct
                req("receive_glyph_teaching");    // Cenote's first structured Glyph lesson
                opt("jungle_lord_encounter");     // Optional: communicate with the apex tree-creature
            }

            case FORGEBORN_GAMBIT -> {
                // Forgeborn faction questline — engineering and discovery
                req("reach_tzikin");              // Travel to the Construct-city of Tz'ikin
                req("meet_tzon");                 // Tzon reads the Stone Shard with his instruments
                req("agree_to_experiment");       // Accept Tzon's deal for tunnel access
                req("activate_resonance_pillar"); // Use the Stone Shard to bridge the Glyph sequence
                req("warden_constructs_appear");  // Three dormant Wardens activate and go to threat mode
                req("resolve_warden_crisis");     // Show the Wardens the Stone Shard (or fight/reprogram)
                req("learn_wardens_key");         // Discover the Stone Shard is a Warden's Key
                req("access_forgeborn_tunnels");  // Collect on Tzon's promise
                opt("witness_ix_reaction");       // Optional: observe Ix standing still near the vault
            }

            case VOICES_IN_STATIC -> {
                // Star Callers faction questline — signals and conflicting truths
                req("reach_serpent_highlands");   // Ascend toward the observatory complex
                req("meet_ek");                   // Ek speaks without turning — she was waiting
                req("learn_of_yaan_signal");      // The decades-long signal from the afterworld
                req("amplify_signal");            // Use the Stone Shard as amplifier
                req("hear_frightened_message");   // The signal resolves — a terrified voice warns
                req("learn_of_conflicting_signals"); // Ek reveals the second signal's existence
                opt("faction_alignment_choice");  // Optional: choose which signal to trust publicly
            }

            case IRON_PACT_BURIES -> {
                // Iron Pact faction questline — battle and buried history; Act I climax
                req("reach_xaan_hold");           // Arrive at the fortress city
                req("meet_kaan");                 // The Iron Pact wants you gone
                req("maw_breach_battle");         // A large breach opens — fight alongside the Pact
                req("notice_kaan_glyph");         // Observe Kaan use a glyph sequence he shouldn't know
                req("kaan_reveals_secret");       // Kaan's Glyph-Keeper heritage and the Codex Fragment
                req("read_codex_fragment");       // "We had another option. We never tried it."
                req("path_to_axis_temple_open");  // All four factions clear the way
                req("axis_temple_dungeon");       // Navigate the multi-tier Axis Temple
                req("councils_final_memory");     // The Timeline Echo: the full truth of The Unraveling
            }

            case FIRST_SHARD -> {
                // Act II — the Crystal Veins dungeon
                req("find_crystal_veins_entrance");
                req("descend_mining_network");
                req("enter_anima_pressure_zone"); // Hallucinations and enhanced effects begin
                req("locate_warden_of_abyss");    // The 60-metre guardian, lonely and misinterpreting
                req("show_shard_to_warden");      // The Stone Shard calms it briefly
                req("find_core_shard_chamber");   // Locate the Council's final message
                req("deliver_council_message");   // "You can come with them." — the Warden kneels
                req("retrieve_ixchelic_shard_one");
            }

            case SEA_TEMPLES_DEPTH -> {
                // Act II — the submerged Abyssal Coast temple
                req("find_sea_temple_access");    // Only accessible during specific lunar phases
                req("descend_through_layers");    // Going deeper = going further back in history
                req("encounter_tidal_constructs"); // Graceful, fast underwater guardians
                req("encounter_tide_keeper");     // The curator Construct protecting art, not weapons
                req("demonstrate_understanding"); // Use glyph sequences matching the archive's subjects
                req("retrieve_ixchelic_shard_two");
                opt("unlock_cultural_archive");   // Optional: full lore content + Heart Script translation
            }

            case FORGOTTEN_KING -> {
                // Act II — temporal distortion in the Serpent Highlands
                req("find_distorted_city");       // The buried city in the pocket of temporal distortion
                req("enter_temporal_distortion");
                req("witness_the_kings_loop");    // The Warden commander reliving his last day
                req("learn_kings_history");       // He voted no; stayed; tried until the last second
                req("understand_the_loop");       // Discover what moment he is trapped in
                req("accumulate_glyph_sequences"); // Build up correct sequences through repetitions
                req("solve_glyph_loop_puzzle");   // The configuration that opens the genuine exit
                req("break_the_loop");            // The King finally moves forward in time
                req("receive_ixchelic_shard_three"); // "I know what you need. Take it."
            }

            case GATE_REBUILT -> {
                // Act II climax — all factions unite; gate activated
                req("return_to_gate_site");       // Serpent Highlands; the ancient structure still stands
                req("gather_factions");           // For the first time, all four work together
                req("rootweavers_leyline_reroute");
                req("forgeborn_repair_mechanism");
                req("star_callers_activation_sequence");
                req("iron_pact_perimeter_defense");
                req("insert_ixchelic_stone");     // Place all three shards into the housing
                req("gate_activates");            // Two signals clash as the Gate opens
                req("choose_to_enter_yaan");      // Step through — the choice that begins Act III
            }

            case WAKING_CITY -> {
                // Act III — arriving in Yaan
                req("arrive_in_yaan");            // The surreal aesthetic shift: dream-logic architecture
                req("explore_mayaan_city");       // A civilization at the height of its glory; frozen
                req("ix_revelation_moment");      // Ix was born here — and somehow left, impossibly
                opt("speak_with_frozen_citizens"); // Optional: hundreds of conversations available
                opt("explore_mayaan_institutions"); // Optional: every building has stories
            }

            case TRUTH_IN_AMBER -> {
                // Act III — the Council chamber and the full truth
                req("find_glyph_council_chamber");
                req("enter_the_vote_echo");       // Walk through the Timeline Echo in real time
                req("witness_glyph_keeper_argument"); // The alternative: Seven Keys, no sacrifice needed
                req("witness_the_vote");          // The compromise voted down by one
                req("recognize_the_receiving_chamber"); // This is the room from the Prologue
                req("understand_the_scouts_plan"); // She followed the failed plan anyway, without blessing
                req("understand_the_stone_shard"); // It was always the central connection of Seven Keys
            }

            case MAW_STIRS -> {
                // Act III — Camazotz communicates; the truth about entropy
                req("camazotz_first_contact");    // He has been aware since you arrived
                req("listen_to_camazotz");        // Not evil — a cosmic force, like winter
                req("see_the_seals_true_state");  // The sacrifice is failing; it was always going to
                req("understand_the_real_question"); // Not whether it breaks — what comes after
                req("three_paths_revealed");      // Restore, Ascend, Seal — all options now visible
            }

            case FINAL_CHOICE -> {
                // Act III climax — the ending
                req("make_final_choice");         // Required: choose one of the three paths
                opt("path_restore");              // RESTORE: Homecoming — free the Mayaan
                opt("path_ascend");               // ASCEND: New Architect — absorb and negotiate
                opt("path_seal");                 // SEAL: Long Watch — the cycle made conscious
            }

            case THREE_WORLDS -> {
                // Epilogue — the aftermath
                req("epilogue_one_year");         // How the world changed immediately
                req("epilogue_one_generation");   // The first generation born after the choice
                req("epilogue_one_century");      // The lasting legacy
                req("find_final_stone_fragment"); // Somewhere, a child finds a glowing shard — seek
            }
        }
    }

    /** Adds a required goal to this chapter. */
    private void req(String goalId) {
        goals.add(new StoryGoal(goalId, this, true));
    }

    /** Adds an optional goal to this chapter. */
    private void opt(String goalId) {
        goals.add(new StoryGoal(goalId, this, false));
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    /** Returns the act this chapter belongs to. */
    public StoryAct getAct() {
        return act;
    }

    /** Returns the unique identifier of this chapter (e.g., {@code "dreamers_shore"}). */
    public String getId() {
        return id;
    }

    /** Returns the human-readable display name of this chapter. */
    public String getDisplayName() {
        return displayName;
    }

    /** Returns the canonical ordering index of this chapter (0 = first, 13 = last). */
    public int getOrder() {
        return order;
    }

    /** Returns an unmodifiable view of all goals in this chapter, in story order. */
    public List<StoryGoal> getGoals() {
        return Collections.unmodifiableList(goals);
    }

    /**
     * Returns only the required goals in this chapter — those that must all be completed
     * before the story advances to the next chapter.
     */
    public List<StoryGoal> getRequiredGoals() {
        return goals.stream().filter(StoryGoal::isRequired).toList();
    }

    /**
     * Returns the chapter that follows this one in canonical story order,
     * or {@code null} if this is the final chapter ({@link #THREE_WORLDS}).
     */
    public StoryChapter next() {
        StoryChapter[] values = StoryChapter.values();
        int nextOrdinal = this.ordinal() + 1;
        return nextOrdinal < values.length ? values[nextOrdinal] : null;
    }

    /**
     * Finds a goal in this chapter by its ID string.
     *
     * @param goalId the goal's identifier (e.g., {@code "wake_on_beach"})
     * @return the matching goal, or {@code null} if not found
     */
    public StoryGoal findGoal(String goalId) {
        for (StoryGoal goal : goals) {
            if (goal.getId().equals(goalId)) {
                return goal;
            }
        }
        return null;
    }

    /**
     * Codec that serializes/deserializes a {@link StoryChapter} by its string ID
     * (e.g., {@code "dreamers_shore"}).
     */
    public static final Codec<StoryChapter> CODEC =
            Codec.STRING.xmap(StoryChapter::byId, StoryChapter::getId);

    /**
     * Finds the chapter with the given ID, or {@code null} if not found.
     *
     * @param id the chapter identifier (e.g., {@code "dreamers_shore"})
     */
    public static StoryChapter byId(String id) {
        for (StoryChapter chapter : values()) {
            if (chapter.getId().equals(id)) {
                return chapter;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "mayaan:chapter/" + id;
    }
}
