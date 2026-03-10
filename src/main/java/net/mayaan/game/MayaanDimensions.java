package net.mayaan.game;

import net.mayaan.core.registries.Registries;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.dimension.LevelStem;

/**
 * Resource keys for all Mayaan-specific dimensions.
 *
 * <p>Mayaan has five playable dimensions. The mortal world — Xibalkaal — is the base
 * dimension. The other four are reached through specific in-game mechanisms:
 *
 * <ul>
 *   <li>{@link #XIBALKAAL} — the mortal world, where the game begins</li>
 *   <li>{@link #YAAN} — the Mayaan afterworld, reached through the rebuilt Astral Gate</li>
 *   <li>{@link #THE_MAW} — the entropic void, accessed only by falling into a Maw Rift</li>
 *   <li>{@link #THE_VOID_SHELF} — the space between worlds, navigated with a Void Shelf Key</li>
 *   <li>{@link #THE_DREAM_SEA} — liquid memory dimension, entered through a Dream Stone</li>
 * </ul>
 *
 * <h2>Reaching the Dimensions</h2>
 *
 * <h3>Yaan</h3>
 * Entered by stepping through the rebuilt Astral Gate (story chapter {@code gate_rebuilt}).
 * The gate is a two-way portal — the player can return to Xibalkaal at any time.
 * Dying in Yaan sends the player to a fragment of the afterworld before returning them
 * to their last Xibalkaal bed or spawn point.
 *
 * <h3>The Maw</h3>
 * Entered accidentally by falling into a Maw Rift (a crack in reality that forms during
 * Ley Storms or near heavily corrupted areas). Escaping requires finding a Rift Anchor
 * within the Maw — a fragment of the original sealing mechanism that remains stable.
 * The Iron Pact sells Rift Anchors to prepared adventurers.
 *
 * <h3>The Void Shelf</h3>
 * Entered by activating a Void Shelf Key near a ley-line nexus. The Void Shelf acts as
 * a fast-travel layer: distances in the Void Shelf are 1/8 of Xibalkaal distances
 * (analogous to the Nether's 8:1 ratio). Void Moths and geometric crystal formations
 * inhabit it.
 *
 * <h3>The Dream Sea</h3>
 * Entered by activating a Dream Stone at midnight (in-game time). The Dream Sea is
 * fluid — the player navigates by swimming through liquid memory. Objects found here
 * are formed from the player's own memories and the Mayaan civilization's collective
 * consciousness. Dying in the Dream Sea causes permanent memory loss of one random
 * Codex Fragment (the journal entry is replaced with static).
 */
public final class MayaanDimensions {

    // ── The Five Playable Dimensions ──────────────────────────────────────────

    /**
     * Xibalkaal — the mortal world. The base dimension where the game begins.
     *
     * <p>A patchwork of impossible biomes stitched together by Mayaan ley-lines.
     * Seven surface biomes, crystal cave systems, and dozens of Mayaan ruin sites.
     * Normal day/night cycle; standard physics.
     */
    public static final ResourceKey<LevelStem> XIBALKAAL =
            MayaanDimensions.register("xibalkaal");

    /**
     * Yaan — the Mayaan afterworld. Reached by completing the Astral Gate in Act II.
     *
     * <p>Endless floating islands of crystallized memory. The Mayaan civilization exists
     * here in a frozen state — the exact moment of The Unraveling, suspended for 3,000 years.
     * Gravity is reduced (0.6× normal). Time moves differently — one in-game hour in Yaan
     * equals six in-game hours in Xibalkaal. No natural hostile spawns; all NPCs are Mayaan
     * citizens frozen in the moment of sacrifice.
     */
    public static final ResourceKey<LevelStem> YAAN =
            MayaanDimensions.register("yaan");

    /**
     * The Maw — the entropic void. Entered through Maw Rifts; escaped through Rift Anchors.
     *
     * <p>A dimension of pure hunger. Dark, twisting corridors of anti-matter that consume
     * anything not anchored to the mortal world by an Anima signature. Hostile spawn rates
     * are extreme. Anima regeneration is halved. The entities sealed here by the Great
     * Sacrifice are partially awake and hunting. Not recommended.
     */
    public static final ResourceKey<LevelStem> THE_MAW =
            MayaanDimensions.register("the_maw");

    /**
     * The Void Shelf — the geometric space between worlds.
     *
     * <p>Silent, crystalline, and geometric. Eight Void Shelf blocks = one Xibalkaal block
     * (fast-travel layer). Inhabited by Void Moths and Void-Shelf constructs. No hostile
     * spawns aside from Void-Shelf native predators; moderate Anima regeneration.
     * TRANSLATE glyph use extends duration of stay without requiring a re-entry key.
     */
    public static final ResourceKey<LevelStem> THE_VOID_SHELF =
            MayaanDimensions.register("the_void_shelf");

    /**
     * The Dream Sea — a liquid dimension where memories take physical form.
     *
     * <p>The player navigates by swimming through liquid memory. Objects are formed from
     * collective Mayaan consciousness. Dying here causes permanent loss of one Codex Fragment
     * journal entry. The Dreamer — an ancient entity that has been watching the player — lives
     * at the Dream Sea's deepest point and is the final optional boss encounter.
     */
    public static final ResourceKey<LevelStem> THE_DREAM_SEA =
            MayaanDimensions.register("the_dream_sea");

    private MayaanDimensions() {}

    private static ResourceKey<LevelStem> register(String name) {
        return ResourceKey.create(Registries.LEVEL_STEM, MayaanIdentifier.of(name));
    }
}
