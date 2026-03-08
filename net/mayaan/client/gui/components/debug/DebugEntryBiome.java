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
import net.mayaan.core.Holder;
import net.mayaan.resources.Identifier;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.biome.Biome;
import net.mayaan.world.level.chunk.LevelChunk;
import org.jspecify.annotations.Nullable;

public class DebugEntryBiome
implements DebugScreenEntry {
    private static final Identifier GROUP = Identifier.withDefaultNamespace("biome");

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Mayaan minecraft = Mayaan.getInstance();
        Entity entity = minecraft.getCameraEntity();
        if (entity == null || minecraft.level == null) {
            return;
        }
        BlockPos feetPos = entity.blockPosition();
        if (minecraft.level.isInsideBuildHeight(feetPos.getY())) {
            if (SharedConstants.DEBUG_SHOW_SERVER_DEBUG_VALUES && serverOrClientLevel instanceof ServerLevel) {
                displayer.addToGroup(GROUP, List.of("Biome: " + DebugEntryBiome.printBiome(minecraft.level.getBiome(feetPos)), "Server Biome: " + DebugEntryBiome.printBiome(serverOrClientLevel.getBiome(feetPos))));
            } else {
                displayer.addLine("Biome: " + DebugEntryBiome.printBiome(minecraft.level.getBiome(feetPos)));
            }
        }
    }

    private static String printBiome(Holder<Biome> biome) {
        return (String)biome.unwrap().map(key -> key.identifier().toString(), l -> "[unregistered " + String.valueOf(l) + "]");
    }
}

