package net.mayaan.game;

import net.mayaan.core.Registry;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.core.registries.Registries;
import net.mayaan.game.item.AnimaShard;
import net.mayaan.game.item.CodexFragment;
import net.mayaan.game.item.CoreShard;
import net.mayaan.game.item.GlyphFragment;
import net.mayaan.game.item.IxchelicShard;
import net.mayaan.game.item.StoneShard;
import net.mayaan.game.item.WandererKey;
import net.mayaan.game.magic.GlyphType;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.item.BlockItem;
import net.mayaan.world.item.Item;

/**
 * Registry of all Mayaan-specific items.
 *
 * Includes the player's starting item, collectible Glyph Fragments for each of the
 * seven glyph types, Anima-based crafting materials, the three Ixchelic Shards needed
 * to rebuild the Astral Gate, six of the seven Wanderer's Keys (the seventh is the
 * Stone Shard itself), and Codex Fragments used for lore delivery.
 */
public final class MayaanItems {

    // ── Starting Item ─────────────────────────────────────────────────────────

    /**
     * Stone Shard — the first item every player holds.
     * Inscribed with the SEEK glyph. Pulses toward points of interest.
     * Also Wanderer's Key #1 — the central connection piece of the Seven Keys method.
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

    // ── Ixchelic Shards (Act II Quest Items) ─────────────────────────────────

    /**
     * Ixchelic Shard One — retrieved from the Warden of the Abyss in the Crystal Veins.
     * The first piece of the shattered Astral Gate activation crystal.
     */
    public static final Item IXCHELIC_SHARD_ONE = MayaanItems.register(
            "ixchelic_shard_one",
            props -> new IxchelicShard(IxchelicShard.Index.ONE, props));

    /**
     * Ixchelic Shard Two — surrendered by the Tide Keeper in the Abyssal Coast Sea Temple.
     * The second piece of the shattered Astral Gate activation crystal.
     */
    public static final Item IXCHELIC_SHARD_TWO = MayaanItems.register(
            "ixchelic_shard_two",
            props -> new IxchelicShard(IxchelicShard.Index.TWO, props));

    /**
     * Ixchelic Shard Three — given by the Forgotten King when his temporal loop is broken.
     * The final piece of the shattered Astral Gate activation crystal.
     */
    public static final Item IXCHELIC_SHARD_THREE = MayaanItems.register(
            "ixchelic_shard_three",
            props -> new IxchelicShard(IxchelicShard.Index.THREE, props));

    // ── Wanderer's Keys (Keys 2–7; Key 1 is the Stone Shard) ─────────────────

    /**
     * Wanderer's Key 2 — The Council's Seal Ring.
     * Recovered from a buried Warden under Xaan Hold during the Maw Breach battle.
     */
    public static final Item WANDERER_KEY_2 = MayaanItems.register(
            "wanderer_key_council_seal",
            props -> new WandererKey(WandererKey.KeyIndex.COUNCIL_SEAL_RING, props));

    /**
     * Wanderer's Key 3 — The Resonance Fork.
     * Found in the Forgeborn's sealed vault at Tz'ikin after the Wardens stand down.
     */
    public static final Item WANDERER_KEY_3 = MayaanItems.register(
            "wanderer_key_resonance_fork",
            props -> new WandererKey(WandererKey.KeyIndex.RESONANCE_FORK, props));

    /**
     * Wanderer's Key 4 — The Star Chart Prism.
     * Donated by Ek at the Gate Rebuilt ceremony in the Serpent Highlands.
     */
    public static final Item WANDERER_KEY_4 = MayaanItems.register(
            "wanderer_key_star_chart_prism",
            props -> new WandererKey(WandererKey.KeyIndex.STAR_CHART_PRISM, props));

    /**
     * Wanderer's Key 5 — The Root-Bound Crystal.
     * Given freely by Elder Cenote when the player reaches HONOURED standing with the Rootweavers.
     */
    public static final Item WANDERER_KEY_5 = MayaanItems.register(
            "wanderer_key_root_bound_crystal",
            props -> new WandererKey(WandererKey.KeyIndex.ROOT_BOUND_CRYSTAL, props));

    /**
     * Wanderer's Key 6 — The Tide Keeper's Prism.
     * Surrendered alongside Ixchelic Shard Two by the Sea Temple's Tide Keeper.
     */
    public static final Item WANDERER_KEY_6 = MayaanItems.register(
            "wanderer_key_tide_keepers_prism",
            props -> new WandererKey(WandererKey.KeyIndex.TIDE_KEEPERS_PRISM, props));

    /**
     * Wanderer's Key 7 — The Forgotten King's Sigil.
     * Surrendered alongside Ixchelic Shard Three when the temporal loop is broken.
     */
    public static final Item WANDERER_KEY_7 = MayaanItems.register(
            "wanderer_key_forgotten_kings_sigil",
            props -> new WandererKey(WandererKey.KeyIndex.FORGOTTEN_KINGS_SIGIL, props));

    // ── Codex Fragments ───────────────────────────────────────────────────────

    /**
     * The First Scout Record — the preserved message placed by Ix-Channa in the Isle of
     * First Light receiving chamber. Every player receives this echo in the Prologue.
     */
    public static final Item CODEX_SCOUTS_WARNING = MayaanItems.register(
            "codex_scouts_warning",
            props -> new CodexFragment("scouts_warning", CodexFragment.Category.SCOUT_RECORD, 0, props));

    /**
     * The Council Vote Record — official minutes of the Glyph Council session in which
     * the Seven Keys compromise was voted down. Found deep in Yaan's Council archive.
     * Requires Glyph Knowledge score 5 to read in full.
     */
    public static final Item CODEX_COUNCIL_VOTE = MayaanItems.register(
            "codex_council_vote",
            props -> new CodexFragment("council_vote", CodexFragment.Category.COUNCIL_MINUTES, 5, props));

    /**
     * Kaan's Margin Note — a fragment of the original vote record with a handwritten note
     * in Heart Script by Kaan's ancestor: "We had another option. We never tried it."
     * Given by Kaan after the Maw Breach battle.
     */
    public static final Item CODEX_KAANS_MARGIN = MayaanItems.register(
            "codex_kaans_margin",
            props -> new CodexFragment("kaans_margin", CodexFragment.Category.HEART_SCRIPT, 2, props));

    /**
     * The Warden's Farewell — the Glyph Council's preserved message to the Warden of the Abyss.
     * Found in the Crystal Veins vault. Required to trigger the Warden's surrender.
     */
    public static final Item CODEX_WARDENS_FAREWELL = MayaanItems.register(
            "codex_wardens_farewell",
            props -> new CodexFragment("wardens_farewell", CodexFragment.Category.COUNCIL_MINUTES, 2, props));

    /**
     * The Gate Schematics — Xalon's technical diagrams for the Seven Keys mechanism.
     * Found in the Axis Temple; unlocks the Act III crafting recipe for the Gate housing.
     */
    public static final Item CODEX_GATE_SCHEMATICS = MayaanItems.register(
            "codex_gate_schematics",
            props -> new CodexFragment("gate_schematics", CodexFragment.Category.TECHNICAL_SCHEMATIC, 3, props));

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

