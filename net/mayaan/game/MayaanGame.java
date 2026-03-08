package net.mayaan.game;

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
 * </ol>
 *
 * <h2>The world of Mayaan</h2>
 * Thousands of years ago, the Mayaan — a mighty empire of scholars, warriors, and mystics —
 * built wonders that defied the natural order. Their cities floated above canopies of eternal
 * forest. Their priests could speak to stars. Then, in a single night known as The Unraveling,
 * everything vanished.
 *
 * <p>You arrive on this world with nothing but your hands, your wits, and a fragment of a map
 * carved in a language nobody alive can read. Discover what happened to the Mayaan.
 * Inherit their power. Or become the next civilization to be swallowed by the dark.
 *
 * @see MayaanBlocks
 * @see MayaanItems
 * @see net.mayaan.game.biome.MayaanBiomes
 * @see net.mayaan.game.magic.GlyphType
 * @see net.mayaan.game.magic.AnimaSystem
 */
public final class MayaanGame {

    /** The current version of the Mayaan game layer. */
    public static final String VERSION = "0.1.0-foundation";

    private MayaanGame() {}

    /**
     * Bootstraps all Mayaan-specific registrations.
     *
     * <p>Calling this method triggers the static initializers of {@link MayaanBlocks} and
     * {@link MayaanItems}, registering all Mayaan content into the built-in registries.
     *
     * <p>Must be called after base game bootstrapping and before any world is loaded.
     */
    public static void bootstrap() {
        // Ensure static fields are initialized in dependency order:
        // blocks must be registered before their block items
        MayaanBlocks.class.getName(); // triggers static init of MayaanBlocks
        MayaanItems.class.getName();  // triggers static init of MayaanItems (which refs MayaanBlocks)
    }
}
