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
import net.mayaan.world.level.Level;
import net.mayaan.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

public class DebugEntrySoundMood
implements DebugScreenEntry {
    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Mayaan minecraft = Mayaan.getInstance();
        if (minecraft.player == null) {
            return;
        }
        displayer.addLine(minecraft.getSoundManager().getChannelDebugString() + String.format(Locale.ROOT, " (Mood %d%%)", Math.round(minecraft.player.getCurrentMood() * 100.0f)));
    }
}

