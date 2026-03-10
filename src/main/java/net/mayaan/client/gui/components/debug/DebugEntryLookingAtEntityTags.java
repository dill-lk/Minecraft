/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import java.util.ArrayList;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.debug.DebugEntryLookingAt;
import net.mayaan.client.gui.components.debug.DebugEntryLookingAtEntity;
import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.client.gui.components.debug.DebugScreenEntry;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

public class DebugEntryLookingAtEntityTags
implements DebugScreenEntry {
    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Mayaan minecraft = Mayaan.getInstance();
        Entity entity = minecraft.crosshairPickEntity;
        ArrayList<String> result = new ArrayList<String>();
        if (entity != null) {
            DebugEntryLookingAt.addTagEntries(result, entity);
        }
        displayer.addToGroup(DebugEntryLookingAtEntity.GROUP, result);
    }
}

