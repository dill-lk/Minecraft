/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.entity.Entity;
import org.jspecify.annotations.Nullable;

public class ClientboundSetEntityLinkPacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundSetEntityLinkPacket> STREAM_CODEC = Packet.codec(ClientboundSetEntityLinkPacket::write, ClientboundSetEntityLinkPacket::new);
    private final int sourceId;
    private final int destId;

    public ClientboundSetEntityLinkPacket(Entity sourceEntity, @Nullable Entity destEntity) {
        this.sourceId = sourceEntity.getId();
        this.destId = destEntity != null ? destEntity.getId() : 0;
    }

    private ClientboundSetEntityLinkPacket(FriendlyByteBuf input) {
        this.sourceId = input.readInt();
        this.destId = input.readInt();
    }

    private void write(FriendlyByteBuf output) {
        output.writeInt(this.sourceId);
        output.writeInt(this.destId);
    }

    @Override
    public PacketType<ClientboundSetEntityLinkPacket> type() {
        return GamePacketTypes.CLIENTBOUND_SET_ENTITY_LINK;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleEntityLinkPacket(this);
    }

    public int getSourceId() {
        return this.sourceId;
    }

    public int getDestId() {
        return this.destId;
    }
}

