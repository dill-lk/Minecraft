/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.game;

import io.netty.buffer.ByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

public record ClientboundSetHeldSlotPacket(int slot) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<ByteBuf, ClientboundSetHeldSlotPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundSetHeldSlotPacket::slot, ClientboundSetHeldSlotPacket::new);

    @Override
    public PacketType<ClientboundSetHeldSlotPacket> type() {
        return GamePacketTypes.CLIENTBOUND_SET_HELD_SLOT;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleSetHeldSlot(this);
    }
}

