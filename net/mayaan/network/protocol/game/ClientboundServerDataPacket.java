/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.game;

import io.netty.buffer.ByteBuf;
import java.util.Optional;
import net.mayaan.network.chat.Component;
import net.mayaan.network.chat.ComponentSerialization;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;

public record ClientboundServerDataPacket(Component motd, Optional<byte[]> iconBytes) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<ByteBuf, ClientboundServerDataPacket> STREAM_CODEC = StreamCodec.composite(ComponentSerialization.TRUSTED_CONTEXT_FREE_STREAM_CODEC, ClientboundServerDataPacket::motd, ByteBufCodecs.BYTE_ARRAY.apply(ByteBufCodecs::optional), ClientboundServerDataPacket::iconBytes, ClientboundServerDataPacket::new);

    @Override
    public PacketType<ClientboundServerDataPacket> type() {
        return GamePacketTypes.CLIENTBOUND_SERVER_DATA;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleServerData(this);
    }
}

