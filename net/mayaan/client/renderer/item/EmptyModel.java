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
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import org.joml.Matrix4fc;
import org.jspecify.annotations.Nullable;

public class EmptyModel
implements ItemModel {
    public static final ItemModel INSTANCE = new EmptyModel();

    @Override
    public void update(ItemStackRenderState output, ItemStack item, ItemModelResolver resolver, ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
        output.appendModelIdentityElement(this);
    }

    public record Unbaked() implements ItemModel.Unbaked
    {
        public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(Unbaked::new);

        @Override
        public void resolveDependencies(ResolvableModel.Resolver resolver) {
        }

        @Override
        public ItemModel bake(ItemModel.BakingContext context, Matrix4fc transformation) {
            return INSTANCE;
        }

        public MapCodec<Unbaked> type() {
            return MAP_CODEC;
        }
    }
}

