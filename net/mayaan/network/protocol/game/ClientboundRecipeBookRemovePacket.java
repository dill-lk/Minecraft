/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  io.netty.buffer.ByteBuf
 */
package net.mayaan.network.protocol.game;

import io.netty.buffer.ByteBuf;
import java.util.List;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.world.item.crafting.display.RecipeDisplayId;

public record ClientboundRecipeBookRemovePacket(List<RecipeDisplayId> recipes) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<ByteBuf, ClientboundRecipeBookRemovePacket> STREAM_CODEC = StreamCodec.composite(RecipeDisplayId.STREAM_CODEC.apply(ByteBufCodecs.list()), ClientboundRecipeBookRemovePacket::recipes, ClientboundRecipeBookRemovePacket::new);

    @Override
    public PacketType<ClientboundRecipeBookRemovePacket> type() {
        return GamePacketTypes.CLIENTBOUND_RECIPE_BOOK_REMOVE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleRecipeBookRemove(this);
    }
}

