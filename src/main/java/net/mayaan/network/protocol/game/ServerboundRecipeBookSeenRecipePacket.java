/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;
import net.mayaan.world.item.crafting.display.RecipeDisplayId;

public record ServerboundRecipeBookSeenRecipePacket(RecipeDisplayId recipe) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ServerboundRecipeBookSeenRecipePacket> STREAM_CODEC = StreamCodec.composite(RecipeDisplayId.STREAM_CODEC, ServerboundRecipeBookSeenRecipePacket::recipe, ServerboundRecipeBookSeenRecipePacket::new);

    @Override
    public PacketType<ServerboundRecipeBookSeenRecipePacket> type() {
        return GamePacketTypes.SERVERBOUND_RECIPE_BOOK_SEEN_RECIPE;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handleRecipeBookSeenRecipePacket(this);
    }
}

