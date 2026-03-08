/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import java.util.Locale;
import net.mayaan.client.Mayaan;
import net.mayaan.client.Options;
import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.client.gui.components.debug.DebugScreenEntry;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

public class DebugEntryFps
implements DebugScreenEntry {
    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Mayaan minecraft = Mayaan.getInstance();
        int framerateLimit = minecraft.getFramerateLimitTracker().getFramerateLimit();
        Options options = minecraft.options;
        displayer.addPriorityLine(String.format(Locale.ROOT, "%d fps T: %s%s", minecraft.getFps(), framerateLimit == 260 ? "inf" : Integer.valueOf(framerateLimit), options.enableVsync().get() != false ? " vsync" : ""));
    }

    @Override
    public boolean isAllowed(boolean reducedDebugInfo) {
        return true;
    }
}

