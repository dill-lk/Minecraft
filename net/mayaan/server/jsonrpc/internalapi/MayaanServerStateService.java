/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.jsonrpc.internalapi;

import java.util.Collection;
import net.mayaan.network.chat.Component;
import net.mayaan.server.jsonrpc.methods.ClientInfo;
import net.mayaan.server.level.ServerPlayer;

public interface MayaanServerStateService {
    public boolean isReady();

    public boolean saveEverything(boolean var1, boolean var2, boolean var3, ClientInfo var4);

    public void halt(boolean var1, ClientInfo var2);

    public void sendSystemMessage(Component var1, ClientInfo var2);

    public void sendSystemMessage(Component var1, boolean var2, Collection<ServerPlayer> var3, ClientInfo var4);

    public void broadcastSystemMessage(Component var1, boolean var2, ClientInfo var3);
}

