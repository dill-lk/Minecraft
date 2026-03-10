/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.profiling.jfr.stats;

import java.time.Duration;
import jdk.jfr.consumer.RecordedEvent;
import net.mayaan.util.profiling.jfr.stats.TimedStat;
import net.mayaan.world.level.ChunkPos;

public record StructureGenStat(Duration duration, ChunkPos chunkPos, String structureName, String level, boolean success) implements TimedStat
{
    public static StructureGenStat from(RecordedEvent event) {
        return new StructureGenStat(event.getDuration(), new ChunkPos(event.getInt("chunkPosX"), event.getInt("chunkPosX")), event.getString("structure"), event.getString("level"), event.getBoolean("success"));
    }
}

