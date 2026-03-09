package net.mayaan.game;

import net.mayaan.core.Holder;
import net.mayaan.core.Registry;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.sounds.SoundEvent;

/**
 * Sound events for all Mayaan-specific audio.
 *
 * <p>Events are registered under the {@code mayaan:} namespace so they map to files in
 * {@code assets/mayaan/sounds/}. Each constant name follows the same convention used by
 * the base-game {@link net.mayaan.sounds.SoundEvents} class:
 * {@code <CATEGORY>_<SUBJECT>_<ACTION>}.
 *
 * <h2>Entity sounds</h2>
 * Every creature has the full set: <em>ambient</em>, <em>hurt</em>, <em>death</em>, and
 * <em>step</em>. Flying entities (Void Moth) and Constructs (Mayaan Construct, Ix) replace
 * <em>step</em> with <em>idle</em> — an idle hum/servo sound.
 *
 * <h2>Game-event sounds</h2>
 * These are triggered by gameplay logic rather than entity AI, and should be played
 * at the player's position:
 * <ul>
 *   <li>{@link #GLYPH_CAST_BASIC} / {@link #GLYPH_CAST_MAJOR} / {@link #GLYPH_CAST_PRIME}</li>
 *   <li>{@link #ANIMA_DROUGHT_ONSET} — plays when the daily drought counter first exceeds threshold</li>
 *   <li>{@link #ECHO_TRIGGER} — plays when a Timeline Echo sequence begins</li>
 *   <li>{@link #CONSTRUCT_BOND} — plays when a Construct bonds with the player</li>
 *   <li>{@link #IX_MEMORY_UNLOCK} — plays when a new Ix memory is unlocked</li>
 * </ul>
 *
 * <h2>Block / environment sounds</h2>
 * <ul>
 *   <li>{@link #ANIMA_CRYSTAL_CHIME} — ambient tone from an Anima Crystal block</li>
 *   <li>{@link #LEYLINE_PULSE} — low resonance pulse heard when standing on a Leyline Conduit</li>
 *   <li>{@link #GLYPH_STONE_ACTIVATE} — click-hum when a Glyph Stone is activated</li>
 * </ul>
 */
public final class MayaanSounds {

    // ── Canopy Stalker ────────────────────────────────────────────────────────

    /** Ambient chittering — short multi-leg scrape against wood/bark. */
    public static final SoundEvent ENTITY_CANOPY_STALKER_AMBIENT =
            MayaanSounds.register("entity.canopy_stalker.ambient");

    /** Hurt — sharp metallic hiss + leg-scatter. */
    public static final SoundEvent ENTITY_CANOPY_STALKER_HURT =
            MayaanSounds.register("entity.canopy_stalker.hurt");

    /** Death — wet crack, then silence. */
    public static final SoundEvent ENTITY_CANOPY_STALKER_DEATH =
            MayaanSounds.register("entity.canopy_stalker.death");

    /** Step — light multi-foot tapping on leaves/bark. */
    public static final SoundEvent ENTITY_CANOPY_STALKER_STEP =
            MayaanSounds.register("entity.canopy_stalker.step");

    /**
     * Leap pounce — the sudden explosive rush just before the ambush strike.
     * Plays when {@link net.mayaan.world.entity.ai.goal.LeapAtTargetGoal} activates.
     */
    public static final SoundEvent ENTITY_CANOPY_STALKER_LEAP =
            MayaanSounds.register("entity.canopy_stalker.leap");

    // ── Stone Serpent ─────────────────────────────────────────────────────────

    /** Ambient — deep cave rumble, stone grinding against stone. */
    public static final SoundEvent ENTITY_STONE_SERPENT_AMBIENT =
            MayaanSounds.register("entity.stone_serpent.ambient");

    /** Hurt — low seismic crack, pained vibration. */
    public static final SoundEvent ENTITY_STONE_SERPENT_HURT =
            MayaanSounds.register("entity.stone_serpent.hurt");

    /** Death — extended grinding collapse, then stillness. */
    public static final SoundEvent ENTITY_STONE_SERPENT_DEATH =
            MayaanSounds.register("entity.stone_serpent.death");

    /** Step — heavy plate-scrape, each step shaking the floor. */
    public static final SoundEvent ENTITY_STONE_SERPENT_STEP =
            MayaanSounds.register("entity.stone_serpent.step");

