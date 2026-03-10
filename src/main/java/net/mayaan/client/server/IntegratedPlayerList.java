/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.server;

import java.net.SocketAddress;
import net.mayaan.client.server.IntegratedServer;
import net.mayaan.core.LayeredRegistryAccess;
import net.mayaan.network.chat.Component;
import net.mayaan.server.RegistryLayer;
import net.mayaan.server.players.NameAndId;
import net.mayaan.server.players.PlayerList;
import net.mayaan.world.level.storage.PlayerDataStorage;

public class IntegratedPlayerList
extends PlayerList {
    public IntegratedPlayerList(IntegratedServer server, LayeredRegistryAccess<RegistryLayer> registryHolder, PlayerDataStorage playerDataStorage) {
        super(server, registryHolder, playerDataStorage, server.notificationManager());
        this.setViewDistance(10);
    }

    @Override
    public Component canPlayerLogin(SocketAddress address, NameAndId nameAndId) {
        if (this.getServer().isSingleplayerOwner(nameAndId) && this.getPlayerByName(nameAndId.name()) != null) {
            return Component.translatable("multiplayer.disconnect.name_taken");
        }
        return super.canPlayerLogin(address, nameAndId);
    }

    @Override
    public IntegratedServer getServer() {
        return (IntegratedServer)super.getServer();
    }
}

