/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.longs.LongSets
 *  it.unimi.dsi.fastutil.longs.LongSets$EmptySet
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import it.unimi.dsi.fastutil.longs.LongSets;
import java.util.List;
import java.util.Locale;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.client.gui.components.debug.DebugScreenEntry;
import net.mayaan.core.BlockPos;
import net.mayaan.core.Direction;
import net.mayaan.core.SectionPos;
import net.mayaan.resources.Identifier;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.Mth;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

public class DebugEntryPosition
implements DebugScreenEntry {
    public static final Identifier GROUP = Identifier.withDefaultNamespace("position");

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Mayaan minecraft = Mayaan.getInstance();
        Entity entity = minecraft.getCameraEntity();
        if (entity == null) {
            return;
        }
        BlockPos feetPos = minecraft.getCameraEntity().blockPosition();
        ChunkPos chunkPos = ChunkPos.containing(feetPos);
        Direction direction = entity.getDirection();
        String faceString = switch (direction) {
            case Direction.NORTH -> "Towards negative Z";
            case Direction.SOUTH -> "Towards positive Z";
            case Direction.WEST -> "Towards negative X";
            case Direction.EAST -> "Towards positive X";
            default -> "Invalid";
        };
        LongSets.EmptySet chunks = serverOrClientLevel instanceof ServerLevel ? ((ServerLevel)serverOrClientLevel).getForceLoadedChunks() : LongSets.EMPTY_SET;
        displayer.addToGroup(GROUP, List.of(String.format(Locale.ROOT, "XYZ: %.3f / %.5f / %.3f", minecraft.getCameraEntity().getX(), minecraft.getCameraEntity().getY(), minecraft.getCameraEntity().getZ()), String.format(Locale.ROOT, "Block: %d %d %d", feetPos.getX(), feetPos.getY(), feetPos.getZ()), String.format(Locale.ROOT, "Chunk: %d %d %d [%d %d in r.%d.%d.mca]", chunkPos.x(), SectionPos.blockToSectionCoord(feetPos.getY()), chunkPos.z(), chunkPos.getRegionLocalX(), chunkPos.getRegionLocalZ(), chunkPos.getRegionX(), chunkPos.getRegionZ()), String.format(Locale.ROOT, "Facing: %s (%s) (%.1f / %.1f)", direction, faceString, Float.valueOf(Mth.wrapDegrees(entity.getYRot())), Float.valueOf(Mth.wrapDegrees(entity.getXRot()))), String.valueOf(minecraft.level.dimension().identifier()) + " FC: " + chunks.size()));
    }
}

