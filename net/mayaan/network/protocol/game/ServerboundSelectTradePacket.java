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

public class ServerboundSelectTradePacket
implements Packet<ServerGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ServerboundSelectTradePacket> STREAM_CODEC = Packet.codec(ServerboundSelectTradePacket::write, ServerboundSelectTradePacket::new);
    private final int item;

    public ServerboundSelectTradePacket(int item) {
        this.item = item;
    }

    private ServerboundSelectTradePacket(FriendlyByteBuf input) {
        this.item = input.readVarInt();
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.item);
    }

    @Override
    public PacketType<ServerboundSelectTradePacket> type() {
        return GamePacketTypes.SERVERBOUND_SELECT_TRADE;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleSelectTrade(this);
    }

    public int getItem() {
        return this.item;
    }
}

