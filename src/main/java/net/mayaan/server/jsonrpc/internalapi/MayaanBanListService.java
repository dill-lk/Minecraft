/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.jsonrpc.internalapi;

import java.util.Collection;
import net.mayaan.server.jsonrpc.methods.ClientInfo;
import net.mayaan.server.players.IpBanListEntry;
import net.mayaan.server.players.NameAndId;
import net.mayaan.server.players.UserBanListEntry;

public interface MayaanBanListService {
    public void addUserBan(UserBanListEntry var1, ClientInfo var2);

    public void removeUserBan(NameAndId var1, ClientInfo var2);

    public Collection<UserBanListEntry> getUserBanEntries();

    public Collection<IpBanListEntry> getIpBanEntries();

    public void addIpBan(IpBanListEntry var1, ClientInfo var2);

    public void clearIpBans(ClientInfo var1);

    public void removeIpBan(String var1, ClientInfo var2);

    public void clearUserBans(ClientInfo var1);
}

