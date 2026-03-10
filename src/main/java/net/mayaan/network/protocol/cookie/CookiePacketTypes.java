/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.cookie;

import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketFlow;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.cookie.ClientCookiePacketListener;
import net.mayaan.network.protocol.cookie.ClientboundCookieRequestPacket;
import net.mayaan.network.protocol.cookie.ServerCookiePacketListener;
import net.mayaan.network.protocol.cookie.ServerboundCookieResponsePacket;
import net.mayaan.resources.Identifier;

public class CookiePacketTypes {
    public static final PacketType<ClientboundCookieRequestPacket> CLIENTBOUND_COOKIE_REQUEST = CookiePacketTypes.createClientbound("cookie_request");
    public static final PacketType<ServerboundCookieResponsePacket> SERVERBOUND_COOKIE_RESPONSE = CookiePacketTypes.createServerbound("cookie_response");

    private static <T extends Packet<ClientCookiePacketListener>> PacketType<T> createClientbound(String id) {
        return new PacketType(PacketFlow.CLIENTBOUND, Identifier.withDefaultNamespace(id));
    }

    private static <T extends Packet<ServerCookiePacketListener>> PacketType<T> createServerbound(String id) {
        return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(id));
    }
}

