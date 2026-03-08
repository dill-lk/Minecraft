/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.jsonrpc;

import net.mayaan.core.Holder;
import net.mayaan.server.jsonrpc.ManagementServer;
import net.mayaan.server.jsonrpc.OutgoingRpcMethod;
import net.mayaan.server.jsonrpc.OutgoingRpcMethods;
import net.mayaan.server.jsonrpc.api.PlayerDto;
import net.mayaan.server.jsonrpc.internalapi.MayaanApi;
import net.mayaan.server.jsonrpc.methods.BanlistService;
import net.mayaan.server.jsonrpc.methods.GameRulesService;
import net.mayaan.server.jsonrpc.methods.IpBanlistService;
import net.mayaan.server.jsonrpc.methods.OperatorService;
import net.mayaan.server.jsonrpc.methods.ServerStateService;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.server.notifications.NotificationService;
import net.mayaan.server.players.IpBanListEntry;
import net.mayaan.server.players.NameAndId;
import net.mayaan.server.players.ServerOpListEntry;
import net.mayaan.server.players.UserBanListEntry;
import net.mayaan.world.level.gamerules.GameRule;

public class JsonRpcNotificationService
implements NotificationService {
    private final ManagementServer managementServer;
    private final MayaanApi minecraftApi;

    public JsonRpcNotificationService(MayaanApi minecraftApi, ManagementServer managementServer) {
        this.minecraftApi = minecraftApi;
        this.managementServer = managementServer;
    }

    @Override
    public void playerJoined(ServerPlayer player) {
        this.broadcastNotification(OutgoingRpcMethods.PLAYER_JOINED, PlayerDto.from(player));
    }

    @Override
    public void playerLeft(ServerPlayer player) {
        this.broadcastNotification(OutgoingRpcMethods.PLAYER_LEFT, PlayerDto.from(player));
    }

    @Override
    public void serverStarted() {
        this.broadcastNotification(OutgoingRpcMethods.SERVER_STARTED);
    }

    @Override
    public void serverShuttingDown() {
        this.broadcastNotification(OutgoingRpcMethods.SERVER_SHUTTING_DOWN);
    }

    @Override
    public void serverSaveStarted() {
        this.broadcastNotification(OutgoingRpcMethods.SERVER_SAVE_STARTED);
    }

    @Override
    public void serverSaveCompleted() {
        this.broadcastNotification(OutgoingRpcMethods.SERVER_SAVE_COMPLETED);
    }

    @Override
    public void serverActivityOccured() {
        this.broadcastNotification(OutgoingRpcMethods.SERVER_ACTIVITY_OCCURRED);
    }

    @Override
    public void playerOped(ServerOpListEntry operator) {
        this.broadcastNotification(OutgoingRpcMethods.PLAYER_OPED, OperatorService.OperatorDto.from(operator));
    }

    @Override
    public void playerDeoped(ServerOpListEntry operator) {
        this.broadcastNotification(OutgoingRpcMethods.PLAYER_DEOPED, OperatorService.OperatorDto.from(operator));
    }

    @Override
    public void playerAddedToAllowlist(NameAndId player) {
        this.broadcastNotification(OutgoingRpcMethods.PLAYER_ADDED_TO_ALLOWLIST, PlayerDto.from(player));
    }

    @Override
    public void playerRemovedFromAllowlist(NameAndId player) {
        this.broadcastNotification(OutgoingRpcMethods.PLAYER_REMOVED_FROM_ALLOWLIST, PlayerDto.from(player));
    }

    @Override
    public void ipBanned(IpBanListEntry ban) {
        this.broadcastNotification(OutgoingRpcMethods.IP_BANNED, IpBanlistService.IpBanDto.from(ban));
    }

    @Override
    public void ipUnbanned(String ip) {
        this.broadcastNotification(OutgoingRpcMethods.IP_UNBANNED, ip);
    }

    @Override
    public void playerBanned(UserBanListEntry ban) {
        this.broadcastNotification(OutgoingRpcMethods.PLAYER_BANNED, BanlistService.UserBanDto.from(ban));
    }

    @Override
    public void playerUnbanned(NameAndId player) {
        this.broadcastNotification(OutgoingRpcMethods.PLAYER_UNBANNED, PlayerDto.from(player));
    }

    @Override
    public <T> void onGameRuleChanged(GameRule<T> gameRule, T value) {
        this.broadcastNotification(OutgoingRpcMethods.GAMERULE_CHANGED, GameRulesService.getTypedRule(this.minecraftApi, gameRule, value));
    }

    @Override
    public void statusHeartbeat() {
        this.broadcastNotification(OutgoingRpcMethods.STATUS_HEARTBEAT, ServerStateService.status(this.minecraftApi));
    }

    private void broadcastNotification(Holder.Reference<? extends OutgoingRpcMethod<Void, ?>> method) {
        this.managementServer.forEachConnection(connection -> connection.sendNotification(method));
    }

    private <Params> void broadcastNotification(Holder.Reference<? extends OutgoingRpcMethod<Params, ?>> method, Params params) {
        this.managementServer.forEachConnection(connection -> connection.sendNotification(method, params));
    }
}

