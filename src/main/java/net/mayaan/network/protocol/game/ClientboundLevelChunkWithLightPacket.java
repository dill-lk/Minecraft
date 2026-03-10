/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.protocol.game;

import java.util.BitSet;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.ClientboundLevelChunkPacketData;
import net.mayaan.network.protocol.game.ClientboundLightUpdatePacketData;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.chunk.LevelChunk;
import net.mayaan.world.level.lighting.LevelLightEngine;
import org.jspecify.annotations.Nullable;

public class ClientboundLevelChunkWithLightPacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundLevelChunkWithLightPacket> STREAM_CODEC = Packet.codec(ClientboundLevelChunkWithLightPacket::write, ClientboundLevelChunkWithLightPacket::new);
    private final int x;
    private final int z;
    private final ClientboundLevelChunkPacketData chunkData;
    private final ClientboundLightUpdatePacketData lightData;

    public ClientboundLevelChunkWithLightPacket(LevelChunk levelChunk, LevelLightEngine lightEngine, @Nullable BitSet skyChangedLightSectionFilter, @Nullable BitSet blockChangedLightSectionFilter) {
        ChunkPos chunkPos = levelChunk.getPos();
        this.x = chunkPos.x();
        this.z = chunkPos.z();
        this.chunkData = new ClientboundLevelChunkPacketData(levelChunk);
        this.lightData = new ClientboundLightUpdatePacketData(chunkPos, lightEngine, skyChangedLightSectionFilter, blockChangedLightSectionFilter);
    }

    private ClientboundLevelChunkWithLightPacket(RegistryFriendlyByteBuf input) {
        this.x = input.readInt();
        this.z = input.readInt();
        this.chunkData = new ClientboundLevelChunkPacketData(input, this.x, this.z);
        this.lightData = new ClientboundLightUpdatePacketData(input, this.x, this.z);
    }

    private void write(RegistryFriendlyByteBuf output) {
        output.writeInt(this.x);
        output.writeInt(this.z);
        this.chunkData.write(output);
        this.lightData.write(output);
    }

    @Override
    public PacketType<ClientboundLevelChunkWithLightPacket> type() {
        return GamePacketTypes.CLIENTBOUND_LEVEL_CHUNK_WITH_LIGHT;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleLevelChunkWithLight(this);
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public ClientboundLevelChunkPacketData getChunkData() {
        return this.chunkData;
    }

    public ClientboundLightUpdatePacketData getLightData() {
        return this.lightData;
    }
}

