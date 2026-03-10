/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.MapCodec
 */
package net.mayaan.client.renderer.item;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.mayaan.client.renderer.item.BundleSelectedItemSpecialRenderer;
import net.mayaan.client.renderer.item.CompositeModel;
import net.mayaan.client.renderer.item.ConditionalItemModel;
import net.mayaan.client.renderer.item.CuboidItemModelWrapper;
import net.mayaan.client.renderer.item.EmptyModel;
import net.mayaan.client.renderer.item.ItemModel;
import net.mayaan.client.renderer.item.RangeSelectItemModel;
import net.mayaan.client.renderer.item.SelectItemModel;
import net.mayaan.client.renderer.item.SpecialModelWrapper;
import net.mayaan.resources.Identifier;
import net.mayaan.util.ExtraCodecs;

public class ItemModels {
    private static final ExtraCodecs.LateBoundIdMapper<Identifier, MapCodec<? extends ItemModel.Unbaked>> ID_MAPPER = new ExtraCodecs.LateBoundIdMapper();
    public static final Codec<ItemModel.Unbaked> CODEC = ID_MAPPER.codec(Identifier.CODEC).dispatch(ItemModel.Unbaked::type, c -> c);

    public static void bootstrap() {
        ID_MAPPER.put(Identifier.withDefaultNamespace("empty"), EmptyModel.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("model"), CuboidItemModelWrapper.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("range_dispatch"), RangeSelectItemModel.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("special"), SpecialModelWrapper.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("composite"), CompositeModel.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("bundle/selected_item"), BundleSelectedItemSpecialRenderer.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("select"), SelectItemModel.Unbaked.MAP_CODEC);
        ID_MAPPER.put(Identifier.withDefaultNamespace("condition"), ConditionalItemModel.Unbaked.MAP_CODEC);
    }
}

