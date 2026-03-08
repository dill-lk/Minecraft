/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.joml.Vector3fc
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.special;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import java.util.function.Consumer;
import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.renderer.PlayerSkinRenderCache;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.resources.model.sprite.SpriteGetter;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;
import org.joml.Vector3fc;
import org.jspecify.annotations.Nullable;

public interface SpecialModelRenderer<T> {
    public void submit(@Nullable T var1, ItemDisplayContext var2, PoseStack var3, SubmitNodeCollector var4, int var5, int var6, boolean var7, int var8);

    public void getExtents(Consumer<Vector3fc> var1);

    public @Nullable T extractArgument(ItemStack var1);

    public static interface BakingContext {
        public EntityModelSet entityModelSet();

        public SpriteGetter sprites();

        public PlayerSkinRenderCache playerSkinRenderCache();
    }

    public static interface Unbaked<T> {
        public @Nullable SpecialModelRenderer<T> bake(BakingContext var1);

        public MapCodec<? extends Unbaked<T>> type();
    }
}

