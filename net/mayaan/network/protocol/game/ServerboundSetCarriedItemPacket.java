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

public class ServerboundSetCarriedItemPacket
implements Packet<ServerGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ServerboundSetCarriedItemPacket> STREAM_CODEC = Packet.codec(ServerboundSetCarriedItemPacket::write, ServerboundSetCarriedItemPacket::new);
    private final int slot;

    public ServerboundSetCarriedItemPacket(int slot) {
        this.slot = slot;
    }

    private ServerboundSetCarriedItemPacket(FriendlyByteBuf input) {
        this.slot = input.readShort();
    }

    private void write(FriendlyByteBuf output) {
        output.writeShort(this.slot);
    }

    @Override
    public PacketType<ServerboundSetCarriedItemPacket> type() {
        return GamePacketTypes.SERVERBOUND_SET_CARRIED_ITEM;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleSetCarriedItem(this);
    }

    public int getSlot() {
        return this.slot;
    }
}

