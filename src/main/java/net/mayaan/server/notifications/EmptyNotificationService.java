/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.notifications;

import net.mayaan.server.level.ServerPlayer;
import net.mayaan.server.notifications.NotificationService;
import net.mayaan.server.players.IpBanListEntry;
import net.mayaan.server.players.NameAndId;
import net.mayaan.server.players.ServerOpListEntry;
import net.mayaan.server.players.UserBanListEntry;
import net.mayaan.world.level.gamerules.GameRule;

public class EmptyNotificationService
implements NotificationService {
    @Override
    public void playerJoined(ServerPlayer player) {
    }

    @Override
    public void playerLeft(ServerPlayer player) {
    }

    @Override
    public void serverStarted() {
    }

    @Override
    public void serverShuttingDown() {
    }

    @Override
    public void serverSaveStarted() {
    }

    @Override
    public void serverSaveCompleted() {
    }

    @Override
    public void serverActivityOccured() {
    }

    @Override
    public void playerOped(ServerOpListEntry operator) {
    }

    @Override
    public void playerDeoped(ServerOpListEntry operator) {
    }

    @Override
    public void playerAddedToAllowlist(NameAndId player) {
    }

    @Override
    public void playerRemovedFromAllowlist(NameAndId player) {
    }

    @Override
    public void ipBanned(IpBanListEntry ban) {
    }

    @Override
    public void ipUnbanned(String ip) {
    }

    @Override
    public void playerBanned(UserBanListEntry ban) {
    }

    @Override
    public void playerUnbanned(NameAndId player) {
    }

    @Override
    public <T> void onGameRuleChanged(GameRule<T> gameRule, T value) {
    }

    @Override
    public void statusHeartbeat() {
    }
}

