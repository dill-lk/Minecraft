/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.status;

import io.netty.buffer.ByteBuf;
import net.mayaan.network.ConnectionProtocol;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.ProtocolInfo;
import net.mayaan.network.protocol.ProtocolInfoBuilder;
import net.mayaan.network.protocol.SimpleUnboundProtocol;
import net.mayaan.network.protocol.ping.ClientboundPongResponsePacket;
import net.mayaan.network.protocol.ping.PingPacketTypes;
import net.mayaan.network.protocol.ping.ServerboundPingRequestPacket;
import net.mayaan.network.protocol.status.ClientStatusPacketListener;
import net.mayaan.network.protocol.status.ClientboundStatusResponsePacket;
import net.mayaan.network.protocol.status.ServerStatusPacketListener;
import net.mayaan.network.protocol.status.ServerboundStatusRequestPacket;
import net.mayaan.network.protocol.status.StatusPacketTypes;

public class StatusProtocols {
    public static final SimpleUnboundProtocol<ServerStatusPacketListener, ByteBuf> SERVERBOUND_TEMPLATE = ProtocolInfoBuilder.serverboundProtocol(ConnectionProtocol.STATUS, builder -> builder.addPacket(StatusPacketTypes.SERVERBOUND_STATUS_REQUEST, ServerboundStatusRequestPacket.STREAM_CODEC).addPacket(PingPacketTypes.SERVERBOUND_PING_REQUEST, ServerboundPingRequestPacket.STREAM_CODEC));
    public static final ProtocolInfo<ServerStatusPacketListener> SERVERBOUND = SERVERBOUND_TEMPLATE.bind(e -> e);
    public static final SimpleUnboundProtocol<ClientStatusPacketListener, FriendlyByteBuf> CLIENTBOUND_TEMPLATE = ProtocolInfoBuilder.clientboundProtocol(ConnectionProtocol.STATUS, builder -> builder.addPacket(StatusPacketTypes.CLIENTBOUND_STATUS_RESPONSE, ClientboundStatusResponsePacket.STREAM_CODEC).addPacket(PingPacketTypes.CLIENTBOUND_PONG_RESPONSE, ClientboundPongResponsePacket.STREAM_CODEC));
    public static final ProtocolInfo<ClientStatusPacketListener> CLIENTBOUND = CLIENTBOUND_TEMPLATE.bind(FriendlyByteBuf::new);
}

