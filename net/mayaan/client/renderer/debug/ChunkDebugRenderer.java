/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.ImmutableMap
 *  com.google.common.collect.ImmutableMap$Builder
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.debug;

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import net.mayaan.client.Mayaan;
import net.mayaan.client.multiplayer.ClientChunkCache;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.debug.DebugRenderer;
import net.mayaan.client.server.IntegratedServer;
import net.mayaan.core.SectionPos;
import net.mayaan.gizmos.Gizmos;
import net.mayaan.gizmos.TextGizmo;
import net.mayaan.resources.ResourceKey;
import net.mayaan.server.level.ServerChunkCache;
import net.mayaan.server.level.ServerLevel;
import net.mayaan.util.Util;
import net.mayaan.util.debug.DebugValueAccess;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.Level;
import net.mayaan.world.level.chunk.LevelChunk;
import net.mayaan.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public class ChunkDebugRenderer
implements DebugRenderer.SimpleDebugRenderer {
    private final Mayaan minecraft;
    private double lastUpdateTime = Double.MIN_VALUE;
    private final int radius = 12;
    private @Nullable ChunkData data;

    public ChunkDebugRenderer(Mayaan minecraft) {
        this.minecraft = minecraft;
    }

    @Override
    public void emitGizmos(double camX, double camY, double camZ, DebugValueAccess debugValues, Frustum frustum, float partialTicks) {
        double time = Util.getNanos();
        if (time - this.lastUpdateTime > 3.0E9) {
            this.lastUpdateTime = time;
            IntegratedServer server = this.minecraft.getSingleplayerServer();
            this.data = server != null ? new ChunkData(this, server, camX, camZ) : null;
        }
        if (this.data != null) {
            Map serverData = this.data.serverData.getNow(null);
            double y = this.minecraft.gameRenderer.getMainCamera().position().y * 0.85;
            for (Map.Entry<ChunkPos, String> entry : this.data.clientData.entrySet()) {
                ChunkPos pos = entry.getKey();
                Object value = entry.getValue();
                if (serverData != null) {
                    value = (String)value + (String)serverData.get(pos);
                }
                String[] parts = ((String)value).split("\n");
                int yOffset = 0;
                for (String part : parts) {
                    Gizmos.billboardText(part, new Vec3(SectionPos.sectionToBlockCoord(pos.x(), 8), y + (double)yOffset, SectionPos.sectionToBlockCoord(pos.z(), 8)), TextGizmo.Style.whiteAndCentered().withScale(2.4f)).setAlwaysOnTop();
                    yOffset -= 2;
                }
            }
        }
    }

    private final class ChunkData {
        private final Map<ChunkPos, String> clientData;
        private final CompletableFuture<Map<ChunkPos, String>> serverData;

        private ChunkData(ChunkDebugRenderer chunkDebugRenderer, IntegratedServer server, double camX, double camZ) {
            Objects.requireNonNull(chunkDebugRenderer);
            ClientLevel clientLevel = chunkDebugRenderer.minecraft.level;
            ResourceKey<Level> dimension = clientLevel.dimension();
            int cx = SectionPos.posToSectionCoord(camX);
            int cz = SectionPos.posToSectionCoord(camZ);
            ImmutableMap.Builder builder = ImmutableMap.builder();
            ClientChunkCache clientChunkSource = clientLevel.getChunkSource();
            for (int x = cx - 12; x <= cx + 12; ++x) {
                for (int z = cz - 12; z <= cz + 12; ++z) {
                    ChunkPos pos = new ChunkPos(x, z);
                    Object result = "";
                    LevelChunk clientChunk = clientChunkSource.getChunk(x, z, false);
                    result = (String)result + "Client: ";
                    if (clientChunk == null) {
                        result = (String)result + "0n/a\n";
                    } else {
                        result = (String)result + (clientChunk.isEmpty() ? " E" : "");
                        result = (String)result + "\n";
                    }
                    builder.put((Object)pos, result);
                }
            }
            this.clientData = builder.build();
            this.serverData = server.submit(() -> {
                ServerLevel serverLevel = server.getLevel(dimension);
                if (serverLevel == null) {
                    return ImmutableMap.of();
                }
                ImmutableMap.Builder serverBuilder = ImmutableMap.builder();
                ServerChunkCache serverChunkSource = serverLevel.getChunkSource();
                for (int x = cx - 12; x <= cx + 12; ++x) {
                    for (int z = cz - 12; z <= cz + 12; ++z) {
                        ChunkPos pos = new ChunkPos(x, z);
                        serverBuilder.put((Object)pos, (Object)("Server: " + serverChunkSource.getChunkDebugData(pos)));
                    }
                }
                return serverBuilder.build();
            });
        }
    }
}

