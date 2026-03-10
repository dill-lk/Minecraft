package net.mayaan.game.story;

/**
 * A single story goal — an objective the player must accomplish to progress the narrative.
 *
 * <p>Goals are the smallest unit of story progression. Each {@link StoryChapter} contains
 * an ordered list of goals defined at enum construction time. When all
 * {@linkplain #isRequired() required} goals in a chapter are completed, the chapter
 * automatically advances to the next.
 *
 * <p>Optional goals ({@link #isRequired()} returns {@code false}) provide additional lore,
 * rewards, and narrative depth but do not block progression.
 *
 * <h2>Translation Keys</h2>
 * Goal display text is looked up using translation keys of the form:
 * <pre>  story.mayaan.&lt;chapter_id&gt;.goal.&lt;goal_id&gt;</pre>
 * For example, the "wake_on_beach" goal in the "dreamers_shore" chapter uses the key:
 * <pre>  story.mayaan.dreamers_shore.goal.wake_on_beach</pre>
 */
public final class StoryGoal {

    private final String id;
    private final StoryChapter chapter;
    private final boolean required;

    StoryGoal(String id, StoryChapter chapter, boolean required) {
        this.id = id;
        this.chapter = chapter;
        this.required = required;
    }

    /** Returns the unique identifier of this goal within its chapter (e.g., {@code "wake_on_beach"}). */
    public String getId() {
        return id;
    }

    /** Returns the chapter this goal belongs to. */
    public StoryChapter getChapter() {
        return chapter;
    }

    /**
     * Returns whether this goal must be completed to advance the story chapter.
     * Optional goals provide lore and rewards but do not block progression.
     */
    public boolean isRequired() {
        return required;
    }

    /**
     * Returns the translation key for this goal's description text.
     * Format: {@code "story.mayaan.<chapter_id>.goal.<goal_id>"}.
     */
    public String getDescriptionKey() {
        return "story.mayaan." + chapter.getId() + ".goal." + id;
    }

    /**
     * Returns the globally unique compound key for this goal, used as a map key in
     * {@link PlayerStoryProgress}. Format: {@code "<chapter_id>:<goal_id>"}.
     */
    public String getGlobalKey() {
        return chapter.getId() + ":" + id;
    }

    @Override
    public String toString() {
        return "mayaan:goal/" + getGlobalKey();
    }
}
