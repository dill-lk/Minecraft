/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.serialization.MapCodec
 *  org.jspecify.annotations.Nullable
 */
package net.minecraft.client.renderer.special;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public interface NoDataSpecialModelRenderer
extends SpecialModelRenderer<Void> {
    @Override
    default public @Nullable Void extractArgument(ItemStack stack) {
        return null;
    }

    @Override
    default public void submit(@Nullable Void argument, ItemDisplayContext type, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, int overlayCoords, boolean hasFoil, int outlineColor) {
        this.submit(type, poseStack, submitNodeCollector, lightCoords, overlayCoords, hasFoil, outlineColor);
    }

    public void submit(ItemDisplayContext var1, PoseStack var2, SubmitNodeCollector var3, int var4, int var5, boolean var6, int var7);

    public static interface Unbaked
    extends SpecialModelRenderer.Unbaked<Void> {
        @Override
        public MapCodec<? extends Unbaked> type();
    }
}

