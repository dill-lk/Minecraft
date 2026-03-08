/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.joml.Matrix4fc
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.item;

import com.mojang.serialization.MapCodec;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.renderer.item.ItemModel;
import net.mayaan.client.renderer.item.ItemModelResolver;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.client.resources.model.ResolvableModel;
import net.mayaan.world.entity.ItemOwner;
import net.mayaan.world.item.BundleItem;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.ItemStackTemplate;
import org.joml.Matrix4fc;
import org.jspecify.annotations.Nullable;

public class BundleSelectedItemSpecialRenderer
implements ItemModel {
    private static final ItemModel INSTANCE = new BundleSelectedItemSpecialRenderer();

    @Override
    public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        output.appendModelIdentityElement(this);
        ItemStackTemplate selectedItem = BundleItem.getSelectedItem(item);
        if (selectedItem != null) {
            resolver.appendItemLayers(output, selectedItem.create(), displayContext, level, owner, seed);
        }
    }

    public record Unbaked() implements ItemModel.Unbaked
    {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit((Object)new Unbaked());

        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }

        @Override
        public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
            return INSTANCE;
        }

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
        }
    }
}

