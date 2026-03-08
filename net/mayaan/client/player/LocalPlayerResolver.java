/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 */
package net.mayaan.client.player;

import com.mojang.authlib.GameProfile;
import java.util.Optional;
import java.util.UUID;
import net.mayaan.client.Mayaan;
import net.mayaan.client.multiplayer.ClientPacketListener;
import net.mayaan.client.multiplayer.PlayerInfo;
import net.mayaan.server.players.ProfileResolver;

public class LocalPlayerResolver
implements ProfileResolver {
    private final Mayaan minecraft;
    private final ProfileResolver parentResolver;

    public LocalPlayerResolver(Mayaan minecraft, ProfileResolver parentResolver) {
        this.minecraft = minecraft;
        this.parentResolver = parentResolver;
    }

    @Override
    public Optional<GameProfile> fetchByName(String name) {
        PlayerInfo playerInfo;
        ClientPacketListener connection = this.minecraft.getConnection();
        if (connection != null && (playerInfo = connection.getPlayerInfoIgnoreCase(name)) != null) {
            return Optional.of(playerInfo.getProfile());
        }
        return this.parentResolver.fetchByName(name);
    }

    @Override
    public Optional<GameProfile> fetchById(UUID id) {
        PlayerInfo playerInfo;
        ClientPacketListener connection = this.minecraft.getConnection();
        if (connection != null && (playerInfo = connection.getPlayerInfo(id)) != null) {
            return Optional.of(playerInfo.getProfile());
        }
        return this.parentResolver.fetchById(id);
    }
}