    /**
     * Pre-shed — the metallic clinking of plates loosening just before a shed event.
     * Plays when {@link net.mayaan.game.entity.StoneSerpent#setPreShed(boolean)} is called with {@code true}.
     */
    public static final SoundEvent ENTITY_STONE_SERPENT_PRE_SHED =
            MayaanSounds.register("entity.stone_serpent.pre_shed");

    // ── Bloom Walker ──────────────────────────────────────────────────────────

    /** Ambient — wet, organic gurgle; spore-sac expansion. */
    public static final SoundEvent ENTITY_BLOOM_WALKER_AMBIENT =
            MayaanSounds.register("entity.bloom_walker.ambient");

    /** Hurt — muffled wet thud; spore cloud puff. */
    public static final SoundEvent ENTITY_BLOOM_WALKER_HURT =
            MayaanSounds.register("entity.bloom_walker.hurt");

    /** Death — slow collapse, extended spore-release wheeze. */
    public static final SoundEvent ENTITY_BLOOM_WALKER_DEATH =
            MayaanSounds.register("entity.bloom_walker.death");

    /** Step — soft wet plod, root tendril dragging. */
    public static final SoundEvent ENTITY_BLOOM_WALKER_STEP =
            MayaanSounds.register("entity.bloom_walker.step");

    /**
     * Spore burst — the pressurized release of hallucinogenic spores.
     * Plays when a Bloom Walker takes damage or dies.
     */
    public static final SoundEvent ENTITY_BLOOM_WALKER_SPORE_BURST =
            MayaanSounds.register("entity.bloom_walker.spore_burst");

    // ── Void Moth ─────────────────────────────────────────────────────────────

    /**
     * Ambient — a barely-audible harmonic shimmer; sounds like the space between notes.
     * Plays at very low volume due to the Void Moth's ethereal nature.
     */
    public static final SoundEvent ENTITY_VOID_MOTH_AMBIENT =
            MayaanSounds.register("entity.void_moth.ambient");

    /** Hurt — a brief dimensional tear; the sound of null-space briefly exposed. */
    public static final SoundEvent ENTITY_VOID_MOTH_HURT =
            MayaanSounds.register("entity.void_moth.hurt");

    /** Death — a soft implosion; the dimensional pocket collapses. */
    public static final SoundEvent ENTITY_VOID_MOTH_DEATH =
            MayaanSounds.register("entity.void_moth.death");

    /**
     * Wing phase — the soft phase-shift sound as a Void Moth passes through a solid block.
     * Plays whenever the moth's position crosses a non-air block boundary.
     */
    public static final SoundEvent ENTITY_VOID_MOTH_PHASE =
            MayaanSounds.register("entity.void_moth.phase");

    /**
     * Dust burst — the release of void wing dust on hit.
     * Plays when {@link net.mayaan.game.entity.VoidMoth#setDustBurstActive(boolean)} is called with {@code true}.
     */
    public static final SoundEvent ENTITY_VOID_MOTH_DUST_BURST =
            MayaanSounds.register("entity.void_moth.dust_burst");

    // ── Hollow Knight ─────────────────────────────────────────────────────────

    /**
     * Ambient — a rhythmic servo-click overlaid with a low Maw-resonance drone.
     * Distinct from a standard Construct's hum — there is something wrong in the pattern.
     */
    public static final SoundEvent ENTITY_HOLLOW_KNIGHT_AMBIENT =
            MayaanSounds.register("entity.hollow_knight.ambient");

    /** Hurt — metal impact + brief Maw-static burst. */
    public static final SoundEvent ENTITY_HOLLOW_KNIGHT_HURT =
            MayaanSounds.register("entity.hollow_knight.hurt");

    /** Death — Maw-resonance collapse, corrupted Core Shard cracking, then null silence. */
    public static final SoundEvent ENTITY_HOLLOW_KNIGHT_DEATH =
            MayaanSounds.register("entity.hollow_knight.death");

    /** Step — heavy armored footfall with a trailing Maw-resonance undertone. */
    public static final SoundEvent ENTITY_HOLLOW_KNIGHT_STEP =
            MayaanSounds.register("entity.hollow_knight.step");

