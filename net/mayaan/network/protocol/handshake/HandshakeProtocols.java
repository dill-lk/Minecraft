/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.handshake;

import net.mayaan.network.ConnectionProtocol;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.ProtocolInfo;
import net.mayaan.network.protocol.ProtocolInfoBuilder;
import net.mayaan.network.protocol.SimpleUnboundProtocol;
import net.mayaan.network.protocol.handshake.ClientIntentionPacket;
import net.mayaan.network.protocol.handshake.HandshakePacketTypes;
import net.mayaan.network.protocol.handshake.ServerHandshakePacketListener;

public class HandshakeProtocols {
    public static final SimpleUnboundProtocol<ServerHandshakePacketListener, FriendlyByteBuf> SERVERBOUND_TEMPLATE = ProtocolInfoBuilder.serverboundProtocol(ConnectionProtocol.HANDSHAKING, builder -> builder.addPacket(HandshakePacketTypes.CLIENT_INTENTION, ClientIntentionPacket.STREAM_CODEC));
    public static final ProtocolInfo<ServerHandshakePacketListener> SERVERBOUND = SERVERBOUND_TEMPLATE.bind(FriendlyByteBuf::new);
}

