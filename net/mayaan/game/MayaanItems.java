package net.mayaan.game;

import net.mayaan.core.Registry;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.game.item.AnimaShard;
import net.mayaan.game.item.CoreShard;
import net.mayaan.game.item.GlyphFragment;
import net.mayaan.game.item.StoneShard;
import net.mayaan.game.magic.GlyphType;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.item.BlockItem;
import net.mayaan.world.item.Item;

/**
 * Registry of all Mayaan-specific items.
 *
 * Includes the player's starting item, collectible Glyph Fragments for each of the
 * seven glyph types, and Anima-based crafting materials.
 */
public final class MayaanItems {

    // ── Starting Item ─────────────────────────────────────────────────────────

    /**
     * Stone Shard — the first item every player holds.
     * Inscribed with the SEEK glyph. Pulses toward points of interest.
     */
    public static final Item STONE_SHARD = MayaanItems.register(
            "stone_shard",
            props -> new StoneShard(props));

    // ── Glyph Fragments ───────────────────────────────────────────────────────

    /** Glyph Fragment: SEEK — the will to find. */
    public static final Item GLYPH_FRAGMENT_SEEK = MayaanItems.register(
            "glyph_fragment_seek",
            props -> new GlyphFragment(GlyphType.SEEK, props));

    /** Glyph Fragment: BIND — the will to hold. */
    public static final Item GLYPH_FRAGMENT_BIND = MayaanItems.register(
            "glyph_fragment_bind",
            props -> new GlyphFragment(GlyphType.BIND, props));

    /** Glyph Fragment: MEND — the will to heal. */
    public static final Item GLYPH_FRAGMENT_MEND = MayaanItems.register(
            "glyph_fragment_mend",
            props -> new GlyphFragment(GlyphType.MEND, props));

    /** Glyph Fragment: ILLUMINATE — the will to reveal. */
    public static final Item GLYPH_FRAGMENT_ILLUMINATE = MayaanItems.register(
            "glyph_fragment_illuminate",
            props -> new GlyphFragment(GlyphType.ILLUMINATE, props));

    /** Glyph Fragment: STRENGTHEN — the will to harden. */
    public static final Item GLYPH_FRAGMENT_STRENGTHEN = MayaanItems.register(
            "glyph_fragment_strengthen",
            props -> new GlyphFragment(GlyphType.STRENGTHEN, props));

    /** Glyph Fragment: CHANNEL — the will to direct. */
    public static final Item GLYPH_FRAGMENT_CHANNEL = MayaanItems.register(
            "glyph_fragment_channel",
            props -> new GlyphFragment(GlyphType.CHANNEL, props));

    /** Glyph Fragment: TRANSLATE — the will to cross. */
    public static final Item GLYPH_FRAGMENT_TRANSLATE = MayaanItems.register(
            "glyph_fragment_translate",
            props -> new GlyphFragment(GlyphType.TRANSLATE, props));

    // ── Anima Materials ───────────────────────────────────────────────────────

    /**
     * Anima Shard — crystallized life-force from ley-line nodes.
     * The primary material cost of Glyph magic and Construct repair.
     */
    public static final Item ANIMA_SHARD = MayaanItems.register(
            "anima_shard",
            props -> new AnimaShard(props));

    /**
     * Core Shard — the crystallized Anima heart of a Construct.
     * Used to repair, bond with, or power Construct-based machinery.
     */
    public static final Item CORE_SHARD = MayaanItems.register(
            "core_shard",
            props -> new CoreShard(props));

    // ── Block Items ───────────────────────────────────────────────────────────

    /** Block item for {@link MayaanBlocks#GLYPH_STONE}. */
    public static final Item GLYPH_STONE = MayaanItems.registerBlock("glyph_stone", MayaanBlocks.GLYPH_STONE);

    /** Block item for {@link MayaanBlocks#POLISHED_GLYPH_STONE}. */
    public static final Item POLISHED_GLYPH_STONE = MayaanItems.registerBlock("polished_glyph_stone",
            MayaanBlocks.POLISHED_GLYPH_STONE);

    /** Block item for {@link MayaanBlocks#ANIMA_CRYSTAL}. */
    public static final Item ANIMA_CRYSTAL = MayaanItems.registerBlock("anima_crystal",
            MayaanBlocks.ANIMA_CRYSTAL);

    /** Block item for {@link MayaanBlocks#LEYLINE_CONDUIT}. */
    public static final Item LEYLINE_CONDUIT = MayaanItems.registerBlock("leyline_conduit",
            MayaanBlocks.LEYLINE_CONDUIT);

    /** Block item for {@link MayaanBlocks#MAYAAN_TEMPLE_BRICKS}. */
    public static final Item MAYAAN_TEMPLE_BRICKS = MayaanItems.registerBlock("mayaan_temple_bricks",
            MayaanBlocks.MAYAAN_TEMPLE_BRICKS);

    /** Block item for {@link MayaanBlocks#CRACKED_MAYAAN_TEMPLE_BRICKS}. */
    public static final Item CRACKED_MAYAAN_TEMPLE_BRICKS = MayaanItems.registerBlock(
            "cracked_mayaan_temple_bricks", MayaanBlocks.CRACKED_MAYAAN_TEMPLE_BRICKS);

    /** Block item for {@link MayaanBlocks#MOSSY_MAYAAN_TEMPLE_BRICKS}. */
    public static final Item MOSSY_MAYAAN_TEMPLE_BRICKS = MayaanItems.registerBlock(
            "mossy_mayaan_temple_bricks", MayaanBlocks.MOSSY_MAYAAN_TEMPLE_BRICKS);

    private MayaanItems() {}

    private static Item register(String name, java.util.function.Function<Item.Properties, Item> factory) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, MayaanIdentifier.of(name));
        Item item = factory.apply(new Item.Properties().setId(key));
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }

    private static Item registerBlock(String name, net.mayaan.world.level.block.Block block) {
        ResourceKey<Item> key = ResourceKey.create(Registries.ITEM, MayaanIdentifier.of(name));
        Item.Properties props = new Item.Properties().setId(key).useBlockDescriptionPrefix();
        Item item = new BlockItem(block, props);
        ((BlockItem) item).registerBlocks(Item.BY_BLOCK, item);
        return Registry.register(BuiltInRegistries.ITEM, key, item);
    }
}
