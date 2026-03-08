/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.jsonrpc.methods;

import net.mayaan.server.jsonrpc.internalapi.MayaanApi;
import net.mayaan.server.jsonrpc.methods.ClientInfo;
import net.mayaan.server.permissions.LevelBasedPermissionSet;
import net.mayaan.server.permissions.PermissionLevel;
import net.mayaan.world.Difficulty;
import net.mayaan.world.level.GameType;

public class ServerSettingsService {
    public static boolean autosave(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().isAutoSave();
    }

    public static boolean setAutosave(MayaanApi minecraftApi, boolean enabled, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setAutoSave(enabled, clientInfo);
    }

    public static Difficulty difficulty(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().getDifficulty();
    }

    public static Difficulty setDifficulty(MayaanApi minecraftApi, Difficulty difficulty, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setDifficulty(difficulty, clientInfo);
    }

    public static boolean enforceAllowlist(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().isEnforceWhitelist();
    }

    public static boolean setEnforceAllowlist(MayaanApi minecraftApi, boolean enforce, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setEnforceWhitelist(enforce, clientInfo);
    }

    public static boolean usingAllowlist(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().isUsingWhitelist();
    }

    public static boolean setUsingAllowlist(MayaanApi minecraftApi, boolean use, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setUsingWhitelist(use, clientInfo);
    }

    public static int maxPlayers(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().getMaxPlayers();
    }

    public static int setMaxPlayers(MayaanApi minecraftApi, int maxPlayers, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setMaxPlayers(maxPlayers, clientInfo);
    }

    public static int pauseWhenEmpty(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().getPauseWhenEmptySeconds();
    }

    public static int setPauseWhenEmpty(MayaanApi minecraftApi, int emptySeconds, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setPauseWhenEmptySeconds(emptySeconds, clientInfo);
    }

    public static int playerIdleTimeout(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().getPlayerIdleTimeout();
    }

    public static int setPlayerIdleTimeout(MayaanApi minecraftApi, int idleTime, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setPlayerIdleTimeout(idleTime, clientInfo);
    }

    public static boolean allowFlight(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().allowFlight();
    }

    public static boolean setAllowFlight(MayaanApi minecraftApi, boolean allow, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setAllowFlight(allow, clientInfo);
    }

    public static int spawnProtection(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().getSpawnProtectionRadius();
    }

    public static int setSpawnProtection(MayaanApi minecraftApi, int spawnProtection, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setSpawnProtectionRadius(spawnProtection, clientInfo);
    }

    public static String motd(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().getMotd();
    }

    public static String setMotd(MayaanApi minecraftApi, String motd, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setMotd(motd, clientInfo);
    }

    public static boolean forceGameMode(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().forceGameMode();
    }

    public static boolean setForceGameMode(MayaanApi minecraftApi, boolean force, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setForceGameMode(force, clientInfo);
    }

    public static GameType gameMode(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().getGameMode();
    }

    public static GameType setGameMode(MayaanApi minecraftApi, GameType gameMode, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setGameMode(gameMode, clientInfo);
    }

    public static int viewDistance(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().getViewDistance();
    }

    public static int setViewDistance(MayaanApi minecraftApi, int viewDistance, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setViewDistance(viewDistance, clientInfo);
    }

    public static int simulationDistance(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().getSimulationDistance();
    }

    public static int setSimulationDistance(MayaanApi minecraftApi, int simulationDistance, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setSimulationDistance(simulationDistance, clientInfo);
    }

    public static boolean acceptTransfers(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().acceptsTransfers();
    }

    public static boolean setAcceptTransfers(MayaanApi minecraftApi, boolean accept, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setAcceptsTransfers(accept, clientInfo);
    }

    public static int statusHeartbeatInterval(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().getStatusHeartbeatInterval();
    }

    public static int setStatusHeartbeatInterval(MayaanApi minecraftApi, int statusHeartbeatInterval, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setStatusHeartbeatInterval(statusHeartbeatInterval, clientInfo);
    }

    public static PermissionLevel operatorUserPermissionLevel(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().getOperatorUserPermissions().level();
    }

    public static PermissionLevel setOperatorUserPermissionLevel(MayaanApi minecraftApi, PermissionLevel level, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setOperatorUserPermissions(LevelBasedPermissionSet.forLevel(level), clientInfo).level();
    }

    public static boolean hidesOnlinePlayers(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().hidesOnlinePlayers();
    }

    public static boolean setHidesOnlinePlayers(MayaanApi minecraftApi, boolean hide, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setHidesOnlinePlayers(hide, clientInfo);
    }

    public static boolean repliesToStatus(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().repliesToStatus();
    }

    public static boolean setRepliesToStatus(MayaanApi minecraftApi, boolean enable, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setRepliesToStatus(enable, clientInfo);
    }

    public static int entityBroadcastRangePercentage(MayaanApi minecraftApi) {
        return minecraftApi.serverSettingsService().getEntityBroadcastRangePercentage();
    }

    public static int setEntityBroadcastRangePercentage(MayaanApi minecraftApi, int percentage, ClientInfo clientInfo) {
        return minecraftApi.serverSettingsService().setEntityBroadcastRangePercentage(percentage, clientInfo);
    }
}

