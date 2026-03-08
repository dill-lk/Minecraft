/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import java.util.List;
import net.mayaan.SharedConstants;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.client.gui.components.debug.DebugScreenEntry;
import net.mayaan.core.BlockPos;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.LightLayer;
import net.mayaan.world.level.chunk.LevelChunk;
import net.mayaan.world.level.lighting.LevelLightEngine;
import org.jspecify.annotations.Nullable;

public class DebugEntryLight
implements DebugScreenEntry {
    public static final Identifier GROUP = Identifier.withDefaultNamespace("light");

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Mayaan minecraft = Mayaan.getInstance();
        Entity entity = minecraft.getCameraEntity();
        if (entity == null || minecraft.level == null) {
            return;
        }
        BlockPos feetPos = entity.blockPosition();
        int rawBrightness = minecraft.level.getChunkSource().getLightEngine().getRawBrightness(feetPos, 0);
        int sky = minecraft.level.getBrightness(LightLayer.SKY, feetPos);
        int block = minecraft.level.getBrightness(LightLayer.BLOCK, feetPos);
        String clientLight = "Client Light: " + rawBrightness + " (" + sky + " sky, " + block + " block)";
        if (SharedConstants.DEBUG_SHOW_SERVER_DEBUG_VALUES) {
            Object serverLight;
            if (serverChunk != null) {
                LevelLightEngine lightEngine = serverChunk.getLevel().getLightEngine();
                serverLight = "Server Light: (" + lightEngine.getLayerListener(LightLayer.SKY).getLightValue(feetPos) + " sky, " + lightEngine.getLayerListener(LightLayer.BLOCK).getLightValue(feetPos) + " block)";
            } else {
                serverLight = "Server Light: (?? sky, ?? block)";
            }
            displayer.addToGroup(GROUP, List.of(clientLight, serverLight));
        } else {
            displayer.addToGroup(GROUP, clientLight);
        }
    }
}

