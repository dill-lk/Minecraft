package net.mayaan.game;

import net.mayaan.core.Holder;
import net.mayaan.core.Registry;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.ai.attributes.AttributeModifier;
import net.mayaan.world.entity.ai.attributes.Attributes;
import net.mayaan.world.effect.MobEffect;
import net.mayaan.world.effect.MobEffectCategory;

/**
 * Mob effects unique to Xibalkaal and its creatures.
 *
 * <p>All effects are registered under the {@code mayaan:} namespace. Their icons live
 * in {@code assets/mayaan/textures/mob_effect/}.
 *
 * <h2>Harmful effects</h2>
 * <dl>
 *   <dt>{@link #ANIMA_SLOW}</dt>
 *   <dd>Halves Anima regeneration rate. Applied by Canopy Stalker venom.
 *       Typically lasts 30 seconds (600 ticks). Stacks by duration, not intensity.</dd>
 *
 *   <dt>{@link #BLOOM_HAZE}</dt>
 *   <dd>Visual hallucination — nearby passive mobs briefly appear hostile.
 *       Applied by Bloom Walker spore clouds. Lasts 8 seconds (160 ticks).
 *       Suppressed by holding an ILLUMINATE Glyph Fragment.</dd>
 *
 *   <dt>{@link #VOID_DISORIENTATION}</dt>
 *   <dd>Dimensional disorientation — movement speed reduced 25% and input sensitivity
 *       slightly scrambled. Applied by Void Moth dust burst. Lasts 5 seconds (100 ticks).
 *       No gameplay lethality; purely a nuisance.</dd>
 *
 *   <dt>{@link #GLYPH_WEAKNESS}</dt>
 *   <dd>Glyph overload aftermath — Anima pool temporarily reduced. Applied to the caster
 *       after a failed Prime Glyph attempt or severe Anima Drought use.
 *       Lasts 60 seconds (1200 ticks); reduces attack damage slightly to signal
 *       systemic depletion.</dd>
 * </dl>
 *
 * <h2>Beneficial effects</h2>
 * <dl>
 *   <dt>{@link #ANIMA_SURGE}</dt>
 *   <dd>Ley-line resonance boost — movement speed and attack speed increased 10% while
 *       standing on an active Leyline Conduit for more than 5 consecutive seconds.
 *       Lasts for as long as the player remains on the conduit plus 10 seconds after leaving.</dd>
 * </dl>
 */
public final class MayaanMobEffects {

    // ── Harmful ───────────────────────────────────────────────────────────────

    /**
     * Anima Slow — halves Anima regeneration per tick.
     *
     * <p>Applied by {@link net.mayaan.game.entity.CanopyStalker} on first melee hit.
     * Duration: 600 ticks (30 seconds). Does not stack amplifier; re-applying resets duration.
     *
     * <p>The Anima regen reduction is handled at the server-event layer by inspecting the
     * effect on the player before calling {@link net.mayaan.game.magic.AnimaManager#onTick}.
     * This effect adds a movement-speed modifier of {@code -0} to signal presence without
     * adding any visible attribute change (the real penalty is in regen logic).
     *
     * <p>Colour: deep amber (0xC85A00) — the colour of venom crystallizing over Anima.
     */
    public static final Holder<MobEffect> ANIMA_SLOW =
            MayaanMobEffects.register("anima_slow",
                    new MobEffect(MobEffectCategory.HARMFUL, 0xC85A00)
                            .withSoundOnAdded(MayaanSounds.registerForHolder("magic.anima.slow_applied")));

    /**
     * Bloom Haze — visual hallucination effect from Bloom Walker spore clouds.
     *
     * <p>Applied when a player stands inside a spore-burst cloud for more than 1 second.
     * Duration: 160 ticks (8 seconds).
     *
     * <p>The actual hallucination rendering is handled client-side by inspecting this effect.
     * At the game logic layer, the effect reduces movement speed by 10% (slight impairment).
     *
     * <p>Colour: muted yellow-green (0x6FAA22) — fungal, organic, faintly sickening.
     */
    public static final Holder<MobEffect> BLOOM_HAZE =
            MayaanMobEffects.register("bloom_haze",
                    new MobEffect(MobEffectCategory.HARMFUL, 0x6FAA22)
                            .addAttributeModifier(
                                    Attributes.MOVEMENT_SPEED,
                                    Identifier.of("mayaan", "effect.bloom_haze"),
                                    -0.1f,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    /**
     * Void Disorientation — dimensional displacement from Void Moth dust burst.
     *
     * <p>Applied when a Void Moth is hit. Duration: 100 ticks (5 seconds).
     * Reduces movement speed by 25%. Client-side, applies a brief compass-spin effect.
     *
     * <p>Colour: silver-black (0x888899) — null-space grey.
     */
    public static final Holder<MobEffect> VOID_DISORIENTATION =
            MayaanMobEffects.register("void_disorientation",
                    new MobEffect(MobEffectCategory.HARMFUL, 0x888899)
                            .addAttributeModifier(
                                    Attributes.MOVEMENT_SPEED,
                                    Identifier.of("mayaan", "effect.void_disorientation"),
                                    -0.25f,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    /**
     * Glyph Weakness — Anima overload aftermath.
     *
     * <p>Applied after a Prime Glyph cast, whether successful or failed, and after a
     * {@link net.mayaan.game.magic.GlyphCasting.CastOutcome#NO_ANIMA} failure while in
     * severe Anima Drought. Duration: 1200 ticks (60 seconds). Reduces attack damage by
     * 2.0 to signal systemic depletion.
     *
     * <p>Colour: deep blue-grey (0x334466) — the colour of a depleted Anima crystal.
     */
    public static final Holder<MobEffect> GLYPH_WEAKNESS =
            MayaanMobEffects.register("glyph_weakness",
                    new MobEffect(MobEffectCategory.HARMFUL, 0x334466)
                            .addAttributeModifier(
                                    Attributes.ATTACK_DAMAGE,
                                    Identifier.of("mayaan", "effect.glyph_weakness"),
                                    -2.0,
                                    AttributeModifier.Operation.ADD_VALUE));

    // ── Beneficial ────────────────────────────────────────────────────────────

    /**
     * Anima Surge — ley-line resonance boost from sustained Leyline Conduit contact.
     *
     * <p>Applied by the server tick handler when a player has been standing on an active
     * {@link net.mayaan.game.block.LeylineConduitBlock} for at least 5 seconds (100 ticks).
     * Duration: refreshed each tick on the conduit + 200 ticks (10 seconds) after leaving.
     *
     * <p>Boosts movement speed by 10% and attack speed by 10%.
     * Does not stack — a second application resets duration only.
     *
     * <p>Colour: bright gold (0xFFD700) — pure ley-line resonance light.
     */
    public static final Holder<MobEffect> ANIMA_SURGE =
            MayaanMobEffects.register("anima_surge",
                    new MobEffect(MobEffectCategory.BENEFICIAL, 0xFFD700)
                            .addAttributeModifier(
                                    Attributes.MOVEMENT_SPEED,
                                    Identifier.of("mayaan", "effect.anima_surge.speed"),
                                    0.1f,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                            .addAttributeModifier(
                                    Attributes.ATTACK_SPEED,
                                    Identifier.of("mayaan", "effect.anima_surge.attack"),
                                    0.1f,
                                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL));

    private MayaanMobEffects() {}

    private static Holder<MobEffect> register(String name, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT,
                MayaanIdentifier.of(name), effect);
    }
}
