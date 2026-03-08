/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.ping;

import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketFlow;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.ping.ClientPongPacketListener;
import net.mayaan.network.protocol.ping.ClientboundPongResponsePacket;
import net.mayaan.network.protocol.ping.ServerPingPacketListener;
import net.mayaan.network.protocol.ping.ServerboundPingRequestPacket;
import net.mayaan.resources.Identifier;

public class PingPacketTypes {
    public static final PacketType<ClientboundPongResponsePacket> CLIENTBOUND_PONG_RESPONSE = PingPacketTypes.createClientbound("pong_response");
    public static final PacketType<ServerboundPingRequestPacket> SERVERBOUND_PING_REQUEST = PingPacketTypes.createServerbound("ping_request");

    private static <T extends Packet<ClientPongPacketListener>> PacketType<T> createClientbound(String id) {
        return new PacketType(PacketFlow.CLIENTBOUND, Identifier.withDefaultNamespace(id));
    }

    private static <T extends Packet<ServerPingPacketListener>> PacketType<T> createServerbound(String id) {
        return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(id));
    }
}

