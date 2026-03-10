/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import net.mayaan.network.FriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.network.protocol.game.ServerGamePacketListener;
import net.mayaan.world.item.crafting.display.RecipeDisplayId;

public record ServerboundPlaceRecipePacket(int containerId, RecipeDisplayId recipe, boolean useMaxItems) implements Packet<ServerGamePacketListener>
{
    public static final StreamCodec<FriendlyByteBuf, ServerboundPlaceRecipePacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.CONTAINER_ID, ServerboundPlaceRecipePacket::containerId, RecipeDisplayId.STREAM_CODEC, ServerboundPlaceRecipePacket::recipe, ByteBufCodecs.BOOL, ServerboundPlaceRecipePacket::useMaxItems, ServerboundPlaceRecipePacket::new);

    @Override
    public PacketType<ServerboundPlaceRecipePacket> type() {
        return GamePacketTypes.SERVERBOUND_PLACE_RECIPE;
    }

    @Override
    public void handle(ServerGamePacketListener listener) {
        listener.handlePlaceRecipe(this);
    }
}

