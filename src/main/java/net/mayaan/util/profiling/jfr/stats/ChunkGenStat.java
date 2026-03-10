/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.profiling.jfr.stats;

import java.time.Duration;
import jdk.jfr.consumer.RecordedEvent;
import net.mayaan.server.level.ColumnPos;
import net.mayaan.util.profiling.jfr.stats.TimedStat;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.chunk.status.ChunkStatus;

public record ChunkGenStat(Duration duration, ChunkPos chunkPos, ColumnPos worldPos, ChunkStatus status, String level) implements TimedStat
{
    public static ChunkGenStat from(RecordedEvent event) {
        return new ChunkGenStat(event.getDuration(), new ChunkPos(event.getInt("chunkPosX"), event.getInt("chunkPosX")), new ColumnPos(event.getInt("worldPosX"), event.getInt("worldPosZ")), ChunkStatus.byName(event.getString("status")), event.getString("level"));
    }
}

