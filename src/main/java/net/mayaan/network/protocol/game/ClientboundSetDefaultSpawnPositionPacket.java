/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.level.storage.LevelData;

public record ClientboundSetDefaultSpawnPositionPacket(LevelData.RespawnData respawnData) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ClientboundSetDefaultSpawnPositionPacket> STREAM_CODEC = StreamCodec.composite(LevelData.RespawnData.STREAM_CODEC, ClientboundSetDefaultSpawnPositionPacket::respawnData, ClientboundSetDefaultSpawnPositionPacket::new);

    @Override
    public PacketType<ClientboundSetDefaultSpawnPositionPacket> type() {
        return GamePacketTypes.CLIENTBOUND_SET_DEFAULT_SPAWN_POSITION;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleSetSpawn(this);
    }
}

