/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.util.profiling.jfr.event;

import jdk.jfr.EventType;
import jdk.jfr.Label;
import jdk.jfr.Name;
import net.mayaan.util.profiling.jfr.event.ChunkRegionIoEvent;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.chunk.storage.RegionFileVersion;
import net.mayaan.world.level.chunk.storage.RegionStorageInfo;

@Name(value="minecraft.ChunkRegionWrite")
@Label(value="Region File Write")
public class ChunkRegionWriteEvent
extends ChunkRegionIoEvent {
    public static final String EVENT_NAME = "minecraft.ChunkRegionWrite";
    public static final EventType TYPE = EventType.getEventType(ChunkRegionWriteEvent.class);

    public ChunkRegionWriteEvent(RegionStorageInfo info, ChunkPos chunkPos, RegionFileVersion version, int bytes) {
        super(info, chunkPos, version, bytes);
    }
}

