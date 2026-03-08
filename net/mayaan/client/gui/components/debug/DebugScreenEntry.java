/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import net.mayaan.client.gui.components.debug.DebugEntryCategory;
import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

public interface DebugScreenEntry {
    public void display(DebugScreenDisplayer var1, @Nullable Level var2, @Nullable LevelChunk var3, @Nullable LevelChunk var4);

    default public boolean isAllowed(boolean reducedDebugInfo) {
        return !reducedDebugInfo;
    }

    default public DebugEntryCategory category() {
        return DebugEntryCategory.SCREEN_TEXT;
    }
}

