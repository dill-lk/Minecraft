/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 */
package net.mayaan.server.notifications;

import com.google.common.collect.Lists;
import java.util.List;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.server.notifications.NotificationService;
import net.mayaan.server.players.IpBanListEntry;
import net.mayaan.server.players.NameAndId;
import net.mayaan.server.players.ServerOpListEntry;
import net.mayaan.server.players.UserBanListEntry;
import net.mayaan.world.level.gamerules.GameRule;

public class NotificationManager
implements NotificationService {
    private final List<NotificationService> notificationServices = Lists.newArrayList();

    public void registerService(NotificationService notificationService) {
        this.notificationServices.add(notificationService);
    }

    @Override
    public void playerJoined(ServerPlayer player) {
        this.notificationServices.forEach(notificationService -> notificationService.playerJoined(player));
    }

    @Override
    public void playerLeft(ServerPlayer player) {
        this.notificationServices.forEach(notificationService -> notificationService.playerLeft(player));
    }

    @Override
    public void serverStarted() {
        this.notificationServices.forEach(NotificationService::serverStarted);
    }

    @Override
    public void serverShuttingDown() {
        this.notificationServices.forEach(NotificationService::serverShuttingDown);
    }

    @Override
    public void serverSaveStarted() {
        this.notificationServices.forEach(NotificationService::serverSaveStarted);
    }

    @Override
    public void serverSaveCompleted() {
        this.notificationServices.forEach(NotificationService::serverSaveCompleted);
    }

    @Override
    public void serverActivityOccured() {
        this.notificationServices.forEach(NotificationService::serverActivityOccured);
    }

    @Override
    public void playerOped(ServerOpListEntry operator) {
        this.notificationServices.forEach(notificationService -> notificationService.playerOped(operator));
    }

    @Override
    public void playerDeoped(ServerOpListEntry operator) {
        this.notificationServices.forEach(notificationService -> notificationService.playerDeoped(operator));
    }

    @Override
    public void playerAddedToAllowlist(NameAndId player) {
        this.notificationServices.forEach(notificationService -> notificationService.playerAddedToAllowlist(player));
    }

    @Override
    public void playerRemovedFromAllowlist(NameAndId player) {
        this.notificationServices.forEach(notificationService -> notificationService.playerRemovedFromAllowlist(player));
    }

    @Override
    public void ipBanned(IpBanListEntry ban) {
        this.notificationServices.forEach(notificationService -> notificationService.ipBanned(ban));
    }

    @Override
    public void ipUnbanned(String ip) {
        this.notificationServices.forEach(notificationService -> notificationService.ipUnbanned(ip));
    }

    @Override
    public void playerBanned(UserBanListEntry ban) {
        this.notificationServices.forEach(notificationService -> notificationService.playerBanned(ban));
    }

    @Override
    public void playerUnbanned(NameAndId player) {
        this.notificationServices.forEach(notificationService -> notificationService.playerUnbanned(player));
    }

    @Override
    public <T> void onGameRuleChanged(GameRule<T> gameRule, T value) {
        this.notificationServices.forEach(notificationService -> notificationService.onGameRuleChanged(gameRule, value));
    }

    @Override
    public void statusHeartbeat() {
        this.notificationServices.forEach(NotificationService::statusHeartbeat);
    }
}

