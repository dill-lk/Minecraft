package net.mayaan.game.advancements;

import com.mojang.datafixers.kinds.App;
import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.mayaan.advancements.Criterion;
import net.mayaan.advancements.criterion.ContextAwarePredicate;
import net.mayaan.advancements.criterion.EntityPredicate;
import net.mayaan.advancements.criterion.SimpleCriterionTrigger;
import net.mayaan.game.magic.GlyphCasting;
import net.mayaan.game.magic.GlyphCasting.CastResult;
import net.mayaan.game.magic.GlyphCasting.CastTier;
import net.mayaan.game.magic.GlyphType;
import net.mayaan.server.level.ServerPlayer;

/**
 * Advancement criterion trigger that fires whenever a glyph is successfully cast.
 *
 * <p>Registered as {@code mayaan:glyph_cast} in {@link MayaanCriteriaTriggers}.
 *
 * <h2>Instance fields</h2>
 * All fields are optional — an absent field matches any value:
 * <ul>
 *   <li>{@code glyph_type} — restrict to a specific {@link GlyphType}</li>
 *   <li>{@code cast_tier} — restrict to a specific {@link CastTier}</li>
 *   <li>{@code drought_cast} — if {@code true}, only match casts made while in Anima Drought
 *       (i.e., {@link GlyphCasting.CastOutcome#SUCCESS_DROUGHT})</li>
 * </ul>
 *
 * <h2>Advancement JSON example</h2>
 * <pre>
 * "conditions": {
 *   "glyph_type": "mayaan:translate",
 *   "cast_tier": "prime"
 * }
 * </pre>
 *
 * @see MayaanCriteriaTriggers#GLYPH_CAST
 */
public final class GlyphCastTrigger
        extends SimpleCriterionTrigger<GlyphCastTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    /**
     * Fires the trigger for the given player if the cast result is a success.
     *
     * @param player the player who cast
     * @param result the result from {@link GlyphCasting#tryCast}
     */
    public void trigger(ServerPlayer player, CastResult result) {
        if (!result.outcome().isSuccess()) {
            return;
        }
        this.trigger(player, instance -> instance.matches(result));
    }

    /**
     * The advancement criterion instance for {@link GlyphCastTrigger}.
     *
     * @param player     optional player predicate
     * @param glyphType  optional glyph type constraint
     * @param castTier   optional cast tier constraint
     * @param droughtCast if true, only match drought-mode casts
     */
    public record TriggerInstance(
            Optional<ContextAwarePredicate> player,
            Optional<GlyphType> glyphType,
            Optional<CastTier> castTier,
            boolean droughtCast)
            implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        (App) EntityPredicate.ADVANCEMENT_CODEC
                                .optionalFieldOf("player")
                                .forGetter(TriggerInstance::player),
                        (App) GlyphType.CODEC.optionalFieldOf("glyph_type")
                                .forGetter(TriggerInstance::glyphType),
                        (App) CastTier.CODEC.optionalFieldOf("cast_tier")
                                .forGetter(TriggerInstance::castTier),
                        (App) Codec.BOOL.optionalFieldOf("drought_cast", false)
                                .forGetter(TriggerInstance::droughtCast))
                        .apply((Applicative) instance, TriggerInstance::new));

        /**
         * Creates a criterion that matches any successful glyph cast.
         */
        public static Criterion<TriggerInstance> anyCast() {
            return MayaanCriteriaTriggers.GLYPH_CAST.createCriterion(
                    new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(), false));
        }

        /**
         * Creates a criterion that matches a cast of the specific glyph type and tier.
         */
        public static Criterion<TriggerInstance> ofGlyphAndTier(GlyphType glyph, CastTier tier) {
            return MayaanCriteriaTriggers.GLYPH_CAST.createCriterion(
                    new TriggerInstance(Optional.empty(), Optional.of(glyph), Optional.of(tier), false));
        }

        /**
         * Creates a criterion that matches any Prime-tier cast.
         */
        public static Criterion<TriggerInstance> primeCast() {
            return MayaanCriteriaTriggers.GLYPH_CAST.createCriterion(
                    new TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(CastTier.PRIME), false));
        }

        /**
         * Creates a criterion that matches any cast made during Anima Drought.
         */
        public static Criterion<TriggerInstance> droughtCast() {
            return MayaanCriteriaTriggers.GLYPH_CAST.createCriterion(
                    new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty(), true));
        }

        /** Returns {@code true} if this instance matches the given cast result. */
        boolean matches(CastResult result) {
            if (glyphType.isPresent() && glyphType.get() != result.glyphType()) {
                return false;
            }
            if (castTier.isPresent() && castTier.get() != result.tier()) {
                return false;
            }
            if (droughtCast && result.outcome() != GlyphCasting.CastOutcome.SUCCESS_DROUGHT) {
                return false;
            }
            return true;
        }
    }
}
