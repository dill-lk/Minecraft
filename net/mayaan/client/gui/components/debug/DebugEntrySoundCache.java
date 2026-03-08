/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import java.util.Locale;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.client.gui.components.debug.DebugScreenEntry;
import net.mayaan.client.sounds.SoundBufferLibrary;
import net.mayaan.util.Mth;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

public class DebugEntrySoundCache
implements DebugScreenEntry {
    @Override
    public boolean isAllowed(boolean reducedDebugInfo) {
        return true;
    }

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        SoundBufferLibrary.DebugOutput.Counter counter = new SoundBufferLibrary.DebugOutput.Counter();
        Mayaan.getInstance().getSoundManager().getSoundCacheDebugStats(counter);
        displayer.addLine(String.format(Locale.ROOT, "Sound cache: %d buffers, %d MiB", counter.totalCount(), DebugEntrySoundCache.bytesToMegabytes(counter.totalSize())));
    }

    private static long bytesToMegabytes(long used) {
        return Mth.ceilLong((double)used / 1024.0 / 1024.0);
    }
}