    /**
     * Drain charge — the rising whine as the Anima Drain beam charges.
     * Plays when {@link net.mayaan.game.entity.HollowKnight#setDrainingActive(boolean)} is called with {@code true}.
     */
    public static final SoundEvent ENTITY_HOLLOW_KNIGHT_DRAIN_CHARGE =
            MayaanSounds.register("entity.hollow_knight.drain_charge");

    /**
     * Drain beam — the sustained tone of the Anima Drain beam while active.
     * Looped at the server event layer for the duration of the drain.
     */
    public static final SoundEvent ENTITY_HOLLOW_KNIGHT_DRAIN_BEAM =
            MayaanSounds.register("entity.hollow_knight.drain_beam");

    // ── Mayaan Construct ──────────────────────────────────────────────────────

    /**
     * Ambient / idle — the quiet, rhythmic hum of an active Core Shard. Pleasant, measured.
     * Neutral Constructs make this sound when standing still.
     */
    public static final SoundEvent ENTITY_MAYAAN_CONSTRUCT_IDLE =
            MayaanSounds.register("entity.mayaan_construct.idle");

    /** Hurt — a solid clank; internal mechanism protest. */
    public static final SoundEvent ENTITY_MAYAAN_CONSTRUCT_HURT =
            MayaanSounds.register("entity.mayaan_construct.hurt");

    /** Death — Core Shard fracture; a long final tone, then nothing. */
    public static final SoundEvent ENTITY_MAYAAN_CONSTRUCT_DEATH =
            MayaanSounds.register("entity.mayaan_construct.death");

    /** Step — measured articulated-plate footfall. */
    public static final SoundEvent ENTITY_MAYAAN_CONSTRUCT_STEP =
            MayaanSounds.register("entity.mayaan_construct.step");

    // ── Ix Companion ──────────────────────────────────────────────────────────

    /**
     * Ix idle — the characteristic hum of Ix's partially damaged Core Shard.
     * Slightly lower pitch than a healthy Construct; the occasional stutter.
     */
    public static final SoundEvent ENTITY_IX_IDLE =
            MayaanSounds.register("entity.ix.idle");

    /** Ix hurt — Ix absorbs a hit; a protective Anima-resonance burst. */
    public static final SoundEvent ENTITY_IX_HURT =
            MayaanSounds.register("entity.ix.hurt");

    /** Ix death — should never play under normal game conditions (Ix is unkillable in the canon path). */
    public static final SoundEvent ENTITY_IX_DEATH =
            MayaanSounds.register("entity.ix.death");

    /** Ix step — quiet, slightly uneven footfall (one joint damaged). */
    public static final SoundEvent ENTITY_IX_STEP =
            MayaanSounds.register("entity.ix.step");

    /**
     * Ix memory unlock — the crystalline tone that plays when a new Ix memory fragment is recovered.
     * Also played at the same moment as {@link #IX_MEMORY_UNLOCK}.
     */
    public static final SoundEvent ENTITY_IX_MEMORY_UNLOCK =
            MayaanSounds.register("entity.ix.memory_unlock");

    /**
     * Ix repair — the warm resonance sound when Ix is repaired with a Core Shard or Anima Shards.
     */
    public static final SoundEvent ENTITY_IX_REPAIR =
            MayaanSounds.register("entity.ix.repair");

    // ── Glyph casting ─────────────────────────────────────────────────────────

    /**
     * Basic glyph cast — a brief focused chime; the world acknowledging a small request.
     * Played when {@link net.mayaan.game.magic.GlyphCasting.CastTier#BASIC} succeeds.
     */
    public static final SoundEvent GLYPH_CAST_BASIC =
            MayaanSounds.register("magic.glyph.cast_basic");

    /**
     * Major glyph cast — a resonant harmonic wave; something significant just shifted.
     * Played when {@link net.mayaan.game.magic.GlyphCasting.CastTier#MAJOR} succeeds.
     */
    public static final SoundEvent GLYPH_CAST_MAJOR =
            MayaanSounds.register("magic.glyph.cast_major");

    /**
     * Prime glyph cast — a deep, world-shaking tone that lingers for several seconds.
     * Plays at full volume server-wide when {@link net.mayaan.game.magic.GlyphCasting.CastTier#PRIME} is invoked.
     * The sound of reality bending.
     */
    public static final SoundEvent GLYPH_CAST_PRIME =
            MayaanSounds.register("magic.glyph.cast_prime");

