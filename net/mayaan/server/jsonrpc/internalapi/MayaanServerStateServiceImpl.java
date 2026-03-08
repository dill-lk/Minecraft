/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.jsonrpc.internalapi;

import java.util.Collection;
import java.util.List;
import net.mayaan.network.chat.Component;
import net.mayaan.server.dedicated.DedicatedServer;
import net.mayaan.server.jsonrpc.JsonRpcLogger;
import net.mayaan.server.jsonrpc.internalapi.MayaanServerStateService;
import net.mayaan.server.jsonrpc.methods.ClientInfo;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.world.entity.player.Player;

public class MayaanServerStateServiceImpl
implements MayaanServerStateService {
    private final DedicatedServer server;
    private final JsonRpcLogger jsonrpcLogger;

    public MayaanServerStateServiceImpl(DedicatedServer server, JsonRpcLogger jsonrpcLogger) {
        this.server = server;
        this.jsonrpcLogger = jsonrpcLogger;
    }

    @Override
    public boolean isReady() {
        return this.server.isReady();
    }

    @Override
    public boolean saveEverything(boolean suppressLogs, boolean flush, boolean force, ClientInfo clientInfo) {
        this.jsonrpcLogger.log(clientInfo, "Save everything. SuppressLogs: {}, flush: {}, force: {}", suppressLogs, flush, force);
        return this.server.saveEverything(suppressLogs, flush, force);
    }

    @Override
    public void halt(boolean waitForShutdown, ClientInfo clientInfo) {
        this.jsonrpcLogger.log(clientInfo, "Halt server. WaitForShutdown: {}", waitForShutdown);
        this.server.halt(waitForShutdown);
    }

    @Override
    public void sendSystemMessage(Component message, ClientInfo clientInfo) {
        this.jsonrpcLogger.log(clientInfo, "Send system message: '{}'", message.getString());
        this.server.sendSystemMessage(message);
    }

    @Override
    public void sendSystemMessage(Component message, boolean overlay, Collection<ServerPlayer> players, ClientInfo clientInfo) {
        List<String> playerNames = players.stream().map(Player::getPlainTextName).toList();
        this.jsonrpcLogger.log(clientInfo, "Send system message to '{}' players (overlay: {}): '{}'", playerNames.size(), overlay, message.getString());
        for (ServerPlayer player : players) {
            if (overlay) {
                player.sendOverlayMessage(message);
                continue;
            }
            player.sendSystemMessage(message);
        }
    }

    @Override
    public void broadcastSystemMessage(Component message, boolean overlay, ClientInfo clientInfo) {
        this.jsonrpcLogger.log(clientInfo, "Broadcast system message (overlay: {}): '{}'", overlay, message.getString());
        for (ServerPlayer player : this.server.getPlayerList().getPlayers()) {
            if (overlay) {
                player.sendOverlayMessage(message);
                continue;
            }
            player.sendSystemMessage(message);
        }
    }
}

