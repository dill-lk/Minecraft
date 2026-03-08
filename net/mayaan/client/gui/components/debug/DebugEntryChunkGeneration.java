/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import java.util.ArrayList;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.client.gui.components.debug.DebugScreenEntry;
import net.mayaan.core.BlockPos;
import net.mayaan.resources.Identifier;
import net.mayaan.server.level.ServerChunkCache;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.biome.BiomeSource;
import net.mayaan.world.level.biome.Climate;
import net.mayaan.world.level.chunk.ChunkGenerator;
import net.mayaan.world.level.chunk.LevelChunk;
import net.mayaan.world.level.levelgen.RandomState;
import org.jspecify.annotations.Nullable;

public class DebugEntryChunkGeneration
implements DebugScreenEntry {
    private static final Identifier GROUP = Identifier.withDefaultNamespace("chunk_generation");

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        ServerLevel serverLevel;
        Mayaan minecraft = Mayaan.getInstance();
        Entity entity = minecraft.getCameraEntity();
        ServerLevel serverLevel2 = serverLevel = serverOrClientLevel instanceof ServerLevel ? (ServerLevel)serverOrClientLevel : null;
        if (entity == null || serverLevel == null) {
            return;
        }
        BlockPos feetPos = entity.blockPosition();
        ServerChunkCache chunkSource = serverLevel.getChunkSource();
        ArrayList<String> result = new ArrayList<String>();
        ChunkGenerator generator = chunkSource.getGenerator();
        RandomState randomState = chunkSource.randomState();
        generator.addDebugScreenInfo(result, randomState, feetPos);
        Climate.Sampler sampler = randomState.sampler();
        BiomeSource biomeSource = generator.getBiomeSource();
        biomeSource.addDebugInfo(result, feetPos, sampler);
        if (serverChunk != null && serverChunk.isOldNoiseGeneration()) {
            result.add("Blending: Old");
        }
        displayer.addToGroup(GROUP, result);
    }
}

