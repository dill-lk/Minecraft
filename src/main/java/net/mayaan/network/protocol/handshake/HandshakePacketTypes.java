/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.handshake;

import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketFlow;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.handshake.ClientIntentionPacket;
import net.mayaan.network.protocol.handshake.ServerHandshakePacketListener;
import net.mayaan.resources.Identifier;

public class HandshakePacketTypes {
    public static final PacketType<ClientIntentionPacket> CLIENT_INTENTION = HandshakePacketTypes.createServerbound("intention");

    private static <T extends Packet<ServerHandshakePacketListener>> PacketType<T> createServerbound(String id) {
        return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(id));
    }
}

