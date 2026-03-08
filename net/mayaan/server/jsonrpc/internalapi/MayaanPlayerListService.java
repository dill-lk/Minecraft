/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.server.jsonrpc.internalapi;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.mayaan.server.jsonrpc.methods.ClientInfo;
import net.mayaan.server.level.ServerPlayer;
import net.mayaan.server.players.NameAndId;
import net.mayaan.util.Util;
import org.jspecify.annotations.Nullable;

public interface MayaanPlayerListService {
    public List<ServerPlayer> getPlayers();

    public @Nullable ServerPlayer getPlayer(UUID var1);

    default public CompletableFuture<Optional<NameAndId>> getUser(Optional<UUID> id, Optional<String> name) {
        if (id.isPresent()) {
            Optional<NameAndId> nameAndId = this.getCachedUserById(id.get());
            if (nameAndId.isPresent()) {
                return CompletableFuture.completedFuture(nameAndId);
            }
            return CompletableFuture.supplyAsync(() -> this.fetchUserById((UUID)id.get()), Util.nonCriticalIoPool());
        }
        if (name.isPresent()) {
            return CompletableFuture.supplyAsync(() -> this.fetchUserByName((String)name.get()), Util.nonCriticalIoPool());
        }
        return CompletableFuture.completedFuture(Optional.empty());
    }

    public Optional<NameAndId> fetchUserByName(String var1);

    public Optional<NameAndId> fetchUserById(UUID var1);

    public Optional<NameAndId> getCachedUserById(UUID var1);

    public Optional<ServerPlayer> getPlayer(Optional<UUID> var1, Optional<String> var2);

    public List<ServerPlayer> getPlayersWithAddress(String var1);

    public @Nullable ServerPlayer getPlayerByName(String var1);

    public void remove(ServerPlayer var1, ClientInfo var2);
}

