package net.mayaan.game;

import net.mayaan.core.registries.Registries;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.damagesource.DamageType;

/**
 * Resource keys for all Mayaan-specific damage types.
 *
 * <p>These keys reference data-driven {@link DamageType} definitions stored in
 * {@code data/mayaan/damage_type/}. Each definition must include at minimum a
 * {@code message_id}, a {@code scaling} value, and an {@code effects} category.
 *
 * <p>Unlike the base-game {@link net.mayaan.world.damagesource.DamageTypes} interface,
 * this class uses the {@code mayaan:} namespace via {@link MayaanIdentifier#of}.
 *
 * <h2>The damage types</h2>
 *
 * <h3>Creature damage</h3>
 * <dl>
 *   <dt>{@link #ANIMA_DRAIN}</dt>
 *   <dd>Delivered by the {@link net.mayaan.game.entity.HollowKnight} drain beam.
 *       Bypasses armor; reduces the target's Anima pool rather than health directly
 *       (at the server event layer). For lethality purposes, health damage is still
 *       applied using this type.</dd>
 *
 *   <dt>{@link #BLOOM_TOXIN}</dt>
 *   <dd>Delivered by Bloom Walker spore clouds to players who remain inside too long.
 *       Low damage; primary penalty is the {@link MayaanMobEffects#BLOOM_HAZE} effect.</dd>
 *
 *   <dt>{@link #VOID_TOUCH}</dt>
 *   <dd>Delivered by contact with Void-class entities ({@link net.mayaan.game.entity.VoidMoth}
 *       dust, The Maw ambient entities). Magical; bypasses armor.</dd>
 * </dl>
 *
 * <h3>Magic damage</h3>
 * <dl>
 *   <dt>{@link #GLYPH_RECOIL}</dt>
 *   <dd>Self-inflicted damage when a Prime Glyph cast overloads.
 *       Magical; applies to the caster only. Not scaled by difficulty.</dd>
 *
 *   <dt>{@link #MAW_ENTROPY}</dt>
 *   <dd>Ambient damage dealt by The Maw dimension itself to all living entities
 *       that lack a Rift Anchor item. Ticks once every 20 seconds (400 ticks).
 *       Magical; scales with difficulty.</dd>
 * </dl>
 */
public final class MayaanDamageTypes {

    // ── Creature damage ───────────────────────────────────────────────────────

    /**
     * Anima Drain — the HollowKnight's beam attack.
     *
     * <p>Message key: {@code "death.attack.mayaan.anima_drain"}
     * → "X was drained by [Hollow Knight]".
     * Bypasses armor (configured in the data file with {@code "effects": "hurt"}).
     */
    public static final ResourceKey<DamageType> ANIMA_DRAIN =
            MayaanDamageTypes.register("anima_drain");

    /**
     * Bloom Toxin — Bloom Walker spore cloud lingering damage.
     *
     * <p>Message key: {@code "death.attack.mayaan.bloom_toxin"}
     * → "X succumbed to bloom toxin".
     * Treated as poison-like; no armor bypass.
     */
    public static final ResourceKey<DamageType> BLOOM_TOXIN =
            MayaanDamageTypes.register("bloom_toxin");

    /**
     * Void Touch — Void-class entity contact damage.
     *
     * <p>Message key: {@code "death.attack.mayaan.void_touch"}
     * → "X was unmade by the void".
     * Magical; bypasses armor.
     */
    public static final ResourceKey<DamageType> VOID_TOUCH =
            MayaanDamageTypes.register("void_touch");

    // ── Magic damage ──────────────────────────────────────────────────────────

    /**
     * Glyph Recoil — self-damage from Prime Glyph overload.
     *
     * <p>Message key: {@code "death.attack.mayaan.glyph_recoil"}
     * → "X was shattered by their own Glyph".
     * Magical; not scaled by difficulty; cannot kill the player (clamped to 80% of max health).
     * The 80% clamp is enforced at the server event layer, not in the damage type definition.
     */
    public static final ResourceKey<DamageType> GLYPH_RECOIL =
            MayaanDamageTypes.register("glyph_recoil");

    /**
     * Maw Entropy — ambient The Maw dimension damage.
     *
     * <p>Message key: {@code "death.attack.mayaan.maw_entropy"}
     * → "X was consumed by the Maw".
     * Magical; scales with difficulty; ticks every 400 ticks for all entities in
     * {@link MayaanDimensions#THE_MAW} without a Rift Anchor.
     */
    public static final ResourceKey<DamageType> MAW_ENTROPY =
            MayaanDamageTypes.register("maw_entropy");

    private MayaanDamageTypes() {}

    private static ResourceKey<DamageType> register(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, MayaanIdentifier.of(name));
    }
}
