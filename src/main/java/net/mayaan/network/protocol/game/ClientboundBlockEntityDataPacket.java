/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import java.util.function.BiFunction;
import net.mayaan.core.BlockPos;
import net.mayaan.core.RegistryAccess;
import net.mayaan.core.registries.Registries;
import net.mayaan.nbt.CompoundTag;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.level.block.entity.BlockEntity;
import net.mayaan.world.level.block.entity.BlockEntityType;

public class ClientboundBlockEntityDataPacket
implements Packet<ClientGamePacketListener> {
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundBlockEntityDataPacket> STREAM_CODEC = StreamCodec.composite(BlockPos.STREAM_CODEC, ClientboundBlockEntityDataPacket::getPos, ByteBufCodecs.registry(Registries.BLOCK_ENTITY_TYPE), ClientboundBlockEntityDataPacket::getType, ByteBufCodecs.TRUSTED_COMPOUND_TAG, ClientboundBlockEntityDataPacket::getTag, ClientboundBlockEntityDataPacket::new);
    private final BlockPos pos;
    private final BlockEntityType<?> type;
    private final CompoundTag tag;

    public static ClientboundBlockEntityDataPacket create(BlockEntity blockEntity, BiFunction<BlockEntity, RegistryAccess, CompoundTag> updateTagSaver) {
        RegistryAccess registryAccess = blockEntity.getLevel().registryAccess();
        return new ClientboundBlockEntityDataPacket(blockEntity.getBlockPos(), blockEntity.getType(), updateTagSaver.apply(blockEntity, registryAccess));
    }

    public static ClientboundBlockEntityDataPacket create(BlockEntity blockEntity) {
        return ClientboundBlockEntityDataPacket.create(blockEntity, BlockEntity::getUpdateTag);
    }

    private ClientboundBlockEntityDataPacket(BlockPos pos, BlockEntityType<?> type, CompoundTag tag) {
        this.pos = pos;
        this.type = type;
        this.tag = tag;
    }

    @Override
    public PacketType<ClientboundBlockEntityDataPacket> type() {
        return GamePacketTypes.CLIENTBOUND_BLOCK_ENTITY_DATA;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleBlockEntityData(this);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public BlockEntityType<?> getType() {
        return this.type;
    }

    public CompoundTag getTag() {
        return this.tag;
    }
}

