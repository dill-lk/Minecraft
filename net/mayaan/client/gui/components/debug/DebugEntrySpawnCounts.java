/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2IntMap
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.client.gui.components.debug.DebugScreenEntry;
import net.mayaan.server.level.ServerChunkCache;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.MobCategory;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.NaturalSpawner;
import net.mayaan.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

public class DebugEntrySpawnCounts
implements DebugScreenEntry {
    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        ServerLevel serverLevel;
        Mayaan minecraft = Mayaan.getInstance();
        Entity entity = minecraft.getCameraEntity();
        ServerLevel serverLevel2 = serverLevel = serverOrClientLevel instanceof ServerLevel ? (ServerLevel)serverOrClientLevel : null;
        if (entity == null || serverLevel == null) {
            return;
        }
        ServerChunkCache chunkSource = serverLevel.getChunkSource();
        NaturalSpawner.SpawnState lastSpawnState = chunkSource.getLastSpawnState();
        if (lastSpawnState != null) {
            Object2IntMap<MobCategory> mobCategoryCounts = lastSpawnState.getMobCategoryCounts();
            int chunkCount = lastSpawnState.getSpawnableChunkCount();
            displayer.addLine("SC: " + chunkCount + ", " + Stream.of(MobCategory.values()).map(c -> Character.toUpperCase(c.getName().charAt(0)) + ": " + mobCategoryCounts.getInt(c)).collect(Collectors.joining(", ")));
        }
    }
}

