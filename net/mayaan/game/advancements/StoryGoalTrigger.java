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
import net.mayaan.game.story.StoryChapter;
import net.mayaan.game.story.StoryGoal;
import net.mayaan.server.level.ServerPlayer;

/**
 * Advancement criterion trigger that fires whenever a story goal is completed.
 *
 * <p>Registered as {@code mayaan:story_goal} in {@link MayaanCriteriaTriggers}.
 *
 * <h2>Instance fields</h2>
 * All fields are optional — an absent field matches any value:
 * <ul>
 *   <li>{@code goal_id} — the specific goal ID to match (e.g., {@code "find_isle_temple"})</li>
 *   <li>{@code chapter} — restrict to goals completed in a specific story chapter</li>
 * </ul>
 *
 * <h2>Advancement JSON example</h2>
 * <pre>
 * "conditions": {
 *   "goal_id": "open_astral_gate"
 * }
 * </pre>
 *
 * @see MayaanCriteriaTriggers#STORY_GOAL
 */
public final class StoryGoalTrigger
        extends SimpleCriterionTrigger<StoryGoalTrigger.TriggerInstance> {

    @Override
    public Codec<TriggerInstance> codec() {
        return TriggerInstance.CODEC;
    }

    /**
     * Fires the trigger for the given player when a story goal is completed.
     *
     * @param player the player who completed the goal
     * @param goal   the goal that was completed
     */
    public void trigger(ServerPlayer player, StoryGoal goal) {
        this.trigger(player, instance -> instance.matches(goal));
    }

    /**
     * The advancement criterion instance for {@link StoryGoalTrigger}.
     *
     * @param player  optional player predicate
     * @param goalId  optional goal ID to match
     * @param chapter optional chapter constraint
     */
    public record TriggerInstance(
            Optional<ContextAwarePredicate> player,
            Optional<String> goalId,
            Optional<StoryChapter> chapter)
            implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TriggerInstance> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                        (App) EntityPredicate.ADVANCEMENT_CODEC
                                .optionalFieldOf("player")
                                .forGetter(TriggerInstance::player),
                        (App) Codec.STRING.optionalFieldOf("goal_id")
                                .forGetter(TriggerInstance::goalId),
                        (App) StoryChapter.CODEC.optionalFieldOf("chapter")
                                .forGetter(TriggerInstance::chapter))
                        .apply((Applicative) instance, TriggerInstance::new));

        /**
         * Creates a criterion that matches completion of any story goal.
         */
        public static Criterion<TriggerInstance> anyGoal() {
            return MayaanCriteriaTriggers.STORY_GOAL.createCriterion(
                    new TriggerInstance(Optional.empty(), Optional.empty(), Optional.empty()));
        }

        /**
         * Creates a criterion that matches completion of the goal with the given ID.
         *
         * @param goalId the goal ID to match (e.g., {@code "find_isle_temple"})
         */
        public static Criterion<TriggerInstance> ofGoalId(String goalId) {
            return MayaanCriteriaTriggers.STORY_GOAL.createCriterion(
                    new TriggerInstance(Optional.empty(), Optional.of(goalId), Optional.empty()));
        }

        /**
         * Creates a criterion that matches completion of any goal in the given chapter.
         */
        public static Criterion<TriggerInstance> inChapter(StoryChapter chapter) {
            return MayaanCriteriaTriggers.STORY_GOAL.createCriterion(
                    new TriggerInstance(Optional.empty(), Optional.empty(), Optional.of(chapter)));
        }

        /** Returns {@code true} if this instance matches the given completed goal. */
        boolean matches(StoryGoal goal) {
            if (goalId.isPresent() && !goalId.get().equals(goal.getId())) {
                return false;
            }
            if (chapter.isPresent() && chapter.get() != goal.getChapter()) {
                return false;
            }
            return true;
        }
    }
}
