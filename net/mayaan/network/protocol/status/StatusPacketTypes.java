/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.status;

import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketFlow;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.status.ClientStatusPacketListener;
import net.mayaan.network.protocol.status.ClientboundStatusResponsePacket;
import net.mayaan.network.protocol.status.ServerStatusPacketListener;
import net.mayaan.network.protocol.status.ServerboundStatusRequestPacket;
import net.mayaan.resources.Identifier;

public class StatusPacketTypes {
    public static final PacketType<ClientboundStatusResponsePacket> CLIENTBOUND_STATUS_RESPONSE = StatusPacketTypes.createClientbound("status_response");
    public static final PacketType<ServerboundStatusRequestPacket> SERVERBOUND_STATUS_REQUEST = StatusPacketTypes.createServerbound("status_request");

    private static <T extends Packet<ClientStatusPacketListener>> PacketType<T> createClientbound(String id) {
        return new PacketType(PacketFlow.CLIENTBOUND, Identifier.withDefaultNamespace(id));
    }

    private static <T extends Packet<ServerStatusPacketListener>> PacketType<T> createServerbound(String id) {
        return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(id));
    }
}

