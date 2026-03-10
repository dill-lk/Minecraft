/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.item.ItemStack;

public record ClientboundSetCursorItemPacket(ItemStack contents) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundSetCursorItemPacket> STREAM_CODEC = StreamCodec.composite(ItemStack.OPTIONAL_STREAM_CODEC, ClientboundSetCursorItemPacket::contents, ClientboundSetCursorItemPacket::new);

    @Override
    public PacketType<ClientboundSetCursorItemPacket> type() {
        return GamePacketTypes.CLIENTBOUND_SET_CURSOR_ITEM;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleSetCursorItem(this);
    }
}

