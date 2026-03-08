/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.client.gui.components.debug.DebugScreenEntry;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

public class DebugEntryChunkSourceStats
implements DebugScreenEntry {
    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Mayaan minecraft = Mayaan.getInstance();
        if (minecraft.level != null) {
            displayer.addLine(minecraft.level.gatherChunkSourceStats());
        }
        if (serverOrClientLevel != null && serverOrClientLevel != minecraft.level) {
            displayer.addLine(serverOrClientLevel.gatherChunkSourceStats());
        }
    }

    @Override
    public boolean isAllowed(boolean reducedDebugInfo) {
        return true;
    }
}

