/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.login;

import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketFlow;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.login.ClientLoginPacketListener;
import net.mayaan.network.protocol.login.ClientboundCustomQueryPacket;
import net.mayaan.network.protocol.login.ClientboundHelloPacket;
import net.mayaan.network.protocol.login.ClientboundLoginCompressionPacket;
import net.mayaan.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.mayaan.network.protocol.login.ClientboundLoginFinishedPacket;
import net.mayaan.network.protocol.login.ServerLoginPacketListener;
import net.mayaan.network.protocol.login.ServerboundCustomQueryAnswerPacket;
import net.mayaan.network.protocol.login.ServerboundHelloPacket;
import net.mayaan.network.protocol.login.ServerboundKeyPacket;
import net.mayaan.network.protocol.login.ServerboundLoginAcknowledgedPacket;
import net.mayaan.resources.Identifier;

public class LoginPacketTypes {
    public static final PacketType<ClientboundCustomQueryPacket> CLIENTBOUND_CUSTOM_QUERY = LoginPacketTypes.createClientbound("custom_query");
    public static final PacketType<ClientboundLoginFinishedPacket> CLIENTBOUND_LOGIN_FINISHED = LoginPacketTypes.createClientbound("login_finished");
    public static final PacketType<ClientboundHelloPacket> CLIENTBOUND_HELLO = LoginPacketTypes.createClientbound("hello");
    public static final PacketType<ClientboundLoginCompressionPacket> CLIENTBOUND_LOGIN_COMPRESSION = LoginPacketTypes.createClientbound("login_compression");
    public static final PacketType<ClientboundLoginDisconnectPacket> CLIENTBOUND_LOGIN_DISCONNECT = LoginPacketTypes.createClientbound("login_disconnect");
    public static final PacketType<ServerboundCustomQueryAnswerPacket> SERVERBOUND_CUSTOM_QUERY_ANSWER = LoginPacketTypes.createServerbound("custom_query_answer");
    public static final PacketType<ServerboundHelloPacket> SERVERBOUND_HELLO = LoginPacketTypes.createServerbound("hello");
    public static final PacketType<ServerboundKeyPacket> SERVERBOUND_KEY = LoginPacketTypes.createServerbound("key");
    public static final PacketType<ServerboundLoginAcknowledgedPacket> SERVERBOUND_LOGIN_ACKNOWLEDGED = LoginPacketTypes.createServerbound("login_acknowledged");

    private static <T extends Packet<ClientLoginPacketListener>> PacketType<T> createClientbound(String id) {
        return new PacketType(PacketFlow.CLIENTBOUND, Identifier.withDefaultNamespace(id));
    }

    private static <T extends Packet<ServerLoginPacketListener>> PacketType<T> createServerbound(String id) {
        return new PacketType(PacketFlow.SERVERBOUND, Identifier.withDefaultNamespace(id));
    }
}

