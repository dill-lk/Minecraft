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
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.multiplayer.ClientLevel;
import net.mayaan.client.renderer.PlayerSkinRenderCache;
import net.mayaan.client.renderer.item.ItemModelResolver;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.client.renderer.item.MissingItemModel;
import net.mayaan.client.renderer.special.SpecialModelRenderer;
import net.mayaan.client.resources.model.ModelBaker;
import net.mayaan.client.resources.model.ResolvableModel;
import net.mayaan.client.resources.model.sprite.SpriteGetter;
import net.mayaan.util.RegistryContextSwapper;
import net.mayaan.world.entity.ItemOwner;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import org.joml.Matrix4fc;
import org.jspecify.annotations.Nullable;

public interface ItemModel {
    public void update(ItemStackRenderState var1, ItemStack var2, ItemModelResolver var3, ItemDisplayContext var4, @Nullable ClientLevel var5, @Nullable ItemOwner var6, int var7);

    public record BakingContext(ModelBaker blockModelBaker, EntityModelSet entityModelSet, SpriteGetter sprites, PlayerSkinRenderCache playerSkinRenderCache, MissingItemModel missingItemModel, @Nullable RegistryContextSwapper contextSwapper) implements SpecialModelRenderer.BakingContext
    {
        public MissingItemModel missingItemModel(Matrix4fc transformation) {
            return this.missingItemModel.withTransform(transformation);
        }
    }

    public static interface Unbaked
    extends ResolvableModel {
        public MapCodec<? extends Unbaked> type();

        public ItemModel bake(BakingContext var1, Matrix4fc var2);
    }
}

