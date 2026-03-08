/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.login;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.login.ClientLoginPacketListener;
import net.mayaan.network.protocol.login.LoginPacketTypes;

public class ClientboundLoginCompressionPacket
implements Packet<ClientLoginPacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundLoginCompressionPacket> STREAM_CODEC = Packet.codec(ClientboundLoginCompressionPacket::write, ClientboundLoginCompressionPacket::new);
    private final int compressionThreshold;

    public ClientboundLoginCompressionPacket(int compressionThreshold) {
        this.compressionThreshold = compressionThreshold;
    }

    private ClientboundLoginCompressionPacket(FriendlyByteBuf input) {
        this.compressionThreshold = input.readVarInt();
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.compressionThreshold);
    }

    @Override
    public PacketType<ClientboundLoginCompressionPacket> type() {
        return LoginPacketTypes.CLIENTBOUND_LOGIN_COMPRESSION;
    }

    @Override
    public void handle(ClientLoginPacketListener listener) {
        listener.handleCompression(this);
    }

    public int getCompressionThreshold() {
        return this.compressionThreshold;
    }
}

