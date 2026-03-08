/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.notifications;

import net.mayaan.server.level.ServerPlayer;
import net.mayaan.server.players.IpBanListEntry;
import net.mayaan.server.players.NameAndId;
import net.mayaan.server.players.ServerOpListEntry;
import net.mayaan.server.players.UserBanListEntry;
import net.mayaan.world.level.gamerules.GameRule;

public interface NotificationService {
    public void playerJoined(ServerPlayer var1);

    public void playerLeft(ServerPlayer var1);

    public void serverStarted();

    public void serverShuttingDown();

    public void serverSaveStarted();

    public void serverSaveCompleted();

    public void serverActivityOccured();

    public void playerOped(ServerOpListEntry var1);

    public void playerDeoped(ServerOpListEntry var1);

    public void playerAddedToAllowlist(NameAndId var1);

    public void playerRemovedFromAllowlist(NameAndId var1);

    public void ipBanned(IpBanListEntry var1);

    public void ipUnbanned(String var1);

    public void playerBanned(UserBanListEntry var1);

    public void playerUnbanned(NameAndId var1);

    public <T> void onGameRuleChanged(GameRule<T> var1, T var2);

    public void statusHeartbeat();
}

