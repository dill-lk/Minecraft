/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.network;

import net.mayaan.network.protocol.Packet;
import net.mayaan.server.level.ServerPlayer;

public interface ServerPlayerConnection {
    public ServerPlayer getPlayer();

    public void send(Packet<?> var1);
}

