package net.mayaan.game.faction;

/**
 * The four surviving factions of Xibalkaal — the remnants of the Mayaan civilization
 * who refused the Great Sacrifice and now each carry a fragment of the lost world's truth.
 *
 * <p>In story terms, the player must engage with all four factions during
 * {@link net.mayaan.game.story.StoryChapter#FIRST_ROOTS Act I} to open the path to the
 * Axis Temple. Each faction controls a different region and obstacle, and each has its
 * own quest chain running parallel to the main story.
 *
 * <h2>Standing Gate</h2>
 * Certain story chapters require a minimum {@link FactionStanding} with a faction before
 * their quest chain can reach its conclusion:
 * <ul>
 *   <li>{@link #ROOTWEAVERS} — {@link FactionStanding#ACCEPTED} to negotiate the Canopy Wall</li>
 *   <li>{@link #FORGEBORN}   — {@link FactionStanding#ACCEPTED} to access the forge tunnels</li>
 *   <li>{@link #STAR_CALLERS} — {@link FactionStanding#ACCEPTED} to use the observatory amplifier</li>
 *   <li>{@link #IRON_PACT}   — {@link FactionStanding#TRUSTED}  to fight alongside during the Maw Breach</li>
 * </ul>
 *
 * <h2>Lore</h2>
 * <ul>
 *   <li>Each faction descends from one of the five Mayaan castes of knowledge</li>
 *   <li>Each carries a different piece of the truth about The Unraveling</li>
 *   <li>Each was founded by someone who voted <em>no</em> on the Great Sacrifice</li>
 *   <li>Each has a secret that recontextualizes their entire history when revealed</li>
 * </ul>
 *
 * @see FactionStanding
 * @see PlayerFactionData
 * @see FactionManager
 */
public enum Faction {

    /**
     * The Rootweavers — pacifist forest-dwellers descended from the Mayaan Root-Speakers.
     *
     * <p>They believe the world is alive: the ley-lines are its nervous system and the
     * jungle is its body. Their magic is organic — growing, binding, calling.
     * Their pacifism is as much atonement as philosophy: the oldest Rootweavers know
     * the truth about The Unraveling and have carried that guilt for generations.
     *
     * <p><b>Leader</b>: Elder Cenote — partially merging with the landscape from
     * decades of ley-line Anima absorption.
     * <p><b>Home</b>: Abyssal Coast / Eternal Canopy
     * <p><b>Caste Origin</b>: Root-Speakers
     * <p><b>Side Quest</b>: "The Last Garden"
     */
    ROOTWEAVERS(
            "rootweavers",
            "The Rootweavers",
            "Cenote",
            "root_speakers",
            "The world is alive. We are its caretakers.",
            FactionStanding.ACCEPTED,
            "the_last_garden"
    ),

    /**
     * The Forgeborn — master engineers descended from the Mayaan Shapers.
     *
     * <p>They believe the Mayaan's mistake was the sacrifice itself, not their ambition.
     * Their goal is to rebuild — restart the Mayaan technological tradition, reactivate
     * the Construct network, and undo The Unraveling through engineering.
     * They sit on a secret: the location of the primary Core Shard vault.
     *
     * <p><b>Leader</b>: Tzon — forty-something engineer who secretly questions whether
     * what the Forgeborn are building is what the Mayaan themselves would have wanted.
     * <p><b>Home</b>: Tz'ikin (the Construct-city) / Ember Wastes
     * <p><b>Caste Origin</b>: Shapers
     * <p><b>Side Quest</b>: "The City That Breathes"
     */
    FORGEBORN(
            "forgeborn",
            "The Forgeborn",
            "Tzon",
            "shapers",
            "We will rebuild what was lost. Stone by stone, shard by shard.",
            FactionStanding.ACCEPTED,
            "the_city_that_breathes"
    ),

