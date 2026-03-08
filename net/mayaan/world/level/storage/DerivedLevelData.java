/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.storage;

import net.mayaan.CrashReportCategory;
import net.mayaan.world.Difficulty;
import net.mayaan.world.level.GameType;
import net.mayaan.world.level.LevelHeightAccessor;
import net.mayaan.world.level.storage.LevelData;
import net.mayaan.world.level.storage.ServerLevelData;
import net.mayaan.world.level.storage.WorldData;

public class DerivedLevelData
implements ServerLevelData {
    private final WorldData worldData;
    private final ServerLevelData wrapped;

    public DerivedLevelData(WorldData worldData, ServerLevelData wrapped) {
        this.worldData = worldData;
        this.wrapped = wrapped;
    }

    @Override
    public LevelData.RespawnData getRespawnData() {
        return this.wrapped.getRespawnData();
    }

    @Override
    public long getGameTime() {
        return this.wrapped.getGameTime();
    }

    @Override
    public String getLevelName() {
        return this.worldData.getLevelName();
    }

    @Override
    public GameType getGameType() {
        return this.worldData.getGameType();
    }

    @Override
    public void setGameTime(long time) {
    }

    @Override
    public void setSpawn(LevelData.RespawnData respawnData) {
        this.wrapped.setSpawn(respawnData);
    }

    @Override
    public void setGameType(GameType gameType) {
    }

    @Override
    public boolean isHardcore() {
        return this.worldData.isHardcore();
    }

    @Override
    public boolean isAllowCommands() {
        return this.worldData.isAllowCommands();
    }

    @Override
    public boolean isInitialized() {
        return this.wrapped.isInitialized();
    }

    @Override
    public void setInitialized(boolean initialized) {
    }

    @Override
    public Difficulty getDifficulty() {
        return this.worldData.getDifficulty();
    }

    @Override
    public boolean isDifficultyLocked() {
        return this.worldData.isDifficultyLocked();
    }

    @Override
    public void fillCrashReportCategory(CrashReportCategory category, LevelHeightAccessor levelHeightAccessor) {
        category.setDetail("Derived", true);
        this.wrapped.fillCrashReportCategory(category, levelHeightAccessor);
    }
}

