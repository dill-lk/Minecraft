package net.mayaan.game;

import java.util.UUID;
import net.mayaan.game.magic.AnimaManager;
import net.mayaan.game.magic.GlyphKnowledgeManager;
import net.mayaan.game.magic.GlyphMastery;
import net.mayaan.game.magic.GlyphType;
import net.mayaan.game.magic.PlayerAnimaData;
import net.mayaan.network.protocol.game.ClientboundMayaanAnimaPacket;
import net.mayaan.network.protocol.game.ClientboundMayaanGlyphSyncPacket;
import net.mayaan.server.level.ServerPlayer;

/**
 * Server-side helper that builds and sends Mayaan state-sync packets to players.
 *
 * <p>This class has no state — it simply reads from the Mayaan manager singletons
 * and calls {@link ServerPlayer#connection connection.send()} on the target player.
 *
 * <h2>When to send</h2>
 * <ul>
 *   <li>{@link #sendAnimaSync(ServerPlayer)} — called every
 *       {@value #ANIMA_SYNC_INTERVAL} ticks from
 *       {@link MayaanServerEvents#onPlayerTick}, and once on join.</li>
 *   <li>{@link #sendGlyphSync(ServerPlayer)} — called once on join and whenever
 *       a fragment is awarded (from item use / command).</li>
 *   <li>{@link #sendAll(ServerPlayer)} — convenience; sends both packets at once
 *       (used on player join).</li>
 * </ul>
 */
public final class MayaanPacketSender {

    /**
     * How often (in server ticks) to push an anima sync packet to the client.
     * At 20 ticks/second this is 2 syncs/second — enough for smooth bar updates
     * without flooding the connection.
     */
    public static final int ANIMA_SYNC_INTERVAL = 10;

    private MayaanPacketSender() {}

    /**
     * Sends the current Anima pool state to the given player's client.
     *
     * @param player the target player
     */
    public static void sendAnimaSync(ServerPlayer player) {
        UUID id = player.getUUID();
        PlayerAnimaData data = AnimaManager.INSTANCE.getAnimaData(id);
        player.connection.send(new ClientboundMayaanAnimaPacket(
                data.getCurrentAnima(),
                data.getMaxAnima(),
                data.isInDrought()));
    }

    /**
     * Sends the current Glyph Knowledge state to the given player's client.
     *
     * @param player the target player
     */
    public static void sendGlyphSync(ServerPlayer player) {
        UUID id = player.getUUID();
        int score = GlyphKnowledgeManager.INSTANCE.getKnowledgeScore(id);
        GlyphType[] types = GlyphType.values();
        byte[] ordinals = new byte[types.length];
        for (int i = 0; i < types.length; i++) {
            GlyphMastery mastery = GlyphKnowledgeManager.INSTANCE.getMastery(id, types[i]);
            ordinals[i] = (byte) mastery.ordinal();
        }
        player.connection.send(new ClientboundMayaanGlyphSyncPacket(score, ordinals));
    }

    /**
     * Sends both Anima and Glyph sync packets to the given player.
     *
     * <p>Use this on player join so the client has a complete initial state.
     *
     * @param player the target player
     */
    public static void sendAll(ServerPlayer player) {
        sendAnimaSync(player);
        sendGlyphSync(player);
    }
}
