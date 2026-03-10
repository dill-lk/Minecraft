package net.mayaan.game.faction;

/**
 * The seven reputation tiers that describe a player's standing with a {@link Faction}.
 *
 * <p>Standing begins at {@link #NEUTRAL} for all factions when a new game starts.
 * Positive player choices, completed quests, and successful negotiations raise standing;
 * hostile actions, betrayals, and diplomatic failures lower it.
 *
 * <p>Certain story chapters are gated behind a minimum standing tier — see
 * {@link Faction#getMinimumAccessStanding()}.
 *
 * <h2>Standing Scale</h2>
 * <pre>
 *   HOSTILE  (-2000 … -1001) — The faction considers you an enemy. Leaders refuse dialogue.
 *   WARY     (-1000 …  -101) — Suspicious of you. Quests and trade unavailable.
 *   NEUTRAL  ( -100 …   100) — Default state. Basic interaction available.
 *   ACCEPTED (  101 …   500) — A tentative alliance. Faction quests unlock.
 *   TRUSTED  (  501 …  1000) — You've proven yourself. Inner-circle access granted.
 *   HONORED  ( 1001 …  1500) — The faction speaks well of you to others.
 *   EXALTED  ( 1501 …  2000) — You are considered one of their own.
 * </pre>
 *
 * <p>The numeric point ranges above are advisory — exact values are maintained by
 * {@link PlayerFactionData}. The tiers exist as named thresholds for story gating and UI display.
 */
public enum FactionStanding {

    /**
     * HOSTILE — The faction considers the player an active enemy.
     * All quests, trades, and dialogue are locked. NPC guards may attack on sight.
     * Recovery from HOSTILE requires significant positive action and time.
     */
    HOSTILE(-2000, -1001, "Hostile"),

    /**
     * WARY — The faction is suspicious of the player.
     * Basic dialogue is possible but quests and trades are refused.
     * Caused by hostile acts or repeated failed negotiations.
     */
    WARY(-1000, -101, "Wary"),

    /**
     * NEUTRAL — The default standing for all new players.
     * Basic dialogue and simple trade are available. No quest access yet.
     */
    NEUTRAL(-100, 100, "Neutral"),

    /**
     * ACCEPTED — The faction has tentatively accepted the player.
     * Faction questlines unlock. Significant NPC dialogue becomes available.
     * This tier is the minimum required by three of the four factions for Act I progression.
     */
    ACCEPTED(101, 500, "Accepted"),

    /**
     * TRUSTED — The faction trusts the player with sensitive information.
     * Inner-circle quests, secret lore, and faction-exclusive crafting unlock.
     * Required by the Iron Pact for Act I progression (they demand proof of loyalty).
     */
    TRUSTED(501, 1000, "Trusted"),

    /**
     * HONORED — The faction speaks well of the player to other factions.
     * Carrying HONORED standing with one faction provides minor diplomatic bonuses
     * when first meeting a faction that the HONORED faction has relations with.
     */
    HONORED(1001, 1500, "Honored"),

    /**
     * EXALTED — The highest tier. The faction considers the player one of their own.
     * All content, lore, and crafting is available. Faction leaders speak as equals.
     * Required for completing full side-quest chains.
     */
    EXALTED(1501, 2000, "Exalted");

    private final int minPoints;
    private final int maxPoints;
    private final String displayName;

    FactionStanding(int minPoints, int maxPoints, String displayName) {
        this.minPoints = minPoints;
        this.maxPoints = maxPoints;
        this.displayName = displayName;
    }

    /** Returns the minimum point value (inclusive) for this tier. */
    public int getMinPoints() {
        return minPoints;
    }

    /** Returns the maximum point value (inclusive) for this tier. */
    public int getMaxPoints() {
        return maxPoints;
    }

    /** Returns the human-readable display name for this standing tier. */
    public String getDisplayName() {
        return displayName;
    }

    /**
     * Returns {@code true} if this standing is at least as high as the required minimum.
     * Used by story gating to check whether a player may advance a faction quest chain.
     *
     * @param minimum the minimum required tier
     */
    public boolean isAtLeast(FactionStanding minimum) {
        return this.ordinal() >= minimum.ordinal();
    }

    /**
     * Returns the {@link FactionStanding} tier that corresponds to the given point total.
     * Clamps to {@link #HOSTILE} below the minimum and {@link #EXALTED} above the maximum.
     *
     * @param points the raw reputation point value
     * @return the matching standing tier
     */
    public static FactionStanding fromPoints(int points) {
        for (FactionStanding tier : values()) {
            if (points >= tier.minPoints && points <= tier.maxPoints) {
                return tier;
            }
        }
        // Below minimum → HOSTILE; above maximum → EXALTED
        return points < HOSTILE.minPoints ? HOSTILE : EXALTED;
    }

    @Override
    public String toString() {
        return "mayaan:standing/" + name().toLowerCase();
    }
}
