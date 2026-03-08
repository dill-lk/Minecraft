/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import java.util.Locale;
import net.mayaan.client.CloudStatus;
import net.mayaan.client.Mayaan;
import net.mayaan.client.Options;
import net.mayaan.client.TextureFilteringMethod;
import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.client.gui.components.debug.DebugScreenEntry;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

public class DebugEntrySimplePerformanceImpactors
implements DebugScreenEntry {
    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Mayaan minecraft = Mayaan.getInstance();
        Options options = minecraft.options;
        Object[] objectArray = new Object[3];
        Object object = objectArray[0] = options.improvedTransparency().get() != false ? "improved-transparency " : "";
        objectArray[1] = options.cloudStatus().get() == CloudStatus.OFF ? "" : (options.cloudStatus().get() == CloudStatus.FAST ? "fast-clouds " : "fancy-clouds ");
        objectArray[2] = options.biomeBlendRadius().get();
        displayer.addLine(String.format(Locale.ROOT, "%s%sB: %d", objectArray));
        TextureFilteringMethod filteringMethod = options.textureFiltering().get();
        if (filteringMethod == TextureFilteringMethod.ANISOTROPIC) {
            displayer.addLine(String.format(Locale.ROOT, "Filtering: %s %dx", filteringMethod.caption().getString(), options.maxAnisotropyValue()));
        } else {
            displayer.addLine(String.format(Locale.ROOT, "Filtering: %s", filteringMethod.caption().getString()));
        }
    }

    @Override
    public boolean isAllowed(boolean reducedDebugInfo) {
        return true;
    }
}