    /**
     * The Star Callers — mystic astronomers descended from the Mayaan Scouts.
     *
     * <p>They believe the Architects are active cosmic forces that can be contacted
     * through the correct astronomical alignment and glyph sequences. They are the most
     * skilled Glyph users among the surviving factions and can enter trance states
     * called Star Walking to observe distant events. Three senior members have already
     * made contact with a presence in Yaan — one they believe to be Ix-Chel herself.
     *
     * <p><b>Leader</b>: Ek — a blind astronomer who has never needed eyes to read the stars.
     * <p><b>Home</b>: Serpent Highlands observatory complex
     * <p><b>Caste Origin</b>: Scouts
     * <p><b>Side Quest</b>: "The Astronomer's Grave"
     */
    STAR_CALLERS(
            "star_callers",
            "The Star Callers",
            "Ek",
            "scouts",
            "The stars speak. We have learned, finally, to listen.",
            FactionStanding.ACCEPTED,
            "the_astronomers_grave"
    ),

    /**
     * The Iron Pact — disciplined warriors descended from the Mayaan Wardens.
     *
     * <p>They believe the philosophical debate about the past is a distraction from
     * immediate survival. Corrupted Constructs, dimensional breaches, and the spreading
     * Grey are present threats that require present solutions. Their true founder was
     * not a Warden but the most senior Glyph-Keeper who voted no on the Great Sacrifice
     * — a secret hidden for centuries inside falsified founding mythology.
     *
     * <p><b>Leader</b>: Kaan — the Pact's greatest warrior, beginning to feel the weight
     * of the secrets his faction's history is hiding.
     * <p><b>Home</b>: Xaan Hold fortress city
     * <p><b>Caste Origin</b>: Wardens
     * <p><b>Side Quest</b>: "Blood and Stone"
     */
    IRON_PACT(
            "iron_pact",
            "The Iron Pact",
            "Kaan",
            "wardens",
            "The world is dangerous now. We will be more dangerous.",
            FactionStanding.TRUSTED,
            "blood_and_stone"
    );

    private final String id;
    private final String displayName;
    private final String leaderName;
    private final String casteOriginId;
    private final String motto;
    /** Minimum standing required to complete this faction's main story chain in Act I. */
    private final FactionStanding minimumAccessStanding;
    private final String sideQuestId;

    Faction(String id,
            String displayName,
            String leaderName,
            String casteOriginId,
            String motto,
            FactionStanding minimumAccessStanding,
            String sideQuestId) {
        this.id = id;
        this.displayName = displayName;
        this.leaderName = leaderName;
        this.casteOriginId = casteOriginId;
        this.motto = motto;
        this.minimumAccessStanding = minimumAccessStanding;
        this.sideQuestId = sideQuestId;
    }

    // ── Accessors ─────────────────────────────────────────────────────────────

    /** Returns the unique identifier of this faction (e.g., {@code "rootweavers"}). */
    public String getId() {
        return id;
    }

    /** Returns the human-readable display name of this faction. */
    public String getDisplayName() {
        return displayName;
    }

    /** Returns the name of this faction's current leader. */
    public String getLeaderName() {
        return leaderName;
    }

    /** Returns the identifier of the Mayaan caste this faction descends from. */
    public String getCasteOriginId() {
        return casteOriginId;
    }

    /** Returns the faction's philosophical motto — the sentence that defines their worldview. */
    public String getMotto() {
        return motto;
    }

    /**
     * Returns the minimum {@link FactionStanding} a player must reach with this faction
     * before their Act I main-story chapter can conclude.
     *
     * <p>Three factions require {@link FactionStanding#ACCEPTED} (first meeting of minds).
     * The Iron Pact requires {@link FactionStanding#TRUSTED} because they demand proof
     * of combat loyalty before allowing passage.
     */
    public FactionStanding getMinimumAccessStanding() {
        return minimumAccessStanding;
    }

    /** Returns the ID of this faction's dedicated side-quest chain. */
    public String getSideQuestId() {
        return sideQuestId;
    }

    /**
     * Finds a faction by its string ID. Returns {@code null} if not found.
     *
     * @param id the faction ID (e.g., {@code "forgeborn"})
     */
    public static Faction byId(String id) {
        for (Faction f : values()) {
            if (f.id.equals(id)) {
                return f;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "mayaan:faction/" + id;
    }
}
