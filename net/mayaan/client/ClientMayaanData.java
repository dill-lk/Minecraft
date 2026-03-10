package net.mayaan.client;

import net.mayaan.game.magic.GlyphMastery;
import net.mayaan.game.magic.GlyphType;
import net.mayaan.game.magic.AnimaSystem;
import net.mayaan.network.protocol.game.ClientboundMayaanGlyphSyncPacket;

/**
 * Client-side cache of Mayaan game state received from the server.
 *
 * <p>This singleton is updated by {@link net.mayaan.client.multiplayer.ClientPacketListener}
 * whenever a {@link net.mayaan.network.protocol.game.ClientboundMayaanAnimaPacket} or
 * {@link net.mayaan.network.protocol.game.ClientboundMayaanGlyphSyncPacket} arrives.
 *
 * <p>HUD renderers and client-side UI read directly from this singleton rather than
 * going through the network — this avoids per-frame allocations.
 *
 * <h2>Thread safety</h2>
 * All writes happen on the main client thread (enforced by
 * {@link net.mayaan.network.protocol.PacketUtils#ensureRunningOnSameThread}), so
 * reads from HUD render code (also main thread) are safe without additional locking.
 */
public final class ClientMayaanData {

    /** Shared singleton instance. */
    public static final ClientMayaanData INSTANCE = new ClientMayaanData();

    // ── Anima state ───────────────────────────────────────────────────────────

    private float currentAnima = AnimaSystem.DEFAULT_MAX_ANIMA;
    private int maxAnima = AnimaSystem.DEFAULT_MAX_ANIMA;
    private boolean inDrought = false;

    // ── Glyph state ───────────────────────────────────────────────────────────

    private int knowledgeScore = 0;
    /**
     * Per-type mastery; indexed by {@link GlyphType#ordinal()}.
     * Initialized to all {@link GlyphMastery#UNLEARNED}.
     */
    private final GlyphMastery[] masteryByType;

    private ClientMayaanData() {
        GlyphType[] types = GlyphType.values();
        this.masteryByType = new GlyphMastery[types.length];
        for (int i = 0; i < types.length; i++) {
            masteryByType[i] = GlyphMastery.UNLEARNED;
        }
    }

    // ── Anima accessors ───────────────────────────────────────────────────────

    /** Returns the player's current Anima, as last received from the server. */
    public float getCurrentAnima() {
        return currentAnima;
    }

    /** Returns the player's Anima pool capacity, as last received from the server. */
    public int getMaxAnima() {
        return maxAnima;
    }

    /**
     * Returns the fill fraction of the Anima pool: {@code currentAnima / maxAnima}.
     * Always in [0.0, 1.0].
     */
    public float getFillFraction() {
        if (maxAnima <= 0) return 0f;
        return Math.max(0f, Math.min(1f, currentAnima / maxAnima));
    }

    /** Returns whether the player is currently in Anima Drought. */
    public boolean isInDrought() {
        return inDrought;
    }

    // ── Glyph accessors ───────────────────────────────────────────────────────

    /** Returns the player's Glyph Knowledge score (0–7). */
    public int getKnowledgeScore() {
        return knowledgeScore;
    }

    /** Returns the mastery tier for the given glyph type. */
    public GlyphMastery getMastery(GlyphType type) {
        int ord = type.ordinal();
        if (ord < 0 || ord >= masteryByType.length) {
            return GlyphMastery.UNLEARNED;
        }
        return masteryByType[ord];
    }

    // ── Update handlers ───────────────────────────────────────────────────────

    /**
     * Called by {@link net.mayaan.client.multiplayer.ClientPacketListener} when an
     * anima-sync packet is received. Must be called on the main client thread.
     */
    public void onAnimaSync(float currentAnima, int maxAnima, boolean inDrought) {
        this.currentAnima = currentAnima;
        this.maxAnima = maxAnima;
        this.inDrought = inDrought;
    }

    /**
     * Called by {@link net.mayaan.client.multiplayer.ClientPacketListener} when a
     * glyph-sync packet is received. Must be called on the main client thread.
     */
    public void onGlyphSync(ClientboundMayaanGlyphSyncPacket packet) {
        this.knowledgeScore = packet.getKnowledgeScore();
        for (GlyphType type : GlyphType.values()) {
            int ord = type.ordinal();
            if (ord < masteryByType.length) {
                masteryByType[ord] = packet.getMastery(type);
            }
        }
    }

    /**
     * Resets all cached values to their defaults.
     * Called when the player disconnects or the level is unloaded.
     */
    public void reset() {
        this.currentAnima = 0f;
        this.maxAnima = AnimaSystem.DEFAULT_MAX_ANIMA;
        this.inDrought = false;
        this.knowledgeScore = 0;
        for (int i = 0; i < masteryByType.length; i++) {
            masteryByType[i] = GlyphMastery.UNLEARNED;
        }
    }
}
