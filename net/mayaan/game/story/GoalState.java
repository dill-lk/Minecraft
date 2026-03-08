package net.mayaan.game.story;

/**
 * The current state of a {@link StoryGoal} for a specific player.
 *
 * <p>Goals begin {@link #LOCKED} and are revealed as the player progresses.
 * Once the player reaches the chapter containing a goal, it becomes {@link #ACTIVE}.
 * When the player fulfils the goal's conditions it becomes {@link #COMPLETED}.
 */
public enum GoalState {

    /**
     * The goal has not yet been unlocked.
     * Locked goals are not visible to the player in the journal or HUD.
     */
    LOCKED,

    /**
     * The goal is currently active and the player can work toward completing it.
     * Active goals are displayed in the player's story journal and objective HUD.
     */
    ACTIVE,

    /**
     * The goal has been completed.
     * Completed goals remain visible in the journal as a record of the player's journey.
     */
    COMPLETED
}
