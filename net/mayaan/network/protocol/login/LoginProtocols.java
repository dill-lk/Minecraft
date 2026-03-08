/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.login;

import net.mayaan.network.ConnectionProtocol;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.ProtocolInfo;
import net.mayaan.network.protocol.ProtocolInfoBuilder;
import net.mayaan.network.protocol.SimpleUnboundProtocol;
import net.mayaan.network.protocol.cookie.ClientboundCookieRequestPacket;
import net.mayaan.network.protocol.cookie.CookiePacketTypes;
import net.mayaan.network.protocol.cookie.ServerboundCookieResponsePacket;
import net.mayaan.network.protocol.login.ClientLoginPacketListener;
import net.mayaan.network.protocol.login.ClientboundCustomQueryPacket;
import net.mayaan.network.protocol.login.ClientboundHelloPacket;
import net.mayaan.network.protocol.login.ClientboundLoginCompressionPacket;
import net.mayaan.network.protocol.login.ClientboundLoginDisconnectPacket;
import net.mayaan.network.protocol.login.ClientboundLoginFinishedPacket;
import net.mayaan.network.protocol.login.LoginPacketTypes;
import net.mayaan.network.protocol.login.ServerLoginPacketListener;
import net.mayaan.network.protocol.login.ServerboundCustomQueryAnswerPacket;
import net.mayaan.network.protocol.login.ServerboundHelloPacket;
import net.mayaan.network.protocol.login.ServerboundKeyPacket;
import net.mayaan.network.protocol.login.ServerboundLoginAcknowledgedPacket;

public class LoginProtocols {
    public static final SimpleUnboundProtocol<ServerLoginPacketListener, FriendlyByteBuf> SERVERBOUND_TEMPLATE = ProtocolInfoBuilder.serverboundProtocol(ConnectionProtocol.LOGIN, builder -> builder.addPacket(LoginPacketTypes.SERVERBOUND_HELLO, ServerboundHelloPacket.STREAM_CODEC).addPacket(LoginPacketTypes.SERVERBOUND_KEY, ServerboundKeyPacket.STREAM_CODEC).addPacket(LoginPacketTypes.SERVERBOUND_CUSTOM_QUERY_ANSWER, ServerboundCustomQueryAnswerPacket.STREAM_CODEC).addPacket(LoginPacketTypes.SERVERBOUND_LOGIN_ACKNOWLEDGED, ServerboundLoginAcknowledgedPacket.STREAM_CODEC).addPacket(CookiePacketTypes.SERVERBOUND_COOKIE_RESPONSE, ServerboundCookieResponsePacket.STREAM_CODEC));
    public static final ProtocolInfo<ServerLoginPacketListener> SERVERBOUND = SERVERBOUND_TEMPLATE.bind(FriendlyByteBuf::new);
    public static final SimpleUnboundProtocol<ClientLoginPacketListener, FriendlyByteBuf> CLIENTBOUND_TEMPLATE = ProtocolInfoBuilder.clientboundProtocol(ConnectionProtocol.LOGIN, builder -> builder.addPacket(LoginPacketTypes.CLIENTBOUND_LOGIN_DISCONNECT, ClientboundLoginDisconnectPacket.STREAM_CODEC).addPacket(LoginPacketTypes.CLIENTBOUND_HELLO, ClientboundHelloPacket.STREAM_CODEC).addPacket(LoginPacketTypes.CLIENTBOUND_LOGIN_FINISHED, ClientboundLoginFinishedPacket.STREAM_CODEC).addPacket(LoginPacketTypes.CLIENTBOUND_LOGIN_COMPRESSION, ClientboundLoginCompressionPacket.STREAM_CODEC).addPacket(LoginPacketTypes.CLIENTBOUND_CUSTOM_QUERY, ClientboundCustomQueryPacket.STREAM_CODEC).addPacket(CookiePacketTypes.CLIENTBOUND_COOKIE_REQUEST, ClientboundCookieRequestPacket.STREAM_CODEC));
    public static final ProtocolInfo<ClientLoginPacketListener> CLIENTBOUND = CLIENTBOUND_TEMPLATE.bind(FriendlyByteBuf::new);
}

