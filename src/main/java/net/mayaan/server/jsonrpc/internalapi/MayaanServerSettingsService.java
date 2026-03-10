/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.server.jsonrpc.internalapi;

import net.mayaan.server.jsonrpc.methods.ClientInfo;
import net.mayaan.server.permissions.LevelBasedPermissionSet;
import net.mayaan.world.Difficulty;
import net.mayaan.world.level.GameType;

public interface MayaanServerSettingsService {
    public boolean isAutoSave();

    public boolean setAutoSave(boolean var1, ClientInfo var2);

    public Difficulty getDifficulty();

    public Difficulty setDifficulty(Difficulty var1, ClientInfo var2);

    public boolean isEnforceWhitelist();

    public boolean setEnforceWhitelist(boolean var1, ClientInfo var2);

    public boolean isUsingWhitelist();

    public boolean setUsingWhitelist(boolean var1, ClientInfo var2);

    public int getMaxPlayers();

    public int setMaxPlayers(int var1, ClientInfo var2);

    public int getPauseWhenEmptySeconds();

    public int setPauseWhenEmptySeconds(int var1, ClientInfo var2);

    public int getPlayerIdleTimeout();

    public int setPlayerIdleTimeout(int var1, ClientInfo var2);

    public boolean allowFlight();

    public boolean setAllowFlight(boolean var1, ClientInfo var2);

    public int getSpawnProtectionRadius();

    public int setSpawnProtectionRadius(int var1, ClientInfo var2);

    public String getMotd();

    public String setMotd(String var1, ClientInfo var2);

    public boolean forceGameMode();

    public boolean setForceGameMode(boolean var1, ClientInfo var2);

    public GameType getGameMode();

    public GameType setGameMode(GameType var1, ClientInfo var2);

    public int getViewDistance();

    public int setViewDistance(int var1, ClientInfo var2);

    public int getSimulationDistance();

    public int setSimulationDistance(int var1, ClientInfo var2);

    public boolean acceptsTransfers();

    public boolean setAcceptsTransfers(boolean var1, ClientInfo var2);

    public int getStatusHeartbeatInterval();

    public int setStatusHeartbeatInterval(int var1, ClientInfo var2);

    public LevelBasedPermissionSet getOperatorUserPermissions();

    public LevelBasedPermissionSet setOperatorUserPermissions(LevelBasedPermissionSet var1, ClientInfo var2);

    public boolean hidesOnlinePlayers();

    public boolean setHidesOnlinePlayers(boolean var1, ClientInfo var2);

    public boolean repliesToStatus();

    public boolean setRepliesToStatus(boolean var1, ClientInfo var2);

    public int getEntityBroadcastRangePercentage();

    public int setEntityBroadcastRangePercentage(int var1, ClientInfo var2);
}

