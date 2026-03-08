/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import java.util.List;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.item.ItemStack;

public record ClientboundContainerSetContentPacket(int containerId, int stateId, List<ItemStack> items, ItemStack carriedItem) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundContainerSetContentPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.CONTAINER_ID, ClientboundContainerSetContentPacket::containerId, ByteBufCodecs.VAR_INT, ClientboundContainerSetContentPacket::stateId, ItemStack.OPTIONAL_LIST_STREAM_CODEC, ClientboundContainerSetContentPacket::items, ItemStack.OPTIONAL_STREAM_CODEC, ClientboundContainerSetContentPacket::carriedItem, ClientboundContainerSetContentPacket::new);

    @Override
    public PacketType<ClientboundContainerSetContentPacket> type() {
        return GamePacketTypes.CLIENTBOUND_CONTAINER_SET_CONTENT;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleContainerContent(this);
    }
}

