package net.mayaan.network.protocol.game;

import net.mayaan.game.magic.GlyphMastery;
import net.mayaan.game.magic.GlyphType;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

/**
 * Sent by the server to sync a player's Glyph Knowledge state to their client.
 *
 * <p>The client stores the received values in {@link net.mayaan.client.ClientMayaanData} where
 * they are read by {@link net.mayaan.client.gui.contextualbar.AnimaBarHudRenderer} to show the
 * Glyph Knowledge score next to the Anima bar.
 *
 * <p>This packet is sent once after join and whenever the player's glyph mastery changes.
 *
 * <h2>Fields</h2>
 * <ul>
 *   <li>{@code knowledgeScore} — number of glyph types at {@link GlyphMastery#PRACTICED}+ (0–7)</li>
 *   <li>{@code masteryOrdinals} — per-type mastery tier ordinal for each {@link GlyphType}
 *       (one byte per type, in {@link GlyphType#values()} order)</li>
 * </ul>
 */
public final class ClientboundMayaanGlyphSyncPacket
        implements Packet<ClientGamePacketListener> {

    public static final StreamCodec<FriendlyByteBuf, ClientboundMayaanGlyphSyncPacket> STREAM_CODEC =
            Packet.codec(ClientboundMayaanGlyphSyncPacket::write,
                    ClientboundMayaanGlyphSyncPacket::new);

    private final int knowledgeScore;
    /** One entry per {@link GlyphType}, in {@link GlyphType#values()} ordinal order. */
    private final byte[] masteryOrdinals;

    public ClientboundMayaanGlyphSyncPacket(int knowledgeScore, byte[] masteryOrdinals) {
        this.knowledgeScore = knowledgeScore;
        this.masteryOrdinals = masteryOrdinals;
    }

    private ClientboundMayaanGlyphSyncPacket(FriendlyByteBuf buf) {
        this.knowledgeScore = buf.readVarInt();
        int count = buf.readVarInt();
        this.masteryOrdinals = new byte[count];
        for (int i = 0; i < count; i++) {
            this.masteryOrdinals[i] = buf.readByte();
        }
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeVarInt(this.knowledgeScore);
        buf.writeVarInt(this.masteryOrdinals.length);
        for (byte ordinal : this.masteryOrdinals) {
            buf.writeByte(ordinal);
        }
    }

    @Override
    public PacketType<ClientboundMayaanGlyphSyncPacket> type() {
        return GamePacketTypes.CLIENTBOUND_MAYAAN_GLYPH_SYNC;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleMayaanGlyphSync(this);
    }

    /** Returns the player's current Glyph Knowledge score (0–7). */
    public int getKnowledgeScore() {
        return knowledgeScore;
    }

    /**
     * Returns the mastery tier for the given glyph type.
     *
     * @param type the glyph type to look up
     * @return the current mastery level; {@link GlyphMastery#UNLEARNED} if type is out of range
     */
    public GlyphMastery getMastery(GlyphType type) {
        int ordinal = type.ordinal();
        if (ordinal < 0 || ordinal >= masteryOrdinals.length) {
            return GlyphMastery.UNLEARNED;
        }
        GlyphMastery[] tiers = GlyphMastery.values();
        int mastery = masteryOrdinals[ordinal] & 0xFF;
        if (mastery >= tiers.length) {
            return GlyphMastery.UNLEARNED;
        }
        return tiers[mastery];
    }
}
