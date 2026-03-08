/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.login;

import java.util.UUID;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.login.LoginPacketTypes;
import net.mayaan.network.protocol.login.ServerLoginPacketListener;

public record ServerboundHelloPacket(String name, UUID profileId) implements Packet<ServerLoginPacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ServerboundHelloPacket> STREAM_CODEC = Packet.codec(ServerboundHelloPacket::write, ServerboundHelloPacket::new);

    private ServerboundHelloPacket(FriendlyByteBuf input) {
        this(input.readUtf(16), input.readUUID());
    }

    private void write(FriendlyByteBuf output) {
        output.writeUtf(this.name, 16);
        output.writeUUID(this.profileId);
    }

    @Override
    public PacketType<ServerboundHelloPacket> type() {
        return LoginPacketTypes.SERVERBOUND_HELLO;
    }

    @Override
    public void handle(ServerLoginPacketListener listener) {
        listener.handleHello(this);
    }
}

