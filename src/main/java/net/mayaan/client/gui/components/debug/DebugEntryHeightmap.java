/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Maps
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.gui.components.debug;

import com.google.common.collect.Maps;
import java.util.ArrayList;
import java.util.Map;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.components.debug.DebugScreenDisplayer;
import net.mayaan.client.gui.components.debug.DebugScreenEntry;
import net.mayaan.core.BlockPos;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.chunk.LevelChunk;
import net.mayaan.world.level.levelgen.Heightmap;
import org.jspecify.annotations.Nullable;

public class DebugEntryHeightmap
implements DebugScreenEntry {
    private static final Map<Heightmap.Types, String> HEIGHTMAP_NAMES = Maps.newEnumMap(Map.of(Heightmap.Types.WORLD_SURFACE_WG, "SW", Heightmap.Types.WORLD_SURFACE, "S", Heightmap.Types.OCEAN_FLOOR_WG, "OW", Heightmap.Types.OCEAN_FLOOR, "O", Heightmap.Types.MOTION_BLOCKING, "M", Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, "ML"));
    private static final Identifier GROUP = Identifier.withDefaultNamespace("heightmaps");

    @Override
    public void display(DebugScreenDisplayer displayer, @Nullable Level serverOrClientLevel, @Nullable LevelChunk clientChunk, @Nullable LevelChunk serverChunk) {
        Mayaan minecraft = Mayaan.getInstance();
        Entity entity = minecraft.getCameraEntity();
        if (entity == null || minecraft.level == null || clientChunk == null) {
            return;
        }
        BlockPos feetPos = entity.blockPosition();
        ArrayList<String> result = new ArrayList<String>();
        StringBuilder heightmaps = new StringBuilder("CH");
        for (Heightmap.Types type : Heightmap.Types.values()) {
            if (!type.sendToClient()) continue;
            heightmaps.append(" ").append(HEIGHTMAP_NAMES.get(type)).append(": ").append(clientChunk.getHeight(type, feetPos.getX(), feetPos.getZ()));
        }
        result.add(heightmaps.toString());
        heightmaps.setLength(0);
        heightmaps.append("SH");
        for (Heightmap.Types type : Heightmap.Types.values()) {
            if (!type.keepAfterWorldgen()) continue;
            heightmaps.append(" ").append(HEIGHTMAP_NAMES.get(type)).append(": ");
            if (serverChunk != null) {
                heightmaps.append(serverChunk.getHeight(type, feetPos.getX(), feetPos.getZ()));
                continue;
            }
            heightmaps.append("??");
        }
        result.add(heightmaps.toString());
        displayer.addToGroup(GROUP, result);
    }
}

