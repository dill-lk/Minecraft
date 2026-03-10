package net.mayaan.game.biome;

import net.mayaan.core.registries.Registries;
import net.mayaan.game.MayaanIdentifier;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.level.biome.Biome;

/**
 * Resource keys for all Mayaan-specific biomes in the world of Xibalkaal.
 *
 * These biomes form the world of Xibalkaal — a patchwork of impossible landscapes
 * stitched together by Mayaan ley-lines. Each biome is unique to the Mayaan world
 * and does not appear in standard Minecraft generation.
 *
 * <h2>The Mortal World — Xibalkaal</h2>
 * <ul>
 *   <li>{@link #ETERNAL_CANOPY} — ancient overgrown jungle with Mayaan cities in the treetops</li>
 *   <li>{@link #SERPENT_HIGHLANDS} — towering misty mesas, carved by the bones of a dead god</li>
 *   <li>{@link #SHIVERING_SHELF} — frozen tundra with prehistoric creatures trapped in glaciers</li>
 *   <li>{@link #EMBER_WASTES} — volcanic badlands patrolled by fire elementals</li>
 *   <li>{@link #ABYSSAL_COAST} — sheer cliffs above a depthless ocean; sea-temples rise at moon phases</li>
 *   <li>{@link #MIRRORWOOD} — forest where reality is thin and light bends strangely</li>
 *   <li>{@link #CRYSTAL_VEINS} — underground geode networks lit by bioluminescent Anima crystals</li>
 * </ul>
 *
 * <h2>The Other Dimensions</h2>
 * <ul>
 *   <li>{@link #YAAN} — the Mayaan afterworld; floating islands of memory</li>
 *   <li>{@link #THE_MAW} — dimension of pure entropy; sealed but not destroyed</li>
 *   <li>{@link #THE_VOID_SHELF} — the geometric space between worlds used for fast travel</li>
 *   <li>{@link #THE_DREAM_SEA} — liquid dimension where memories take physical form</li>
 * </ul>
 */
public final class MayaanBiomes {

    // ── Xibalkaal surface biomes ──────────────────────────────────────────────

    /**
     * Eternal Canopy — a jungle so ancient it has its own weather system.
     * Mayaan cities are hidden in the treetops; canopy stalkers hunt from above.
     */
    public static final ResourceKey<Biome> ETERNAL_CANOPY = MayaanBiomes.register("eternal_canopy");

    /**
     * Serpent Highlands — towering mesa plateaus wrapped in mist,
     * carved through by the bones of a dead god. Ley-lines are especially dense here.
     */
    public static final ResourceKey<Biome> SERPENT_HIGHLANDS = MayaanBiomes.register("serpent_highlands");

    /**
     * The Shivering Shelf — frozen tundra where glaciers trap prehistoric creatures
     * and Mayaan ice-vaults preserve knowledge from before The Unraveling.
     */
    public static final ResourceKey<Biome> SHIVERING_SHELF = MayaanBiomes.register("shivering_shelf");

    /**
     * Ember Wastes — volcanic badlands where the ground is unstable and fire elementals roam.
     * Many Forgeborn settlements are carved into the cooled lava formations.
     */
    public static final ResourceKey<Biome> EMBER_WASTES = MayaanBiomes.register("ember_wastes");

    /**
     * Abyssal Coast — cliffs rising above a depthless ocean.
     * Sea-temples surface from the water during certain moon phases.
     */
    public static final ResourceKey<Biome> ABYSSAL_COAST = MayaanBiomes.register("abyssal_coast");

    /**
     * Mirrorwood — a forest where every tree reflects light oddly.
     * Reality is thin here; Void Moths pass through trees as if they were air.
     */
    public static final ResourceKey<Biome> MIRRORWOOD = MayaanBiomes.register("mirrorwood");

    /**
     * Crystal Veins — underground geode networks lit by bioluminescent Anima crystals.
     * Mayaan mining complexes are embedded throughout; richest source of raw Anima.
     */
    public static final ResourceKey<Biome> CRYSTAL_VEINS = MayaanBiomes.register("crystal_veins");

    // ── Other Dimensions ─────────────────────────────────────────────────────

    /**
     * Yaan — the Mayaan afterworld. Endless floating islands of crystallized memory,
     * haunted by the echoes of a civilization frozen at the moment of The Unraveling.
     */
    public static final ResourceKey<Biome> YAAN = MayaanBiomes.register("yaan");

    /**
     * The Maw — a dimension of pure entropy. Dark, twisting, and home to the entities
     * the Mayaan sealed away. Imperfectly sealed — and something is waking.
     */
    public static final ResourceKey<Biome> THE_MAW = MayaanBiomes.register("the_maw");

    /**
     * The Void Shelf — the silent geometric space between worlds.
     * The Mayaan used it as a fast-travel layer; Void Moths call it home.
     */
    public static final ResourceKey<Biome> THE_VOID_SHELF = MayaanBiomes.register("the_void_shelf");

    /**
     * The Dream Sea — a liquid dimension where memories take physical form.
     * Extremely dangerous, extremely rewarding. The Dreamer lives here.
     */
    public static final ResourceKey<Biome> THE_DREAM_SEA = MayaanBiomes.register("the_dream_sea");

    private MayaanBiomes() {}

    private static ResourceKey<Biome> register(String name) {
        return ResourceKey.create(Registries.BIOME, MayaanIdentifier.of(name));
    }
}
