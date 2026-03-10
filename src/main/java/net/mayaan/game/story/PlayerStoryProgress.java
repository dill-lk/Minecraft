package net.mayaan.game.story;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tracks a single player's progress through the Mayaan story.
 *
 * <p>This class maintains:
 * <ul>
 *   <li>The player's {@linkplain #getCurrentChapter() current chapter}</li>
 *   <li>The {@linkplain GoalState state} of every goal encountered</li>
 *   <li>Whether this is a brand-new game requiring story spawn initialization</li>
 * </ul>
 *
 * <p>Progress is designed to be persisted per-player. Use {@link #save()} and
 * {@link #load(Map)} for serialization to a flat string map (compatible with NBT
 * compound tag storage).
 *
 * <p>A new progress object is obtained via {@link #newGame()}; loaded data is restored
 * via {@link #load(Map)}.
 */
public final class PlayerStoryProgress {

    private static final String KEY_CHAPTER  = "chapter";
    private static final String KEY_NEW_GAME = "new_game";
    private static final String GOAL_PREFIX  = "goal:";

    private StoryChapter currentChapter;
    private boolean newGame;
    private final Map<String, GoalState> goalStates = new HashMap<>();

    private PlayerStoryProgress() {}

    // ── Factory Methods ───────────────────────────────────────────────────────

    /**
     * Creates a fresh story progress for a brand-new playthrough.
     *
     * <p>Sets the starting chapter to {@link StoryChapter#DREAMERS_SHORE} and activates
     * all of its goals. The {@link #isNewGame()} flag is set so the server knows to spawn
     * the player on the Isle of First Light and hand them the Stone Shard.
     */
    public static PlayerStoryProgress newGame() {
        PlayerStoryProgress progress = new PlayerStoryProgress();
        progress.currentChapter = StoryChapter.DREAMERS_SHORE;
        progress.newGame = true;
        // Activate every goal in the first chapter immediately
        for (StoryGoal goal : StoryChapter.DREAMERS_SHORE.getGoals()) {
            progress.goalStates.put(goal.getGlobalKey(), GoalState.ACTIVE);
        }
        return progress;
    }

    /**
     * Loads story progress from a previously {@linkplain #save() saved} string map.
     * Returns a {@link #newGame()} instance if the data is null, empty, or corrupt.
     *
     * @param data the saved data map (may be null or empty)
     * @return restored progress, or fresh new-game progress on failure
     */
    public static PlayerStoryProgress load(Map<String, String> data) {
        if (data == null || data.isEmpty()) {
            return PlayerStoryProgress.newGame();
        }
        PlayerStoryProgress progress = new PlayerStoryProgress();
        try {
            String chapterName = data.get(KEY_CHAPTER);
            progress.currentChapter = chapterName != null
                    ? StoryChapter.valueOf(chapterName)
                    : StoryChapter.DREAMERS_SHORE;

            String newGameStr = data.get(KEY_NEW_GAME);
            progress.newGame = newGameStr != null && Boolean.parseBoolean(newGameStr);

            data.forEach((key, value) -> {
                if (key.startsWith(GOAL_PREFIX)) {
                    String goalKey = key.substring(GOAL_PREFIX.length());
                    try {
                        progress.goalStates.put(goalKey, GoalState.valueOf(value));
                    } catch (IllegalArgumentException ignored) {
                        // Unknown state value — forward-compat: skip silently
                    }
                }
            });
        } catch (IllegalArgumentException e) {
            // Unknown chapter name — data from a future version; start fresh
            return PlayerStoryProgress.newGame();
        }
        return progress;
    }

    // ── Queries ───────────────────────────────────────────────────────────────

    /** Returns the chapter the player is currently in. */
    public StoryChapter getCurrentChapter() {
        return currentChapter;
    }

    /** Returns the act the player is currently in. */
    public StoryAct getCurrentAct() {
        return currentChapter.getAct();
    }

    /**
     * Returns {@code true} if this is a fresh playthrough that has not yet had
     * its story spawn initialized (spawn point set, Stone Shard given, etc.).
     */
    public boolean isNewGame() {
        return newGame;
    }

    /** Marks story-spawn initialization as done. Call after giving the player their starting items. */
    public void markNewGameInitialized() {
        this.newGame = false;
    }

    /**
     * Returns the current {@link GoalState} for the given goal.
     * Defaults to {@link GoalState#LOCKED} for goals that haven't been reached yet.
     */
    public GoalState getGoalState(StoryGoal goal) {
        return goalStates.getOrDefault(goal.getGlobalKey(), GoalState.LOCKED);
    }

    /** Returns {@code true} if the given goal has been completed. */
    public boolean isGoalCompleted(StoryGoal goal) {
        return getGoalState(goal) == GoalState.COMPLETED;
    }

    /** Returns {@code true} if the given goal is currently active. */
    public boolean isGoalActive(StoryGoal goal) {
        return getGoalState(goal) == GoalState.ACTIVE;
    }

    /**
     * Returns the list of active (non-completed) goals in the current chapter.
     * These are the objectives that should be shown in the player's HUD and journal.
     */
    public List<StoryGoal> getActiveGoals() {
        List<StoryGoal> active = new ArrayList<>();
        for (StoryGoal goal : currentChapter.getGoals()) {
            if (getGoalState(goal) == GoalState.ACTIVE) {
                active.add(goal);
            }
        }
        return Collections.unmodifiableList(active);
    }

    /**
     * Returns the required goals in the current chapter that are not yet completed.
     * When this list is empty, the chapter is ready to advance.
     */
    public List<StoryGoal> getRemainingRequiredGoals() {
        List<StoryGoal> remaining = new ArrayList<>();
        for (StoryGoal goal : currentChapter.getRequiredGoals()) {
            if (getGoalState(goal) != GoalState.COMPLETED) {
                remaining.add(goal);
            }
        }
        return Collections.unmodifiableList(remaining);
    }

    // ── Mutation ──────────────────────────────────────────────────────────────

    /**
     * Marks the given goal as {@link GoalState#COMPLETED} and checks whether
     * all required goals in the current chapter are now done.
     * If so, the chapter advances automatically and the next chapter's goals become active.
     *
     * <p>Goals belonging to a chapter other than the current one are silently ignored.
     *
     * @param goal the goal that was just fulfilled
     * @return {@code true} if completing this goal caused the story chapter to advance
     */
    public boolean completeGoal(StoryGoal goal) {
        if (goal.getChapter() != currentChapter) {
            return false;
        }
        GoalState current = goalStates.getOrDefault(goal.getGlobalKey(), GoalState.LOCKED);
        if (current == GoalState.COMPLETED) {
            return false; // Already done — idempotent
        }
        goalStates.put(goal.getGlobalKey(), GoalState.COMPLETED);
        return checkAndAdvanceChapter();
    }

    /**
     * Checks whether all required goals in the current chapter are completed.
     * If so, advances to the next chapter and activates its goals.
     *
     * @return {@code true} if the chapter advanced
     */
    private boolean checkAndAdvanceChapter() {
        boolean allRequiredDone = currentChapter.getRequiredGoals().stream()
                .allMatch(g -> goalStates.getOrDefault(g.getGlobalKey(), GoalState.LOCKED)
                        == GoalState.COMPLETED);
        if (!allRequiredDone) {
            return false;
        }
        StoryChapter next = currentChapter.next();
        if (next == null) {
            return false; // EPILOGUE complete — story is finished
        }
        currentChapter = next;
        for (StoryGoal newGoal : next.getGoals()) {
            goalStates.put(newGoal.getGlobalKey(), GoalState.ACTIVE);
        }
        return true;
    }

    // ── Serialization ─────────────────────────────────────────────────────────

    /**
     * Serializes this progress into a flat string-to-string map for storage.
     * The result can be written to an NBT compound, a JSON object, or any
     * key-value store.
     *
     * @return serialized data map
     */
    public Map<String, String> save() {
        Map<String, String> data = new HashMap<>();
        data.put(KEY_CHAPTER, currentChapter.name());
        data.put(KEY_NEW_GAME, String.valueOf(newGame));
        goalStates.forEach((key, state) -> data.put(GOAL_PREFIX + key, state.name()));
        return data;
    }
}
