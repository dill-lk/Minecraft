package net.mayaan.game;

import net.mayaan.core.Registry;
import net.mayaan.core.particles.SimpleParticleType;
import net.mayaan.core.registries.BuiltInRegistries;

/**
 * Particle types for all Mayaan-specific visual effects.
 *
 * <p>Particle types are registered under the {@code mayaan:} namespace. Their
 * rendering definitions live in {@code assets/mayaan/particles/}. All currently
 * registered types are {@link SimpleParticleType} — they carry no additional data
 * beyond the type itself. Colour and size are controlled in the particle JSON definition.
 *
 * <h2>Anima particles</h2>
 * <ul>
 *   <li>{@link #ANIMA_GLOW} — soft golden motes drifting up from Anima sources</li>
 *   <li>{@link #ANIMA_DRAIN} — darker, inverted motes being pulled toward the HollowKnight's hand</li>
 *   <li>{@link #LEYLINE_SHIMMER} — fine silver-blue shimmer along ley-line conduit paths</li>
 * </ul>
 *
 * <h2>Glyph cast trails</h2>
 * Each glyph type has its own distinct trail colour and motion when cast:
 * <ul>
 *   <li>{@link #GLYPH_SEEK_TRAIL} — amber; curves toward the nearest point of interest</li>
 *   <li>{@link #GLYPH_BIND_TRAIL} — deep blue; radiates rings outward then snaps inward</li>
 *   <li>{@link #GLYPH_MEND_TRAIL} — warm green; rises slowly and dissipates</li>
 *   <li>{@link #GLYPH_ILLUMINATE_TRAIL} — white-gold; explodes outward and fades</li>
 *   <li>{@link #GLYPH_STRENGTHEN_TRAIL} — ochre-brown; falls down around the target and solidifies</li>
 *   <li>{@link #GLYPH_CHANNEL_TRAIL} — cyan; flows in a directed line between cast point and target</li>
 *   <li>{@link #GLYPH_TRANSLATE_TRAIL} — deep violet; spirals inward toward the dimensional anchor</li>
 * </ul>
 *
 * <h2>Creature particles</h2>
 * <ul>
 *   <li>{@link #BLOOM_SPORE} — yellow-green drifting spores from Bloom Walker releases</li>
 *   <li>{@link #VOID_DUST} — silver-black dust bursting from a struck Void Moth</li>
 *   <li>{@link #HOLLOW_KNIGHT_DRAIN_BEAM} — Maw-purple energy particles tracing the drain beam path</li>
 * </ul>
 *
 * <h2>Story / UI particles</h2>
 * <ul>
 *   <li>{@link #ECHO_SHIMMER} — the visual shimmer that surrounds a player during a Timeline Echo</li>
 *   <li>{@link #IX_MEMORY_SPARK} — a brief sparkle when a new Ix memory fragment unlocks</li>
 * </ul>
 */
public final class MayaanParticles {

    // ── Anima ─────────────────────────────────────────────────────────────────

    /**
     * Anima Glow — soft golden motes rising from Anima Crystal blocks, Anima Shards,
     * and ley-line conduit surfaces. Drifts upward at low speed and fades.
     * Overrides the default particle limiter (visible even at minimum particle settings).
     */
    public static final SimpleParticleType ANIMA_GLOW =
            MayaanParticles.register("anima_glow", true);

    /**
     * Anima Drain — dark amber inverse-motes sucked toward the HollowKnight's
     * drain beam source. Rendered during the drain beam window.
     * Not override-limiter — high-performance area particle.
     */
    public static final SimpleParticleType ANIMA_DRAIN =
            MayaanParticles.register("anima_drain", false);

    /**
     * Leyline Shimmer — fine silver-blue shimmer particles on the surface of
     * {@link net.mayaan.game.block.LeylineConduitBlock} when Anima is flowing.
     * Very dense, very small — a shimmering carpet effect.
     */
    public static final SimpleParticleType LEYLINE_SHIMMER =
            MayaanParticles.register("leyline_shimmer", false);

    // ── Glyph cast trails ─────────────────────────────────────────────────────

