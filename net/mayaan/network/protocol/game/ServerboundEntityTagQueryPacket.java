/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;

public class ServerboundEntityTagQueryPacket
implements Packet<ServerGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ServerboundEntityTagQueryPacket> STREAM_CODEC = Packet.codec(ServerboundEntityTagQueryPacket::write, ServerboundEntityTagQueryPacket::new);
    private final int transactionId;
    private final int entityId;

    public ServerboundEntityTagQueryPacket(int transactionId, int entityId) {
        this.transactionId = transactionId;
        this.entityId = entityId;
    }

    private ServerboundEntityTagQueryPacket(FriendlyByteBuf input) {
        this.transactionId = input.readVarInt();
        this.entityId = input.readVarInt();
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.transactionId);
        output.writeVarInt(this.entityId);
    }

    @Override
    public PacketType<ServerboundEntityTagQueryPacket> type() {
        return GamePacketTypes.SERVERBOUND_ENTITY_TAG_QUERY;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleEntityTagQuery(this);
    }

    public int getTransactionId() {
        return this.transactionId;
    }

    public int getEntityId() {
        return this.entityId;
    }
}

