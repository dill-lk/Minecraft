/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.jsonrpc.internalapi;

import java.util.Collection;
import net.mayaan.server.jsonrpc.methods.ClientInfo;
import net.mayaan.server.players.NameAndId;
import net.mayaan.server.players.UserWhiteListEntry;

public interface MayaanAllowListService {
    public Collection<UserWhiteListEntry> getEntries();

    public boolean add(UserWhiteListEntry var1, ClientInfo var2);

    public void clear(ClientInfo var1);

    public void remove(NameAndId var1, ClientInfo var2);

    public void kickUnlistedPlayers(ClientInfo var1);
}

