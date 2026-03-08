/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import net.mayaan.SharedConstants;
import net.mayaan.client.ClientBrandRetriever;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.client.gui.components.debug.DebugScreenEntry;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

class DebugEntryVersion
implements DebugScreenEntry {
    DebugEntryVersion() {
    }

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level level, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        displayer.addPriorityLine("Mayaan " + SharedConstants.getCurrentVersion().name() + " (" + Mayaan.getInstance().getLaunchedVersion() + "/" + ClientBrandRetriever.getClientModName() + ")");
    }

    @Override
    public boolean isAllowed(boolean reducedDebugInfo) {
        return true;
    }
}

