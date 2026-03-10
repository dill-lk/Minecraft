/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.item.ItemStack;

public record ClientboundSetPlayerInventoryPacket(int slot, ItemStack contents) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetPlayerInventoryPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.VAR_INT, ClientboundSetPlayerInventoryPacket::slot, ItemStack.OPTIONAL_STREAM_CODEC, ClientboundSetPlayerInventoryPacket::contents, ClientboundSetPlayerInventoryPacket::new);

    @Override
    public PacketType<ClientboundSetPlayerInventoryPacket> type() {
        return GamePacketTypes.CLIENTBOUND_SET_PLAYER_INVENTORY;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleSetPlayerInventory(this);
    }
}

