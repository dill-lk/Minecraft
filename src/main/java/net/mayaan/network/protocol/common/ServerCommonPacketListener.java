/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.common;

import net.mayaan.network.protocol.common.ServerboundClientInformationPacket;
import net.mayaan.network.protocol.common.ServerboundCustomClickActionPacket;
import net.mayaan.network.protocol.common.ServerboundCustomPayloadPacket;
import net.mayaan.network.protocol.common.ServerboundKeepAlivePacket;
import net.mayaan.network.protocol.common.ServerboundPongPacket;
import net.mayaan.network.protocol.common.ServerboundResourcePackPacket;
import net.mayaan.network.protocol.cookie.ServerCookiePacketListener;

public interface ServerCommonPacketListener
extends ServerCookiePacketListener {
    public void handleKeepAlive(ServerboundKeepAlivePacket var1);

    public void handlePong(ServerboundPongPacket var1);

    public void handleCustomPayload(ServerboundCustomPayloadPacket var1);

    public void handleResourcePackResponse(ServerboundResourcePackPacket var1);

    public void handleClientInformation(ServerboundClientInformationPacket var1);

    public void handleCustomClickAction(ServerboundCustomClickActionPacket var1);
}

