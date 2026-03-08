/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.cookie;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.cookie.ClientCookiePacketListener;
import net.mayaan.network.protocol.cookie.CookiePacketTypes;
import net.mayaan.resources.Identifier;

public record ClientboundCookieRequestPacket(Identifier key) implements Packet<ClientCookiePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundCookieRequestPacket> STREAM_CODEC = Packet.codec(ClientboundCookieRequestPacket::write, ClientboundCookieRequestPacket::new);

    private ClientboundCookieRequestPacket(FriendlyByteBuf input) {
        this(input.readIdentifier());
    }

    private void write(FriendlyByteBuf output) {
        output.writeIdentifier(this.key);
    }

    @Override
    public PacketType<ClientboundCookieRequestPacket> type() {
        return CookiePacketTypes.CLIENTBOUND_COOKIE_REQUEST;
    }

    @Override
    public void handle(ClientCookiePacketListener listener) {
        listener.handleRequestCookie(this);
    }
}

