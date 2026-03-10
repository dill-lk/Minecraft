/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.IntArrayList
 *  it.unimi.dsi.fastutil.ints.IntList
 */
package net.mayaan.network.protocol.game;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

public class ClientboundRemoveEntitiesPacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundRemoveEntitiesPacket> STREAM_CODEC = Packet.codec(ClientboundRemoveEntitiesPacket::write, ClientboundRemoveEntitiesPacket::new);
    private final IntList entityIds;

    public ClientboundRemoveEntitiesPacket(IntList ids) {
        this.entityIds = new IntArrayList(ids);
    }

    public ClientboundRemoveEntitiesPacket(int ... ids) {
        this.entityIds = new IntArrayList(ids);
    }

    private ClientboundRemoveEntitiesPacket(FriendlyByteBuf input) {
        this.entityIds = input.readIntIdList();
    }

    private void write(FriendlyByteBuf output) {
        output.writeIntIdList(this.entityIds);
    }

    @Override
    public PacketType<ClientboundRemoveEntitiesPacket> type() {
        return GamePacketTypes.CLIENTBOUND_REMOVE_ENTITIES;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleRemoveEntities(this);
    }

    public IntList getEntityIds() {
        return this.entityIds;
    }
}

