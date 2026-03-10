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
import net.mayaan.world.item.crafting.display.RecipeDisplay;

public record ClientboundPlaceGhostRecipePacket(int containerId, RecipeDisplay recipeDisplay) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundPlaceGhostRecipePacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.CONTAINER_ID, ClientboundPlaceGhostRecipePacket::containerId, RecipeDisplay.STREAM_CODEC, ClientboundPlaceGhostRecipePacket::recipeDisplay, ClientboundPlaceGhostRecipePacket::new);

    @Override
    public PacketType<ClientboundPlaceGhostRecipePacket> type() {
        return GamePacketTypes.CLIENTBOUND_PLACE_GHOST_RECIPE;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handlePlaceRecipe(this);
    }
}