    /**
     * SEEK trail — amber particles curving toward the nearest point of interest.
     * Spawned in an arc from the cast point on a {@link net.mayaan.game.magic.GlyphCasting.CastTier#BASIC} SEEK cast.
     */
    public static final SimpleParticleType GLYPH_SEEK_TRAIL =
            MayaanParticles.register("glyph_seek_trail", false);

    /**
     * BIND trail — deep cobalt-blue rings radiating outward then snapping inward.
     * Spawned in expanding rings on a BIND cast.
     */
    public static final SimpleParticleType GLYPH_BIND_TRAIL =
            MayaanParticles.register("glyph_bind_trail", false);

    /**
     * MEND trail — warm emerald-green motes rising slowly and dissipating.
     * Spawned rising from the targeted entity or block on a MEND cast.
     */
    public static final SimpleParticleType GLYPH_MEND_TRAIL =
            MayaanParticles.register("glyph_mend_trail", false);

    /**
     * ILLUMINATE trail — white-gold particles exploding outward from the cast point.
     * Dense for a BASIC cast; fills a 5-block sphere on a MAJOR cast.
     */
    public static final SimpleParticleType GLYPH_ILLUMINATE_TRAIL =
            MayaanParticles.register("glyph_illuminate_trail", false);

    /**
     * STRENGTHEN trail — ochre-brown dust falling down around the target and
     * spreading outward before dissipating at ground level.
     */
    public static final SimpleParticleType GLYPH_STRENGTHEN_TRAIL =
            MayaanParticles.register("glyph_strengthen_trail", false);

    /**
     * CHANNEL trail — cyan particles flowing in a directed line from cast point
     * to target. Renders as a continuous stream during the channel duration.
     */
    public static final SimpleParticleType GLYPH_CHANNEL_TRAIL =
            MayaanParticles.register("glyph_channel_trail", false);

    /**
     * TRANSLATE trail — deep violet spiraling particles collapsing inward toward
     * the dimensional anchor point. Dense and dramatic on a MAJOR cast.
     */
    public static final SimpleParticleType GLYPH_TRANSLATE_TRAIL =
            MayaanParticles.register("glyph_translate_trail", true);

    // ── Creature particles ────────────────────────────────────────────────────

    /**
     * Bloom Spore — yellow-green drifting spore particles released when a
     * {@link net.mayaan.game.entity.BloomWalker} is hurt or dies.
     * Persists for ~8 seconds; forms the visible cloud of the spore-burst area.
     */
    public static final SimpleParticleType BLOOM_SPORE =
            MayaanParticles.register("bloom_spore", false);

    /**
     * Void Dust — silver-black dust bursting outward from a struck
     * {@link net.mayaan.game.entity.VoidMoth}.
     * Very brief (2–3 seconds); dimensional shimmer tint.
     */
    public static final SimpleParticleType VOID_DUST =
            MayaanParticles.register("void_dust", false);

    /**
     * Hollow Knight drain beam — Maw-purple energy particles tracing the path of
     * a {@link net.mayaan.game.entity.HollowKnight}'s Anima Drain beam.
     * Spawned frame-by-frame between the Knight and its target during the drain window.
     * Override-limiter so the beam is always visible.
     */
    public static final SimpleParticleType HOLLOW_KNIGHT_DRAIN_BEAM =
            MayaanParticles.register("hollow_knight_drain_beam", true);

    // ── Story / UI particles ──────────────────────────────────────────────────

    /**
     * Echo shimmer — a soft, slow shimmer surrounding the player during an active
     * {@link net.mayaan.game.echo.TimelineEcho} sequence. Subtle enough not to obscure
     * the world; prominent enough to signal that something unusual is happening.
     */
    public static final SimpleParticleType ECHO_SHIMMER =
            MayaanParticles.register("echo_shimmer", true);

    /**
     * Ix memory spark — a brief starburst of warm-white particles at Ix's position
     * when a new memory fragment is unlocked.
     * Plays alongside {@link MayaanSounds#ENTITY_IX_MEMORY_UNLOCK}.
     */
    public static final SimpleParticleType IX_MEMORY_SPARK =
            MayaanParticles.register("ix_memory_spark", true);

    private MayaanParticles() {}

    private static SimpleParticleType register(String name, boolean overrideLimiter) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE,
                MayaanIdentifier.of(name),
                new SimpleParticleType(overrideLimiter));
    }
}
