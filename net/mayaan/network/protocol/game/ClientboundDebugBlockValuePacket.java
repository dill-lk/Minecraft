/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.core.BlockPos;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.util.debug.DebugSubscription;

public record ClientboundDebugBlockValuePacket(BlockPos blockPos, DebugSubscription.Update<?> update) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundDebugBlockValuePacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ClientboundDebugBlockValuePacket::blockPos, DebugSubscription.Update.STREAM_CODEC, ClientboundDebugBlockValuePacket::update, ClientboundDebugBlockValuePacket::new);

    @Override
    public PacketType<ClientboundDebugBlockValuePacket> type() {
        return GamePacketTypes.CLIENTBOUND_DEBUG_BLOCK_VALUE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleDebugBlockValue(this);
    }
}

