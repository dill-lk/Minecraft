/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.jsonrpc.internalapi;

import java.util.Collection;
import java.util.Optional;
import net.mayaan.server.MayaanServer;
import net.mayaan.server.jsonrpc.JsonRpcLogger;
import net.mayaan.server.jsonrpc.internalapi.MayaanOperatorListService;
import net.mayaan.server.jsonrpc.methods.ClientInfo;
import net.mayaan.server.permissions.LevelBasedPermissionSet;
import net.mayaan.server.permissions.PermissionLevel;
import net.mayaan.server.players.NameAndId;
import net.mayaan.server.players.ServerOpListEntry;

public class MayaanOperatorListServiceImpl
implements MayaanOperatorListService {
    private final MayaanServer minecraftServer;
    private final JsonRpcLogger jsonrpcLogger;

    public MayaanOperatorListServiceImpl(MayaanServer minecraftServer, JsonRpcLogger jsonrpcLogger) {
        this.minecraftServer = minecraftServer;
        this.jsonrpcLogger = jsonrpcLogger;
    }

    @Override
    public Collection<ServerOpListEntry> getEntries() {
        return this.minecraftServer.getPlayerList().getOps().getEntries();
    }

    @Override
    public void op(NameAndId nameAndId, Optional<PermissionLevel> permissionLevel, Optional<Boolean> canBypassPlayerLimit, ClientInfo clientInfo) {
        this.jsonrpcLogger.log(clientInfo, "Op '{}'", nameAndId);
        this.minecraftServer.getPlayerList().op(nameAndId, permissionLevel.map(LevelBasedPermissionSet::forLevel), canBypassPlayerLimit);
    }

    @Override
    public void op(NameAndId nameAndId, ClientInfo clientInfo) {
        this.jsonrpcLogger.log(clientInfo, "Op '{}'", nameAndId);
        this.minecraftServer.getPlayerList().op(nameAndId);
    }

    @Override
    public void deop(NameAndId nameAndId, ClientInfo clientInfo) {
        this.jsonrpcLogger.log(clientInfo, "Deop '{}'", nameAndId);
        this.minecraftServer.getPlayerList().deop(nameAndId);
    }

    @Override
    public void clear(ClientInfo clientInfo) {
        this.jsonrpcLogger.log(clientInfo, "Clear operator list", new Object[0]);
        this.minecraftServer.getPlayerList().getOps().clear();
    }
}

