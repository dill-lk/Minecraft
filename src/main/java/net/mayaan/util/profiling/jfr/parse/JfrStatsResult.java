/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.util.Pair
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.util.profiling.jfr.parse;

import com.mojang.datafixers.util.Pair;
import java.time.Duration;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import net.mayaan.util.profiling.jfr.serialize.JfrResultJsonSerializer;
import net.mayaan.util.profiling.jfr.stats.ChunkGenStat;
import net.mayaan.util.profiling.jfr.stats.ChunkIdentification;
import net.mayaan.util.profiling.jfr.stats.CpuLoadStat;
import net.mayaan.util.profiling.jfr.stats.FileIOStat;
import net.mayaan.util.profiling.jfr.stats.FpsStat;
import net.mayaan.util.profiling.jfr.stats.GcHeapStat;
import net.mayaan.util.profiling.jfr.stats.IoSummary;
import net.mayaan.util.profiling.jfr.stats.PacketIdentification;
import net.mayaan.util.profiling.jfr.stats.StructureGenStat;
import net.mayaan.util.profiling.jfr.stats.ThreadAllocationStat;
import net.mayaan.util.profiling.jfr.stats.TickTimeStat;
import net.mayaan.util.profiling.jfr.stats.TimedStatSummary;
import net.mayaan.world.level.chunk.status.ChunkStatus;
import org.jspecify.annotations.Nullable;

public record JfrStatsResult(Instant recordingStarted, Instant recordingEnded, Duration recordingDuration, @Nullable Duration worldCreationDuration, List<FpsStat> fps, List<TickTimeStat> serverTickTimes, List<CpuLoadStat> cpuLoadStats, GcHeapStat.Summary heapSummary, ThreadAllocationStat.Summary threadAllocationSummary, IoSummary<PacketIdentification> receivedPacketsSummary, IoSummary<PacketIdentification> sentPacketsSummary, IoSummary<ChunkIdentification> writtenChunks, IoSummary<ChunkIdentification> readChunks, FileIOStat.Summary fileWrites, FileIOStat.Summary fileReads, List<ChunkGenStat> chunkGenStats, List<StructureGenStat> structureGenStats) {
    public List<Pair<ChunkStatus, TimedStatSummary<ChunkGenStat>>> chunkGenSummary() {
        Map<ChunkStatus, List<ChunkGenStat>> byStatus = this.chunkGenStats.stream().collect(Collectors.groupingBy(ChunkGenStat::status));
        return byStatus.entrySet().stream().map(e -> Pair.of((Object)((ChunkStatus)e.getKey()), TimedStatSummary.summary((List)e.getValue()))).filter(pair -> ((Optional)pair.getSecond()).isPresent()).map(e -> Pair.of((Object)((ChunkStatus)e.getFirst()), (Object)((TimedStatSummary)((Optional)e.getSecond()).get()))).sorted(Comparator.comparing(pair -> ((TimedStatSummary)pair.getSecond()).totalDuration()).reversed()).toList();
    }

    public String asJson() {
        return new JfrResultJsonSerializer().format(this);
    }
}

