/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.cookie;

import net.mayaan.network.protocol.cookie.ServerboundCookieResponsePacket;
import net.mayaan.network.protocol.game.ServerPacketListener;

public interface ServerCookiePacketListener
extends ServerPacketListener {
    public void handleCookieResponse(ServerboundCookieResponsePacket var1);
}

