/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.core.BlockPos;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.level.BlockGetter;
import net.mayaan.world.level.block.Block;
import net.mayaan.world.level.block.state.BlockState;

public class ClientboundBlockUpdatePacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundBlockUpdatePacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ClientboundBlockUpdatePacket::getPos, ByteBufCodecs.idMapper(Block.BLOCK_STATE_REGISTRY), ClientboundBlockUpdatePacket::getBlockState, ClientboundBlockUpdatePacket::new);
    private final BlockPos pos;
    private final BlockState blockState;

    public ClientboundBlockUpdatePacket(BlockPos pos, BlockState state) {
        this.pos = pos;
        this.blockState = state;
    }

    public ClientboundBlockUpdatePacket(BlockGetter level, BlockPos pos) {
        this(pos, level.getBlockState(pos));
    }

    @Override
    public PacketType<ClientboundBlockUpdatePacket> type() {
        return GamePacketTypes.CLIENTBOUND_BLOCK_UPDATE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleBlockUpdate(this);
    }

    public BlockState getBlockState() {
        return this.blockState;
    }

    public BlockPos getPos() {
        return this.pos;
    }
}

