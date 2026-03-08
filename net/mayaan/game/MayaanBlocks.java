package net.mayaan.game;

import net.mayaan.core.Registry;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.game.block.AnimaCrystalBlock;
import net.mayaan.game.block.GlyphStoneBlock;
import net.mayaan.game.block.LeylineConduitBlock;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.SoundType;
import net.mayaan.world.level.block.state.BlockBehaviour;
import net.mayaan.world.level.material.MapColor;

/**
 * Registry of all Mayaan-specific blocks.
 *
 * These blocks form the physical building language of the Mayaan world — from the
 * glowing Anima Crystals found at ley-line crossings to the ancient Glyph Stone
 * that still hums with inscribed power thousands of years after The Unraveling.
 */
public final class MayaanBlocks {

    // ── Mayaan Stone & Structure ──────────────────────────────────────────────

    /**
     * Glyph Stone — carved Mayaan temple stone inscribed with ancient glyphs.
     * Harder than normal stone; provides a faint magical hum to Anima-sensitive players.
     */
    public static final Block GLYPH_STONE = MayaanBlocks.register(
            "glyph_stone",
            GlyphStoneBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(2.5f, 9.0f)
                    .sound(SoundType.STONE));

    /**
     * Polished Glyph Stone — dressed Mayaan stone, used for refined architectural elements.
     */
    public static final Block POLISHED_GLYPH_STONE = MayaanBlocks.register(
            "polished_glyph_stone",
            GlyphStoneBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.STONE)
                    .requiresCorrectToolForDrops()
                    .strength(2.5f, 9.0f)
                    .sound(SoundType.STONE));

    // ── Anima & Ley-line ──────────────────────────────────────────────────────

    /**
     * Anima Crystal Block — deposits of crystallized Anima found at ley-line crossings.
     * Emits golden particles and produces light level 10. Primary source of raw Anima.
     */
    public static final Block ANIMA_CRYSTAL = MayaanBlocks.register(
            "anima_crystal",
            AnimaCrystalBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_YELLOW)
                    .requiresCorrectToolForDrops()
                    .strength(1.5f, 5.0f)
                    .lightLevel(state -> 10)
                    .sound(SoundType.AMETHYST));

    /**
     * Ley-line Conduit — channels Anima between ley-line nodes.
     * Active conduits glow faintly gold and serve as fast-travel waypoints.
     */
    public static final Block LEYLINE_CONDUIT = MayaanBlocks.register(
            "leyline_conduit",
            LeylineConduitBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_ORANGE)
                    .requiresCorrectToolForDrops()
                    .strength(3.0f, 12.0f)
                    .lightLevel(state -> 7)
                    .sound(SoundType.STONE));

    // ── Mayaan Temple Materials ───────────────────────────────────────────────

    /**
     * Mayaan Temple Bricks — the characteristic reddish-brown blocks found in Mayaan ruins.
     * Weathered but extraordinarily durable; immune to most explosive forces.
     */
    public static final Block MAYAAN_TEMPLE_BRICKS = MayaanBlocks.register(
            "mayaan_temple_bricks",
            GlyphStoneBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .requiresCorrectToolForDrops()
                    .strength(3.0f, 1200.0f)
                    .sound(SoundType.STONE));

    /**
     * Cracked Mayaan Temple Bricks — damaged temple blocks, common in ruins.
     */
    public static final Block CRACKED_MAYAAN_TEMPLE_BRICKS = MayaanBlocks.register(
            "cracked_mayaan_temple_bricks",
            GlyphStoneBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .requiresCorrectToolForDrops()
                    .strength(3.0f, 1200.0f)
                    .sound(SoundType.STONE));

    /**
     * Mossy Mayaan Temple Bricks — temple blocks overgrown after millennia in the jungle.
     */
    public static final Block MOSSY_MAYAAN_TEMPLE_BRICKS = MayaanBlocks.register(
            "mossy_mayaan_temple_bricks",
            GlyphStoneBlock::new,
            BlockBehaviour.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_RED)
                    .requiresCorrectToolForDrops()
                    .strength(2.5f, 1200.0f)
                    .sound(SoundType.STONE));

    private MayaanBlocks() {}

    private static Block register(String name, java.util.function.Function<BlockBehaviour.Properties, Block> factory,
            BlockBehaviour.Properties properties) {
        ResourceKey<Block> key = ResourceKey.create(Registries.BLOCK, MayaanIdentifier.of(name));
        Block block = factory.apply(properties);
        return Registry.register(BuiltInRegistries.BLOCK, key, block);
    }
}
