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
import net.mayaan.server.level.ServerPlayer;

/**
 * Advancement criterion trigger that fires when a player fully experiences a Timeline Echo.
 *
 * <p>A Timeline Echo is "fully experienced" when the player meets all knowledge and glyph gates
 * defined on the echo and the complete sequence plays — not the truncated version. The trigger
 * fires from {@link net.mayaan.game.MayaanServerEvents#onEchoCompleted}.
 *
 * <p>Registered as {@code mayaan:echo_experienced} in {@link MayaanCriteriaTriggers}.
 *
 * <h2>Instance fields</h2>
 * <ul>
 *   <li>{@code trigger_goal_id} — optional; restricts to the echo keyed by this goal ID
 *       (e.g., {@code "prologue_scouts_warning"}, {@code "final_transmission"})</li>
 * </ul>
 *
 * <h2>Advancement JSON example</h2>
 * <pre>
 * "conditions": {
 *   "trigger_goal_id": "final_transmission"
 * }
 * </pre>
 *
 * @see MayaanCriteriaTriggers#ECHO_EXPERIENCED
 * @see net.mayaan.game.echo.TimelineEchoRegistry
 */
public final class EchoExperiencedTrigger
        extends SimpleCriterionTrigger<EchoExperiencedTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    /**
     * Fires the trigger for the given player when an echo sequence completes in full.
     *
     * @param player        the player who experienced the echo
     * @param triggerGoalId the echo's trigger goal ID from {@link net.mayaan.game.echo.TimelineEcho}
     */
    public void trigger(ServerPlayer player, String triggerGoalId) {
        this.trigger(player, instance -> instance.matches(triggerGoalId));
    }

    /**
     * The advancement criterion instance for {@link EchoExperiencedTrigger}.
     *
     * @param player        optional player predicate
     * @param triggerGoalId optional trigger goal ID to constrain
     */
    public record TriggerInstance(
            Optional<ContextAwarePredicate> player,
            Optional<String> triggerGoalId)
            implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        (App) EntityPredicate.ADVANCEMENT_CODEC
                                .optionalFieldOf("player")
                                .forGetter(TriggerInstance::player),
                        (App) Codec.STRING.optionalFieldOf("trigger_goal_id")
                                .forGetter(TriggerInstance::triggerGoalId))
                        .apply((Applicative) instance, TriggerInstance::new));

        /**
         * Creates a criterion that matches any fully-experienced echo.
         */
        public static Criterion<TriggerInstance> anyEcho() {
            return MayaanCriteriaTriggers.ECHO_EXPERIENCED.createCriterion(
                    new TriggerInstance(Optional.empty(), Optional.empty()));
        }

        /**
         * Creates a criterion that matches the echo with the given trigger goal ID.
         *
         * @param goalId the trigger goal ID of the echo
         */
        public static Criterion<TriggerInstance> ofEcho(String goalId) {
            return MayaanCriteriaTriggers.ECHO_EXPERIENCED.createCriterion(
                    new TriggerInstance(Optional.empty(), Optional.of(goalId)));
        }

        /** Returns {@code true} if this instance matches the given trigger goal ID. */
        boolean matches(String triggeredGoalId) {
            return triggerGoalId.isEmpty() || triggerGoalId.get().equals(triggeredGoalId);
        }
    }
}
