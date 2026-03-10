/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import java.util.ArrayList;
import net.mayaan.ChatFormatting;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.client.gui.components.debug.DebugScreenEntry;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

public class DebugEntryLookingAtEntity
implements DebugScreenEntry {
    public static final Identifier GROUP = Identifier.withDefaultNamespace("looking_at_entity");

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Mayaan minecraft = Mayaan.getInstance();
        Entity entity = minecraft.crosshairPickEntity;
        ArrayList<String> result = new ArrayList<String>();
        if (entity != null) {
            result.add(String.valueOf(ChatFormatting.UNDERLINE) + "Targeted Entity");
            result.add(entity.typeHolder().getRegisteredName());
        }
        displayer.addToGroup(GROUP, result);
    }
}

