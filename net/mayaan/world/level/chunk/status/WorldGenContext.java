/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.level.chunk.status;

import java.util.concurrent.Executor;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.server.level.ThreadedLevelLightEngine;
import net.mayaan.world.level.chunk.ChunkGenerator;
import net.mayaan.world.level.chunk.LevelChunk;
import net.mayaan.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public record WorldGenContext(ServerLevel level, ChunkGenerator generator, StructureTemplateManager structureManager, ThreadedLevelLightEngine lightEngine, Executor mainThreadExecutor, LevelChunk.UnsavedListener unsavedListener) {
}

