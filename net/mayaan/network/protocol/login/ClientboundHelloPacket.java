/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.login;

import java.security.PublicKey;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.login.ClientLoginPacketListener;
import net.mayaan.network.protocol.login.LoginPacketTypes;
import net.mayaan.util.Crypt;
import net.mayaan.util.CryptException;

public class ClientboundHelloPacket
implements Packet<ClientLoginPacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundHelloPacket> STREAM_CODEC = Packet.codec(ClientboundHelloPacket::write, ClientboundHelloPacket::new);
    private final String serverId;
    private final byte[] publicKey;
    private final byte[] challenge;
    private final boolean shouldAuthenticate;

    public ClientboundHelloPacket(String serverId, byte[] publicKey, byte[] challenge, boolean shouldAuthenticate) {
        this.serverId = serverId;
        this.publicKey = publicKey;
        this.challenge = challenge;
        this.shouldAuthenticate = shouldAuthenticate;
    }

    private ClientboundHelloPacket(FriendlyByteBuf input) {
        this.serverId = input.readUtf(20);
        this.publicKey = input.readByteArray();
        this.challenge = input.readByteArray();
        this.shouldAuthenticate = input.readBoolean();
    }

    private void write(FriendlyByteBuf output) {
        output.writeUtf(this.serverId);
        output.writeByteArray(this.publicKey);
        output.writeByteArray(this.challenge);
        output.writeBoolean(this.shouldAuthenticate);
    }

    @Override
    public PacketType<ClientboundHelloPacket> type() {
        return LoginPacketTypes.CLIENTBOUND_HELLO;
    }

    @Override
    public void handle(ClientLoginPacketListener listener) {
        listener.handleHello(this);
    }

    public String getServerId() {
        return this.serverId;
    }

    public PublicKey getPublicKey() throws CryptException {
        return Crypt.byteToPublicKey(this.publicKey);
    }

    public byte[] getChallenge() {
        return this.challenge;
    }

    public boolean shouldAuthenticate() {
        return this.shouldAuthenticate;
    }
}

