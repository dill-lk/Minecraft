/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.protocol.cookie;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.common.ClientboundStoreCookiePacket;
import net.mayaan.network.protocol.cookie.CookiePacketTypes;
import net.mayaan.network.protocol.cookie.ServerCookiePacketListener;
import net.mayaan.resources.Identifier;
import org.jspecify.annotations.Nullable;

public record ServerboundCookieResponsePacket(Identifier key, byte @Nullable [] payload) implements Packet<ServerCookiePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ServerboundCookieResponsePacket> STREAM_CODEC = Packet.codec(ServerboundCookieResponsePacket::write, ServerboundCookieResponsePacket::new);

    private ServerboundCookieResponsePacket(FriendlyByteBuf input) {
        this(input.readIdentifier(), input.readNullable(ClientboundStoreCookiePacket.PAYLOAD_STREAM_CODEC));
    }

    private void write(FriendlyByteBuf output) {
        output.writeIdentifier(this.key);
        output.writeNullable(this.payload, ClientboundStoreCookiePacket.PAYLOAD_STREAM_CODEC);
    }

    @Override
    public PacketType<ServerboundCookieResponsePacket> type() {
        return CookiePacketTypes.SERVERBOUND_COOKIE_RESPONSE;
    }

    @Override
    public void handle(ServerCookiePacketListener listener) {
        listener.handleCookieResponse(this);
    }
}

