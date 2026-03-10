/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.jsonrpc.internalapi;

import java.util.Collection;
import net.mayaan.server.MayaanServer;
import net.mayaan.server.jsonrpc.JsonRpcLogger;
import net.mayaan.server.jsonrpc.internalapi.MayaanBanListService;
import net.mayaan.server.jsonrpc.methods.ClientInfo;
import net.mayaan.server.players.IpBanListEntry;
import net.mayaan.server.players.NameAndId;
import net.mayaan.server.players.UserBanListEntry;

public class MayaanBanListServiceImpl
implements MayaanBanListService {
    private final MayaanServer server;
    private final JsonRpcLogger jsonrpcLogger;

    public MayaanBanListServiceImpl(MayaanServer server, JsonRpcLogger jsonrpcLogger) {
        this.server = server;
        this.jsonrpcLogger = jsonrpcLogger;
    }

    @Override
    public void addUserBan(UserBanListEntry ban, ClientInfo clientInfo) {
        this.jsonrpcLogger.log(clientInfo, "Add player '{}' to banlist. Reason: '{}'", ban.getDisplayName(), ban.getReasonMessage().getString());
        this.server.getPlayerList().getBans().add(ban);
    }

    @Override
    public void removeUserBan(NameAndId nameAndId, ClientInfo clientInfo) {
        this.jsonrpcLogger.log(clientInfo, "Remove player '{}' from banlist", nameAndId);
        this.server.getPlayerList().getBans().remove(nameAndId);
    }

    @Override
    public void clearUserBans(ClientInfo clientInfo) {
        this.server.getPlayerList().getBans().clear();
    }

    @Override
    public Collection<UserBanListEntry> getUserBanEntries() {
        return this.server.getPlayerList().getBans().getEntries();
    }

    @Override
    public Collection<IpBanListEntry> getIpBanEntries() {
        return this.server.getPlayerList().getIpBans().getEntries();
    }

    @Override
    public void addIpBan(IpBanListEntry ipBanEntry, ClientInfo clientInfo) {
        this.jsonrpcLogger.log(clientInfo, "Add ip '{}' to ban list", ipBanEntry.getUser());
        this.server.getPlayerList().getIpBans().add(ipBanEntry);
    }

    @Override
    public void clearIpBans(ClientInfo clientInfo) {
        this.jsonrpcLogger.log(clientInfo, "Clear ip ban list", new Object[0]);
        this.server.getPlayerList().getIpBans().clear();
    }

    @Override
    public void removeIpBan(String ip, ClientInfo clientInfo) {
        this.jsonrpcLogger.log(clientInfo, "Remove ip '{}' from ban list", ip);
        this.server.getPlayerList().getIpBans().remove(ip);
    }
}

