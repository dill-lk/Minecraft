package net.mayaan.game.advancements;

import net.mayaan.advancements.CriterionTrigger;
import net.mayaan.core.Registry;
import net.mayaan.core.registries.BuiltInRegistries;
import net.mayaan.game.MayaanIdentifier;

/**
 * Registry of all Mayaan-specific advancement criterion triggers.
 *
 * <p>All triggers are registered under the {@code mayaan:} namespace so they can
 * be referenced in advancement JSON as:
 * <pre>
 * "criteria": {
 *   "cast_translate_prime": {
 *     "trigger": "mayaan:glyph_cast",
 *     "conditions": {
 *       "glyph_type": "translate",
 *       "cast_tier": "prime"
 *     }
 *   }
 * }
 * </pre>
 *
 * <h2>Registered triggers</h2>
 * <ul>
 *   <li>{@link #GLYPH_CAST} ({@code mayaan:glyph_cast}) — fires on successful glyph cast;
 *       optional filters: {@code glyph_type}, {@code cast_tier}, {@code drought_cast}</li>
 *   <li>{@link #STORY_GOAL} ({@code mayaan:story_goal}) — fires when a story goal is completed;
 *       optional filters: {@code goal_id}, {@code chapter}</li>
 *   <li>{@link #ECHO_EXPERIENCED} ({@code mayaan:echo_experienced}) — fires when a player
 *       fully experiences a Timeline Echo; optional filter: {@code trigger_goal_id}</li>
 * </ul>
 *
 * <p>Bootstrap via {@link #bootstrap()} from {@link net.mayaan.game.MayaanGame#bootstrap()}.
 *
 * @see GlyphCastTrigger
 * @see StoryGoalTrigger
 * @see EchoExperiencedTrigger
 */
public final class MayaanCriteriaTriggers {

    /**
     * Fires whenever a player successfully casts a glyph at any tier.
     *
     * <p>Use factory methods on {@link GlyphCastTrigger.TriggerInstance} to create criteria:
     * <ul>
     *   <li>{@link GlyphCastTrigger.TriggerInstance#anyCast()} — any cast</li>
     *   <li>{@link GlyphCastTrigger.TriggerInstance#primeCast()} — Prime-tier cast</li>
     *   <li>{@link GlyphCastTrigger.TriggerInstance#ofGlyphAndTier} — specific glyph + tier</li>
     *   <li>{@link GlyphCastTrigger.TriggerInstance#droughtCast()} — any drought-mode cast</li>
     * </ul>
     */
    public static final GlyphCastTrigger GLYPH_CAST =
            MayaanCriteriaTriggers.register("glyph_cast", new GlyphCastTrigger());

    /**
     * Fires whenever a player completes a story goal.
     *
     * <p>Use factory methods on {@link StoryGoalTrigger.TriggerInstance} to create criteria:
     * <ul>
     *   <li>{@link StoryGoalTrigger.TriggerInstance#anyGoal()} — any goal</li>
     *   <li>{@link StoryGoalTrigger.TriggerInstance#ofGoalId} — specific goal by ID</li>
     *   <li>{@link StoryGoalTrigger.TriggerInstance#inChapter} — any goal in a chapter</li>
     * </ul>
     */
    public static final StoryGoalTrigger STORY_GOAL =
            MayaanCriteriaTriggers.register("story_goal", new StoryGoalTrigger());

    /**
     * Fires whenever a player fully experiences a Timeline Echo.
     *
     * <p>Use factory methods on {@link EchoExperiencedTrigger.TriggerInstance} to create criteria:
     * <ul>
     *   <li>{@link EchoExperiencedTrigger.TriggerInstance#anyEcho()} — any echo</li>
     *   <li>{@link EchoExperiencedTrigger.TriggerInstance#ofEcho} — specific echo by trigger goal ID</li>
     * </ul>
     */
    public static final EchoExperiencedTrigger ECHO_EXPERIENCED =
            MayaanCriteriaTriggers.register("echo_experienced", new EchoExperiencedTrigger());

    private MayaanCriteriaTriggers() {}

    /**
     * Eagerly initializes all Mayaan criteria triggers by touching this class.
     * Called from {@link net.mayaan.game.MayaanGame#bootstrap()}.
     */
    public static void bootstrap() {
        MayaanCriteriaTriggers.class.getName();
    }

    private static <T extends CriterionTrigger<?>> T register(String name, T trigger) {
        return Registry.register(BuiltInRegistries.TRIGGER_TYPES,
                MayaanIdentifier.of(name), trigger);
    }
}
