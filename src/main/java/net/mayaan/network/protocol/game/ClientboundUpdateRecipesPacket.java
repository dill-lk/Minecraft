/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.network.protocol.game;

import java.util.HashMap;
import java.util.Map;
import net.mayaan.network.RegistryFriendlyByteBuf;
import net.mayaan.network.codec.ByteBufCodecs;
import net.mayaan.network.codec.StreamCodec;
import net.mayaan.network.protocol.Packet;
import net.mayaan.network.protocol.PacketType;
import net.mayaan.network.protocol.game.ClientGamePacketListener;
import net.mayaan.network.protocol.game.GamePacketTypes;
import net.mayaan.resources.ResourceKey;
import net.mayaan.world.item.crafting.RecipePropertySet;
import net.mayaan.world.item.crafting.SelectableRecipe;
import net.mayaan.world.item.crafting.StonecutterRecipe;

public record ClientboundUpdateRecipesPacket(Map<ResourceKey<RecipePropertySet>, RecipePropertySet> itemSets, SelectableRecipe.SingleInputSet<StonecutterRecipe> stonecutterRecipes) implements Packet<ClientGamePacketListener>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateRecipesPacket> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.map(HashMap::new, ResourceKey.streamCodec(RecipePropertySet.TYPE_KEY), RecipePropertySet.STREAM_CODEC), ClientboundUpdateRecipesPacket::itemSets, SelectableRecipe.SingleInputSet.noRecipeCodec(), ClientboundUpdateRecipesPacket::stonecutterRecipes, ClientboundUpdateRecipesPacket::new);

    @Override
    public PacketType<ClientboundUpdateRecipesPacket> type() {
        return GamePacketTypes.CLIENTBOUND_UPDATE_RECIPES;
    }

    @Override
    public void handle(ClientGamePacketListener listener) {
        listener.handleUpdateRecipes(this);
    }
}

