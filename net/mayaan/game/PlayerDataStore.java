package net.mayaan.game;

import java.util.Map;
import java.util.UUID;
import net.mayaan.game.faction.FactionManager;
import net.mayaan.game.magic.AnimaManager;
import net.mayaan.game.magic.GlyphKnowledgeManager;
import net.mayaan.game.story.StoryManager;

/**
 * PlayerDataStore — coordinates per-player data lifecycle across all Mayaan subsystems.
 *
 * <p>When a player joins the server, their data must be loaded (or initialized) in every
 * Mayaan subsystem. When they leave, all data must be saved and unloaded from memory.
 * This class provides a single point of coordination so that callers (typically a server
 * event handler) only need one call rather than four.
 *
 * <h2>Managed subsystems</h2>
 * <ol>
 *   <li>{@link StoryManager} — story progress, chapter state, goal completion</li>
 *   <li>{@link FactionManager} — faction standing points for all four factions</li>
 *   <li>{@link GlyphKnowledgeManager} — fragment counts and mastery tiers per glyph type</li>
 *   <li>{@link AnimaManager} — Anima pool, drought counter, max Anima</li>
 * </ol>
 *
 * <h2>Data Format</h2>
 * Each subsystem receives its data as a {@code Map<String, String>} (a flat string map
 * compatible with NBT compound storage). The host server layer is responsible for
 * reading/writing these maps to persistent storage (e.g., player NBT data, a database,
 * or a file-per-player store). This class does not perform I/O.
 *
 * <h2>Usage (server event handler)</h2>
 * <pre>
 *   // On PlayerJoinEvent:
 *   PlayerSaveData save = database.load(playerId); // or empty for new player
 *   PlayerDataStore.onPlayerJoin(
 *       playerId,
 *       save.story(),
 *       save.factions(),
 *       save.glyphs(),
 *       save.anima());
 *
 *   // On PlayerLeaveEvent:
 *   PlayerSaveData snapshot = PlayerDataStore.onPlayerLeave(playerId);
 *   database.save(playerId, snapshot);
 * </pre>
 *
 * @see StoryManager
 * @see FactionManager
 * @see GlyphKnowledgeManager
 * @see AnimaManager
 */
public final class PlayerDataStore {

    /** Singleton. */
    public static final PlayerDataStore INSTANCE = new PlayerDataStore();

    private PlayerDataStore() {}

    // ── Join ──────────────────────────────────────────────────────────────────

    /**
     * Loads all Mayaan subsystem data for a player joining the server.
     *
     * <p>Pass empty maps (or {@code null} values) for a brand-new player — each subsystem
     * will initialize appropriate defaults.
     *
     * @param playerId    the joining player's UUID
     * @param storyData   serialized story progress (from {@link StoryManager}); nullable
     * @param factionData serialized faction standing data (from {@link FactionManager}); nullable
     * @param glyphData   serialized glyph knowledge data (from {@link GlyphKnowledgeManager}); nullable
     * @param animaData   serialized Anima data (from {@link AnimaManager}); nullable
     */
    public void onPlayerJoin(UUID playerId,
            Map<String, String> storyData,
            Map<String, String> factionData,
            Map<String, String> glyphData,
            Map<String, String> animaData) {
        StoryManager.INSTANCE.loadProgress(playerId, storyData);
        FactionManager.INSTANCE.loadData(playerId, factionData);
        GlyphKnowledgeManager.INSTANCE.loadKnowledge(playerId, glyphData);
        AnimaManager.INSTANCE.loadAnimaData(playerId, animaData);
    }

    // ── Leave ─────────────────────────────────────────────────────────────────

    /**
     * Saves and unloads all Mayaan subsystem data for a player leaving the server.
     *
     * <p>Returns a snapshot of all four data maps. The caller is responsible for
     * persisting these to storage before discarding the snapshot.
     *
     * @param playerId the leaving player's UUID
     * @return a {@link SaveSnapshot} containing all serialized data maps; never null
     */
    public SaveSnapshot onPlayerLeave(UUID playerId) {
        SaveSnapshot snapshot = new SaveSnapshot(
                StoryManager.INSTANCE.getProgress(playerId).save(),
                FactionManager.INSTANCE.getFactionData(playerId).save(),
                GlyphKnowledgeManager.INSTANCE.getKnowledge(playerId).save(),
                AnimaManager.INSTANCE.getAnimaData(playerId).save());

        StoryManager.INSTANCE.unloadProgress(playerId);
        FactionManager.INSTANCE.unloadData(playerId);
        GlyphKnowledgeManager.INSTANCE.unloadKnowledge(playerId);
        AnimaManager.INSTANCE.unloadAnimaData(playerId);

        return snapshot;
    }

    // ── Snapshot ──────────────────────────────────────────────────────────────

    /**
     * Saves all Mayaan data for a player without unloading them from memory.
     *
     * <p>Use this for periodic auto-saves (e.g., every 5 minutes) while the player
     * remains online.
     *
     * @param playerId the player's UUID
     * @return a {@link SaveSnapshot}; never null
     */
    public SaveSnapshot snapshot(UUID playerId) {
        return new SaveSnapshot(
                StoryManager.INSTANCE.getProgress(playerId).save(),
                FactionManager.INSTANCE.getFactionData(playerId).save(),
                GlyphKnowledgeManager.INSTANCE.getKnowledge(playerId).save(),
                AnimaManager.INSTANCE.getAnimaData(playerId).save());
    }

    // ── Nested type ───────────────────────────────────────────────────────────

    /**
     * A snapshot of all four per-player Mayaan data maps at a single point in time.
     *
     * <p>Use the four accessor methods to retrieve each map for storage. All maps are
     * non-null (but may be empty for new players with default data).
     */
    public static final class SaveSnapshot {

        private final Map<String, String> storyData;
        private final Map<String, String> factionData;
        private final Map<String, String> glyphData;
        private final Map<String, String> animaData;

        SaveSnapshot(Map<String, String> storyData, Map<String, String> factionData,
                Map<String, String> glyphData, Map<String, String> animaData) {
            this.storyData = storyData != null ? storyData : Map.of();
            this.factionData = factionData != null ? factionData : Map.of();
            this.glyphData = glyphData != null ? glyphData : Map.of();
            this.animaData = animaData != null ? animaData : Map.of();
        }

        /** Returns the serialized story progress data. */
        public Map<String, String> storyData() {
            return storyData;
        }

        /** Returns the serialized faction standing data. */
        public Map<String, String> factionData() {
            return factionData;
        }

        /** Returns the serialized glyph knowledge data. */
        public Map<String, String> glyphData() {
            return glyphData;
        }

        /** Returns the serialized Anima pool data. */
        public Map<String, String> animaData() {
            return animaData;
        }
    }
}
