package net.mayaan.game;

import net.mayaan.game.echo.TimelineEchoRegistry;
import net.mayaan.game.entity.MayaanEntities;
import net.mayaan.game.faction.Faction;
import net.mayaan.game.item.IxchelicShard;
import net.mayaan.game.item.WandererKey;
import net.mayaan.game.magic.AnimaManager;
import net.mayaan.game.magic.GlyphCasting;
import net.mayaan.game.magic.GlyphKnowledgeManager;
import net.mayaan.game.magic.GlyphMastery;
import net.mayaan.game.magic.GlyphType;
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
 *   <li>{@link MayaanItems} — Mayaan-specific items and block items (including Ixchelic Shards,
 *       Wanderer's Keys, and Codex Fragments)</li>
 *   <li>{@link StoryChapter} — all 14 story chapters and their goal lists</li>
 *   <li>{@link Faction} — all four surviving factions with standing thresholds</li>
 *   <li>{@link GlyphType} / {@link GlyphMastery} — glyph type and mastery tier enums</li>
 *   <li>{@link TimelineEchoRegistry} — all 12 major story Timeline Echoes</li>
 *   <li>{@link IxchelicShard.Index} / {@link WandererKey.KeyIndex} — Act II quest item enums</li>
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
 * <h2>Glyph Knowledge</h2>
 * Players collect {@link net.mayaan.game.item.GlyphFragment Glyph Fragments} across all seven
 * glyph types. Fragment count determines mastery tier ({@link GlyphMastery}); the number
 * of glyph types at {@link GlyphMastery#PRACTICED}+ is the Glyph Knowledge score (0–7),
 * the second major story gate alongside faction standing. Per-player tracking is handled by
 * {@link GlyphKnowledgeManager}.
 *
 * <h2>Anima</h2>
 * Anima is the life-force of Xibalkaal. Players have an Anima pool that starts at
 * {@link net.mayaan.game.magic.AnimaSystem#DEFAULT_MAX_ANIMA}. Casting Glyph spells spends
 * Anima; standing on ley-lines regenerates it faster. Excessive daily use causes Anima Drought.
 * Per-player tracking is handled by {@link AnimaManager}.
 *
 * <h2>Timeline Echoes</h2>
 * Timeline Echoes are cinematic memory sequences triggered by Glyph Shards and Anima-saturated
 * locations. The 12 major story echoes — from the Prologue's Scout's Warning through
 * Camazotz's final communication in Act III — are defined in {@link TimelineEchoRegistry}.
 *
 * <h2>Act II Quest Items</h2>
 * The three {@link net.mayaan.game.item.IxchelicShard Ixchelic Shards} and six of the seven
 * {@link net.mayaan.game.item.WandererKey Wanderer's Keys} are registered as distinct items
 * collected throughout the story. All seven keys are needed for the Seven Keys sealing method.
 *
 * @see MayaanBlocks
 * @see MayaanItems
 * @see net.mayaan.game.biome.MayaanBiomes
 * @see net.mayaan.game.magic.GlyphType
 * @see net.mayaan.game.magic.AnimaSystem
 * @see GlyphKnowledgeManager
 * @see AnimaManager
 * @see net.mayaan.game.story.StoryManager
 * @see net.mayaan.game.story.StorySpawnHandler
 * @see net.mayaan.game.faction.FactionManager
 * @see TimelineEchoRegistry
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
     *   <li>{@link GlyphType} / {@link GlyphMastery} — glyph progression enums</li>
     *   <li>{@link TimelineEchoRegistry} — builds all 12 major Timeline Echo sequences</li>
     *   <li>{@link IxchelicShard.Index} / {@link WandererKey.KeyIndex} — Act II quest enums</li>
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

        // Eagerly initialize glyph progression enums.
        GlyphType.values();
        GlyphMastery.values();

        // Eagerly build all Timeline Echo sequences so they are ready before world load.
        TimelineEchoRegistry.all();

        // Eagerly initialize Act II quest item enums.
        IxchelicShard.Index.values();
        WandererKey.KeyIndex.values();

        // Eagerly register all entity types so they are available at world load.
        MayaanEntities.class.getName();

        // Eagerly initialize cast tier enum.
        GlyphCasting.CastTier.values();
        GlyphCasting.CastOutcome.values();

        // Eagerly initialize dimension registry.
        MayaanDimensions.class.getName();

        // Touch singleton managers so they are initialized (though they hold no static
        // state that needs warming — this just confirms they are classloaded early).
        AnimaManager.INSTANCE.getClass();
        GlyphKnowledgeManager.INSTANCE.getClass();
        PlayerDataStore.INSTANCE.getClass();
    }
}
