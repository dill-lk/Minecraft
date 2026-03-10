/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.client.gui.components.debug.DebugScreenEntry;
import net.mayaan.world.clock.ClockManager;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.chunk.LevelChunk;
import net.mayaan.world.timeline.Timeline;
import net.mayaan.world.timeline.Timelines;
import org.jspecify.annotations.Nullable;

public class DebugEntryDayCount
implements DebugScreenEntry {
    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        if (serverOrClientLevel != null) {
            ClockManager clockManager = serverOrClientLevel.clockManager();
            serverOrClientLevel.registryAccess().get(Timelines.OVERWORLD_DAY).ifPresent(timeline -> displayer.addLine("Day #" + ((Timeline)timeline.value()).getPeriodCount(clockManager)));
        }
    }
}

