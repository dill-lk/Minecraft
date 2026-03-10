package net.mayaan.game.story;

import net.mayaan.game.MayaanItems;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.phys.Vec3;

/**
 * Defines the story-driven spawn conditions for a new Mayaan playthrough.
 *
 * <p>When a player starts a new game, they are not placed at the world's default
 * spawn point. Instead they wake on the {@link #ISLE_SPAWN Isle of First Light} —
 * a small island three days offshore from the main continent of Xibalkaal — and
 * receive the {@link #createStartingStoneShardStack() Stone Shard} as their first
 * and most important item.
 *
 * <h2>The Prologue Sequence</h2>
 * <ol>
 *   <li>Player wakes on the beach with no memories ({@code wake_on_beach} goal fires)</li>
 *   <li>A circle of monolith stones surrounds them; a Stone Shard glows at the centre</li>
 *   <li>The island is gentle — food, water, shelter nearby; this is intentional</li>
 *   <li>On the first night the monoliths light up and the Shard pulses — inland</li>
 *   <li>Following the pulse leads to Ix, the ancient vine-covered Construct</li>
 *   <li>Ix leads to the buried temple; inside, the Scout's Timeline Echo delivers the mission</li>
 *   <li>A ley-line vessel is waiting in a hidden cove — sail to Xibalkaal</li>
 * </ol>
 *
 * <h2>Integration</h2>
 * The server layer should check {@link StoryManager#isNewGame(java.util.UUID)} on
 * player join. If {@code true}, apply {@link #ISLE_SPAWN} as the respawn anchor,
 * give the player {@link #createStartingStoneShardStack()}, and then call
 * {@link StoryManager#markNewGameInitialized(java.util.UUID)}.
 */
public final class StorySpawnHandler {

    /**
     * The spawn coordinates for a new player on the Isle of First Light.
     *
     * <p>These coordinates place the player on the beach near the monolith circle
     * described in the Prologue. World generation anchors the full island at this point.
     * The island is located due west of Xibalkaal's Abyssal Coast.
     *
     * <ul>
     *   <li>X: 0 — centred on the world origin, which the Mayaan scouts left as an anchor</li>
     *   <li>Y: 64 — standard sea-level surface height</li>
     *   <li>Z: 0 — the monolith circle's centre</li>
     * </ul>
     */
    public static final Vec3 ISLE_SPAWN = new Vec3(0.0, 64.0, 0.0);

    /**
     * The display name of the Isle of First Light.
     * Shown as the player's location when they first load in.
     */
    public static final String ISLE_NAME = "Isle of First Light";

    /**
     * The yaw (horizontal rotation) a new player faces at spawn — looking inland
     * toward where the Stone Shard will eventually point them.
     */
    public static final float ISLE_SPAWN_YAW = 0.0f;

    /**
     * The pitch (vertical rotation) a new player has at spawn — looking straight ahead
     * across the calm, impossibly clear sea.
     */
    public static final float ISLE_SPAWN_PITCH = 0.0f;

    /**
     * Creates the starting item stack given to every new player: one Stone Shard,
     * inscribed with the SEEK glyph, faintly pulsing toward points of interest.
     *
     * <p>The Stone Shard is the player's first clue, first tutorial tool, and —
     * as revealed in Chapter 2 — a Warden's Key. It persists through the entire story.
     *
     * @return a new {@link ItemStack} containing one Stone Shard
     */
    public static ItemStack createStartingStoneShardStack() {
        return new ItemStack(MayaanItems.STONE_SHARD, 1);
    }

    /**
     * Returns the first story goal that fires automatically at spawn:
     * {@code "wake_on_beach"} in {@link StoryChapter#DREAMERS_SHORE}.
     *
     * <p>This goal is completed the moment the player's spawn event fires. Its
     * completion activates the next goal — {@code "pick_up_stone_shard"} — which
     * draws the player's attention to the glowing item at the monolith circle's centre.
     *
     * @return the first {@link StoryGoal} of the story
     */
    public static StoryGoal getSpawnGoal() {
        return StoryChapter.DREAMERS_SHORE.getGoals().get(0);
    }

    private StorySpawnHandler() {}
}
