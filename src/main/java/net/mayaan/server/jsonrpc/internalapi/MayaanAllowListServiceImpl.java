/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.jsonrpc.internalapi;

import java.util.Collection;
import net.mayaan.server.dedicated.DedicatedServer;
import net.mayaan.server.jsonrpc.JsonRpcLogger;
import net.mayaan.server.jsonrpc.internalapi.MayaanAllowListService;
import net.mayaan.server.jsonrpc.methods.ClientInfo;
import net.mayaan.server.players.NameAndId;
import net.mayaan.server.players.UserWhiteListEntry;

public class MayaanAllowListServiceImpl
implements MayaanAllowListService {
    private final DedicatedServer server;
    private final JsonRpcLogger jsonrpcLogger;

    public MayaanAllowListServiceImpl(DedicatedServer server, JsonRpcLogger jsonrpcLogger) {
        this.server = server;
        this.jsonrpcLogger = jsonrpcLogger;
    }

    @Override
    public Collection<UserWhiteListEntry> getEntries() {
        return this.server.getPlayerList().getWhiteList().getEntries();
    }

    @Override
    public boolean add(UserWhiteListEntry infos, ClientInfo clientInfo) {
        this.jsonrpcLogger.log(clientInfo, "Add player '{}' to allowlist", infos.getUser());
        return this.server.getPlayerList().getWhiteList().add(infos);
    }

    @Override
    public void clear(ClientInfo clientInfo) {
        this.jsonrpcLogger.log(clientInfo, "Clear allowlist", new Object[0]);
        this.server.getPlayerList().getWhiteList().clear();
    }

    @Override
    public void remove(NameAndId nameAndId, ClientInfo clientInfo) {
        this.jsonrpcLogger.log(clientInfo, "Remove player '{}' from allowlist", nameAndId);
        this.server.getPlayerList().getWhiteList().remove(nameAndId);
    }

    @Override
    public void kickUnlistedPlayers(ClientInfo clientInfo) {
        this.jsonrpcLogger.log(clientInfo, "Kick unlisted players", new Object[0]);
        this.server.kickUnlistedPlayers();
    }
}

