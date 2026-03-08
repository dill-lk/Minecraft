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

public record ServerboundSelectBundleItemPacket(int slotId, int selectedItemIndex) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ServerboundSelectBundleItemPacket> STREAM_CODEC = Packet.codec(ServerboundSelectBundleItemPacket::write, ServerboundSelectBundleItemPacket::new);

    private ServerboundSelectBundleItemPacket(FriendlyByteBuf input) {
        this(input.readVarInt(), input.readVarInt());
        if (this.selectedItemIndex < 0 && this.selectedItemIndex != -1) {
            throw new IllegalArgumentException("Invalid selectedItemIndex: " + this.selectedItemIndex);
        }
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.slotId);
        output.writeVarInt(this.selectedItemIndex);
    }

    @Override
    public PacketType<ServerboundSelectBundleItemPacket> type() {
        return GamePacketTypes.SERVERBOUND_BUNDLE_ITEM_SELECTED;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleBundleItemSelectedPacket(this);
    }
}

