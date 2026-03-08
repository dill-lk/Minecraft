package net.mayaan.game;

import net.mayaan.game.faction.Faction;
import net.mayaan.game.story.StoryChapter;

/**
 * MayaanGame — the central initialization point for all Mayaan-specific content.
 *
 * This class bootstraps the Mayaan game layer on top of the Minecraft engine foundation.
 * It must be called during the game's bootstrap phase, after the base Minecraft
 * registries have been initialized but before world loading begins.
 *
 * <h2>What this initializes</h2>
 * <ol>
 *   <li>{@link MayaanBlocks} — Mayaan-specific blocks (Glyph Stone, Anima Crystal, etc.)</li>
 *   <li>{@link MayaanItems} — Mayaan-specific items and block items</li>
 *   <li>{@link StoryChapter} — all 14 story chapters and their goal lists</li>
 *   <li>{@link Faction} — all four surviving factions with standing thresholds</li>
 * </ol>
 *
 * <h2>The story of Mayaan</h2>
 * This is not a sandbox. Every playthrough begins with a story.
 *
 * <p>You wake on the Isle of First Light with no memories, carrying a Stone Shard that
 * glows with an ancient Mayaan glyph. An ancient Construct named Ix has been waiting
 * for you for three thousand years. A buried temple holds a message left centuries ago
 * by a scout who somehow knew you were coming. The message ends with:
 * <em>"We're sorry for what you're about to learn."</em>
 *
 * <p>Thousands of years ago, the Mayaan — a civilization of scholars, warriors, and mystics —
 * built wonders that defied the natural order. Then, in a single night known as The Unraveling,
 * they vanished. Not destroyed. Not killed. <em>Translated.</em> They sacrificed themselves
 * to seal a cosmic force of entropy — and they are still there, holding the seal, waiting.
 *
 * <p>The seal is weakening. You carry the one tool that can change the outcome.
 * Discover what happened. Inherit their power. Choose the world's fate.
 *
 * <h2>Story Structure</h2>
 * The game progresses through three acts and twelve chapters plus prologue and epilogue,
 * each with story goals the player must accomplish. Goals are tracked per-player by
 * {@link net.mayaan.game.story.StoryManager}. New players spawn on the Isle of First Light
 * and receive the Stone Shard via {@link net.mayaan.game.story.StorySpawnHandler}.
 *
 * <h2>Faction System</h2>
 * Act I requires the player to build standing with all four surviving factions:
 * the Rootweavers, the Forgeborn, the Star Callers, and the Iron Pact.
 * Each faction controls a different obstacle blocking the path to the Axis Temple.
 * Faction reputation is tracked per-player by {@link net.mayaan.game.faction.FactionManager}.
 *
 * @see MayaanBlocks
 * @see MayaanItems
 * @see net.mayaan.game.biome.MayaanBiomes
 * @see net.mayaan.game.magic.GlyphType
 * @see net.mayaan.game.magic.AnimaSystem
 * @see net.mayaan.game.story.StoryManager
 * @see net.mayaan.game.story.StorySpawnHandler
 * @see net.mayaan.game.faction.FactionManager
 */
public final class MayaanGame {

    /** The current version of the Mayaan game layer. */
    public static final String VERSION = "0.1.0-foundation";

    private MayaanGame() {}

    /**
     * Bootstraps all Mayaan-specific registrations.
     *
     * <p>Calling this method triggers the static initializers of:
     * <ol>
     *   <li>{@link MayaanBlocks} — registers all Mayaan blocks</li>
     *   <li>{@link MayaanItems} — registers all Mayaan items (depends on blocks)</li>
     *   <li>{@link StoryChapter} — initializes all 14 story chapters and their goal lists</li>
     *   <li>{@link Faction} — initializes all four surviving factions</li>
     * </ol>
     *
     * <p>Must be called after base game bootstrapping and before any world is loaded.
     */
    public static void bootstrap() {
        // Ensure static fields are initialized in dependency order:
        // blocks must be registered before their block items
        MayaanBlocks.class.getName(); // triggers static init of MayaanBlocks
        MayaanItems.class.getName();  // triggers static init of MayaanItems (which refs MayaanBlocks)

        // Eagerly initialize the story chapter enum so that all goal lists are populated
        // before the first player joins.
        StoryChapter.values();

        // Eagerly initialize the faction enum so all faction metadata is ready.
        Faction.values();
    }
}
