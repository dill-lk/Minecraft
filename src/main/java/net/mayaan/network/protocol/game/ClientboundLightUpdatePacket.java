/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.network.protocol.game;

import java.util.BitSet;
import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.ClientboundLightUpdatePacketData;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.level.ChunkPos;
import net.mayaan.world.level.lighting.LevelLightEngine;
import org.jspecify.annotations.Nullable;

public class ClientboundLightUpdatePacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<FriendlyByteBuf, ClientboundLightUpdatePacket> STREAM_CODEC = Packet.codec(ClientboundLightUpdatePacket::write, ClientboundLightUpdatePacket::new);
    private final int x;
    private final int z;
    private final ClientboundLightUpdatePacketData lightData;

    public ClientboundLightUpdatePacket(ChunkPos pos, LevelLightEngine lightEngine, @Nullable BitSet skyChangedLightSectionFilter, @Nullable BitSet blockChangedLightSectionFilter) {
        this.x = pos.x();
        this.z = pos.z();
        this.lightData = new ClientboundLightUpdatePacketData(pos, lightEngine, skyChangedLightSectionFilter, blockChangedLightSectionFilter);
    }

    private ClientboundLightUpdatePacket(FriendlyByteBuf input) {
        this.x = input.readVarInt();
        this.z = input.readVarInt();
        this.lightData = new ClientboundLightUpdatePacketData(input, this.x, this.z);
    }

    private void write(FriendlyByteBuf output) {
        output.writeVarInt(this.x);
        output.writeVarInt(this.z);
        this.lightData.write(output);
    }

    @Override
    public PacketType<ClientboundLightUpdatePacket> type() {
        return GamePacketTypes.CLIENTBOUND_LIGHT_UPDATE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleLightUpdatePacket(this);
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public ClientboundLightUpdatePacketData getLightData() {
        return this.lightData;
    }
}

