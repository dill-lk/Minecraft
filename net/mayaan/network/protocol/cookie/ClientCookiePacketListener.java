/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.cookie;

import net.mayaan.network.ClientboundPacketListener;
import net.mayaan.network.protocol.cookie.ClientboundCookieRequestPacket;

public interface ClientCookiePacketListener
extends ClientboundPacketListener {
    public void handleRequestCookie(ClientboundCookieRequestPacket var1);
}