    /**
     * Glyph recoil — the sharp, dissonant crack of a failed Prime glyph overloading.
     * Played when the caster sustains glyph-recoil damage.
     */
    public static final SoundEvent GLYPH_RECOIL =
            MayaanSounds.register("magic.glyph.recoil");

    // ── Anima events ──────────────────────────────────────────────────────────

    /**
     * Anima drought onset — a soft but unmistakable change in ambient tone.
     * The world sounds quieter, slightly hollow. Plays when the drought counter
     * first exceeds {@link net.mayaan.game.magic.AnimaSystem#DROUGHT_THRESHOLD}.
     */
    public static final SoundEvent ANIMA_DROUGHT_ONSET =
            MayaanSounds.register("magic.anima.drought_onset");

    /**
     * Anima drought relief — the gradual return of ambient tone as the drought clears.
     * Plays at dawn when {@link net.mayaan.game.magic.AnimaManager#onDayChange()} resets the counter.
     */
    public static final SoundEvent ANIMA_DROUGHT_RELIEF =
            MayaanSounds.register("magic.anima.drought_relief");

    // ── Timeline Echo ─────────────────────────────────────────────────────────

    /**
     * Echo trigger — plays at the moment a Timeline Echo sequence begins.
     * A reverberant tone, as if a stone was dropped into deep water.
     */
    public static final SoundEvent ECHO_TRIGGER =
            MayaanSounds.register("echo.trigger");

    /**
     * Echo end — plays when the last line of a Timeline Echo sequence completes.
     * Gentler than the trigger; the water settling.
     */
    public static final SoundEvent ECHO_END =
            MayaanSounds.register("echo.end");

    // ── Construct interactions ────────────────────────────────────────────────

    /**
     * Construct bond — the warm dual-tone chime when a Core Shard bonds a Construct.
     * Plays once for both the player and Construct.
     */
    public static final SoundEvent CONSTRUCT_BOND =
            MayaanSounds.register("entity.construct.bond");

    /**
     * Ix memory unlock (global event key) — the external trigger used by the story layer.
     * Play this instead of {@link #ENTITY_IX_MEMORY_UNLOCK} when the memory unlock
     * is triggered programmatically (not via direct interaction).
     */
    public static final SoundEvent IX_MEMORY_UNLOCK =
            MayaanSounds.register("entity.ix.memory_unlock_event");

    // ── Blocks ────────────────────────────────────────────────────────────────

    /**
     * Anima Crystal chime — the gentle, crystalline ambient tone emitted by
     * {@link net.mayaan.game.block.AnimaCrystalBlock} when Anima is flowing nearby.
     * Loops while the crystal is in an Anima-active state.
     */
    public static final SoundEvent ANIMA_CRYSTAL_CHIME =
            MayaanSounds.register("block.anima_crystal.chime");

    /**
     * Leyline pulse — the low resonance pulse heard when standing on a
     * {@link net.mayaan.game.block.LeylineConduitBlock}.
     * Plays every 40 ticks while a player is within 2 blocks of a conduit.
     */
    public static final SoundEvent LEYLINE_PULSE =
            MayaanSounds.register("block.leyline_conduit.pulse");

    /**
     * Glyph Stone activate — the click-and-hum when a
     * {@link net.mayaan.game.block.GlyphStoneBlock} is interacted with or activated.
     */
    public static final SoundEvent GLYPH_STONE_ACTIVATE =
            MayaanSounds.register("block.glyph_stone.activate");

    private MayaanSounds() {}

    private static SoundEvent register(String name) {
        return Registry.register(BuiltInRegistries.SOUND_EVENT,
                MayaanIdentifier.of(name),
                SoundEvent.createVariableRangeEvent(MayaanIdentifier.of(name)));
    }

    /**
     * Returns a {@link Holder.Reference} for the registered sound event.
     * Use this variant when downstream code needs a {@code Holder<SoundEvent>}
     * (e.g., {@link net.mayaan.world.effect.MobEffect#withSoundOnAdded}).
     */
    static Holder.Reference<SoundEvent> registerForHolder(String name) {
        return Registry.registerForHolder(BuiltInRegistries.SOUND_EVENT,
                MayaanIdentifier.of(name),
                SoundEvent.createVariableRangeEvent(MayaanIdentifier.of(name)));
    }
}
