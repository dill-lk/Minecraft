/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import com.maayanlabs.blaze3d.platform.GLX;
import com.maayanlabs.blaze3d.systems.GpuDevice;
import com.maayanlabs.blaze3d.systems.RenderSystem;
import java.util.List;
import java.util.Locale;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.client.gui.components.debug.DebugScreenEntry;
import net.mayaan.resources.Identifier;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

public class DebugEntrySystemSpecs
implements DebugScreenEntry {
    private static final Identifier GROUP = Identifier.withDefaultNamespace("system");

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        GpuDevice device = RenderSystem.getDevice();
        displayer.addToGroup(GROUP, List.of(String.format(Locale.ROOT, "Java: %s", System.getProperty("java.version")), String.format(Locale.ROOT, "CPU: %s", GLX._getCpuInfo()), String.format(Locale.ROOT, "Display: %dx%d (%s)", Mayaan.getInstance().getWindow().getWidth(), Mayaan.getInstance().getWindow().getHeight(), device.getVendor()), device.getRenderer(), String.format(Locale.ROOT, "%s %s", device.getBackendName(), device.getVersion())));
    }

    @Override
    public boolean isAllowed(boolean reducedDebugInfo) {
        return true;
    }
}

