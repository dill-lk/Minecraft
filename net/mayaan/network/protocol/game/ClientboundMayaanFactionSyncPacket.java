package net.mayaan.network.protocol.game;

import java.util.EnumMap;
import java.util.Map;
import net.mayaan.game.faction.Faction;
import net.mayaan.game.faction.FactionStanding;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;

/**
 * Sent by the server to sync a player's faction standing map to their client.
 *
 * <p>The client stores the received standings in {@link net.mayaan.client.ClientMayaanData}
 * where they are read by HUD renderers and screens.
 *
 * <p>This packet is sent:
 * <ul>
 *   <li>Once immediately after the player joins.</li>
 *   <li>Whenever a faction standing changes via
 *       {@link net.mayaan.game.faction.FactionManager#adjustStanding}.</li>
 * </ul>
 *
 * <h2>Wire format</h2>
 * <ul>
 *   <li>4 × {@code (byte factionOrdinal, int points)} tuples — one per faction</li>
 * </ul>
 */
public final class ClientboundMayaanFactionSyncPacket
        implements Packet<ClientGamePacketListener> {

    public static final StreamCodec<FriendlyByteBuf, ClientboundMayaanFactionSyncPacket>
            STREAM_CODEC = Packet.codec(
                    ClientboundMayaanFactionSyncPacket::write,
                    ClientboundMayaanFactionSyncPacket::new);

    private final Map<Faction, Integer> points;

    public ClientboundMayaanFactionSyncPacket(Map<Faction, Integer> points) {
        this.points = new EnumMap<>(points);
    }

    private ClientboundMayaanFactionSyncPacket(FriendlyByteBuf buf) {
        int size = buf.readByte();
        Faction[] factions = Faction.values();
        points = new EnumMap<>(Faction.class);
        for (int i = 0; i < size; i++) {
            int ordinal = buf.readByte() & 0xFF;
            int pts = buf.readInt();
            if (ordinal < factions.length) {
                points.put(factions[ordinal], pts);
            }
        }
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeByte(points.size());
        for (Map.Entry<Faction, Integer> entry : points.entrySet()) {
            buf.writeByte(entry.getKey().ordinal());
            buf.writeInt(entry.getValue());
        }
    }

    @Override
    public PacketType<ClientboundMayaanFactionSyncPacket> type() {
        return GamePacketTypes.CLIENTBOUND_MAYAAN_FACTION_SYNC;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleMayaanFactionSync(this);
    }

    /**
     * Returns the raw point value for the given faction.
     * Returns 0 if the faction is not present in this packet.
     */
    public int getPoints(Faction faction) {
        return points.getOrDefault(faction, 0);
    }

    /** Returns the {@link FactionStanding} tier for the given faction. */
    public FactionStanding getStanding(Faction faction) {
        return FactionStanding.fromPoints(getPoints(faction));
    }
}
