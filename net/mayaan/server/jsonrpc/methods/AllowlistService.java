/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.jsonrpc.methods;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import net.mayaan.server.jsonrpc.api.PlayerDto;
import net.mayaan.server.jsonrpc.internalapi.MayaanApi;
import net.mayaan.server.jsonrpc.methods.ClientInfo;
import net.mayaan.server.players.NameAndId;
import net.mayaan.server.players.StoredUserEntry;
import net.mayaan.server.players.UserWhiteListEntry;
import net.mayaan.util.Util;

public class AllowlistService {
    public static List<PlayerDto> get(MayaanApi minecraftApi) {
        return minecraftApi.allowListService().getEntries().stream().filter(p -> p.getUser() != null).map(u -> PlayerDto.from((NameAndId)u.getUser())).toList();
    }

    public static List<PlayerDto> add(MayaanApi minecraftApi, List<PlayerDto> playerDtos, ClientInfo clientInfo) {
        List<CompletableFuture> fetch = playerDtos.stream().map(playerDto -> minecraftApi.playerListService().getUser(playerDto.id(), playerDto.name())).toList();
        for (Optional user : Util.sequence(fetch).join()) {
            user.ifPresent(nameAndId -> minecraftApi.allowListService().add(new UserWhiteListEntry((NameAndId)nameAndId), clientInfo));
        }
        return AllowlistService.get(minecraftApi);
    }

    public static List<PlayerDto> clear(MayaanApi minecraftApi, ClientInfo clientInfo) {
        minecraftApi.allowListService().clear(clientInfo);
        return AllowlistService.get(minecraftApi);
    }

    public static List<PlayerDto> remove(MayaanApi minecraftApi, List<PlayerDto> playerDtos, ClientInfo clientInfo) {
        List<CompletableFuture> fetch = playerDtos.stream().map(playerDto -> minecraftApi.playerListService().getUser(playerDto.id(), playerDto.name())).toList();
        for (Optional user : Util.sequence(fetch).join()) {
            user.ifPresent(nameAndId -> minecraftApi.allowListService().remove((NameAndId)nameAndId, clientInfo));
        }
        minecraftApi.allowListService().kickUnlistedPlayers(clientInfo);
        return AllowlistService.get(minecraftApi);
    }

    public static List<PlayerDto> set(MayaanApi minecraftApi, List<PlayerDto> playerDtos, ClientInfo clientInfo) {
        List<CompletableFuture> fetch = playerDtos.stream().map(playerDto -> minecraftApi.playerListService().getUser(playerDto.id(), playerDto.name())).toList();
        Set finalAllowList = Util.sequence(fetch).join().stream().flatMap(Optional::stream).collect(Collectors.toSet());
        Set currentAllowList = minecraftApi.allowListService().getEntries().stream().map(StoredUserEntry::getUser).collect(Collectors.toSet());
        currentAllowList.stream().filter(user -> !finalAllowList.contains(user)).forEach(user -> minecraftApi.allowListService().remove((NameAndId)user, clientInfo));
        finalAllowList.stream().filter(user -> !currentAllowList.contains(user)).forEach(user -> minecraftApi.allowListService().add(new UserWhiteListEntry((NameAndId)user), clientInfo));
        minecraftApi.allowListService().kickUnlistedPlayers(clientInfo);
        return AllowlistService.get(minecraftApi);
    }
}

