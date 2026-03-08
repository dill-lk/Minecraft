package net.mayaan.game.story;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Central manager for the Mayaan story system.
 *
 * <p>Maintains per-player {@link PlayerStoryProgress} and provides the primary API
 * for story interactions:
 * <ul>
 *   <li>{@link #getProgress(UUID)} — retrieve a player's current progress</li>
 *   <li>{@link #completeGoal(UUID, StoryGoal)} — mark a goal complete</li>
 *   <li>{@link #getActiveGoals(UUID)} — get the goals currently shown in the HUD/journal</li>
 *   <li>{@link #isNewGame(UUID)} — check whether a player needs story-spawn initialization</li>
 *   <li>{@link #loadProgress(UUID, Map)} / {@link #unloadProgress(UUID)} — session lifecycle</li>
 * </ul>
 *
 * <p>This class is a singleton. Use {@link #INSTANCE} to obtain it.
 * Player data is held in memory during a session and must be persisted by the server
 * layer using {@link PlayerStoryProgress#save()} and {@link PlayerStoryProgress#load(Map)}.
 */
public final class StoryManager {

    /** The singleton instance. */
    public static final StoryManager INSTANCE = new StoryManager();

    private final Map<UUID, PlayerStoryProgress> progressMap = new HashMap<>();

    private StoryManager() {}

    // ── Session lifecycle ─────────────────────────────────────────────────────

    /**
     * Returns the story progress for the given player, creating a fresh new-game
     * progress if no data exists yet.
     *
     * @param playerId the player's unique UUID
     * @return the player's current story progress
     */
    public PlayerStoryProgress getProgress(UUID playerId) {
        return progressMap.computeIfAbsent(playerId, id -> PlayerStoryProgress.newGame());
    }

    /**
     * Registers pre-loaded progress for a player. Call this on player login,
     * after reading saved data from disk, before any in-game events fire.
     *
     * @param playerId  the player's UUID
     * @param savedData the serialized data map from a previous {@link PlayerStoryProgress#save()} call;
     *                  pass an empty map or {@code null} for a new player
     */
    public void loadProgress(UUID playerId, Map<String, String> savedData) {
        progressMap.put(playerId, PlayerStoryProgress.load(savedData));
    }

    /**
     * Removes a player's in-memory progress after their data has been saved.
     * Call this on player logout.
     *
     * @param playerId the player's UUID
     */
    public void unloadProgress(UUID playerId) {
        progressMap.remove(playerId);
    }

    // ── State queries ─────────────────────────────────────────────────────────

    /**
     * Returns {@code true} if the player needs story-spawn initialization —
     * i.e., they are starting a brand-new game and have not yet been spawned on the
     * Isle of First Light or given the Stone Shard.
     *
     * @param playerId the player's UUID
     */
    public boolean isNewGame(UUID playerId) {
        return getProgress(playerId).isNewGame();
    }

    /**
     * Marks story-spawn initialization as complete for the given player.
     * Call this after the spawn location and starting items have been applied.
     *
     * @param playerId the player's UUID
     */
    public void markNewGameInitialized(UUID playerId) {
        getProgress(playerId).markNewGameInitialized();
    }

    /**
     * Returns the story chapter the player is currently in.
     *
     * @param playerId the player's UUID
     */
    public StoryChapter getCurrentChapter(UUID playerId) {
        return getProgress(playerId).getCurrentChapter();
    }

    /**
     * Returns the story act the player is currently in.
     *
     * @param playerId the player's UUID
     */
    public StoryAct getCurrentAct(UUID playerId) {
        return getProgress(playerId).getCurrentAct();
    }

    /**
     * Returns the currently active story goals for the given player.
     * These are the objectives that should be displayed in the HUD and story journal.
     *
     * @param playerId the player's UUID
     */
    public List<StoryGoal> getActiveGoals(UUID playerId) {
        return getProgress(playerId).getActiveGoals();
    }

    // ── Goal completion ───────────────────────────────────────────────────────

    /**
     * Marks a story goal as completed for the given player.
     *
     * <p>If completing this goal causes all required goals in the current chapter
     * to be done, the chapter automatically advances and the next chapter's goals
     * become active.
     *
     * @param playerId the player whose goal was completed
     * @param goal     the goal that was fulfilled
     * @return {@code true} if the story chapter advanced as a result
     */
    public boolean completeGoal(UUID playerId, StoryGoal goal) {
        return getProgress(playerId).completeGoal(goal);
    }

    /**
     * Convenience overload: complete a goal identified by its chapter and goal ID strings.
     *
     * <p>Silently returns {@code false} if the chapter or goal ID does not match any
     * known value — safe to call without pre-validation.
     *
     * @param playerId  the player's UUID
     * @param chapterId the chapter ID string (e.g., {@code "dreamers_shore"})
     * @param goalId    the goal ID string (e.g., {@code "wake_on_beach"})
     * @return {@code true} if the story chapter advanced as a result
     */
    public boolean completeGoal(UUID playerId, String chapterId, String goalId) {
        for (StoryChapter chapter : StoryChapter.values()) {
            if (!chapter.getId().equals(chapterId)) {
                continue;
            }
            StoryGoal goal = chapter.findGoal(goalId);
            if (goal != null) {
                return completeGoal(playerId, goal);
            }
        }
        return false;
    }

    /**
     * Convenience overload: complete a goal identified by the player's current chapter
     * and a goal ID string. Useful when the calling code already knows the player is
     * in the correct chapter.
     *
     * @param playerId the player's UUID
     * @param goalId   the goal ID string (e.g., {@code "pick_up_stone_shard"})
     * @return {@code true} if the story chapter advanced as a result
     */
    public boolean completeCurrentChapterGoal(UUID playerId, String goalId) {
        StoryChapter chapter = getCurrentChapter(playerId);
        StoryGoal goal = chapter.findGoal(goalId);
        return goal != null && completeGoal(playerId, goal);
    }
}
