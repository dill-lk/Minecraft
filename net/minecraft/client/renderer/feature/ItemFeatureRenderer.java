/*
 * Decompiled with CFR 0.152.
 */
package net.minecraft.client.renderer.feature;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.SubmitNodeCollection;
import net.minecraft.client.renderer.SubmitNodeStorage;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.geometry.BakedQuad;

public class ItemFeatureRenderer {
    private final PoseStack poseStack = new PoseStack();

    public void renderSolid(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, OutlineBufferSource outlineBufferSource) {
        for (SubmitNodeStorage.ItemSubmit submit : nodeCollection.getItemSubmits()) {
            if (ItemFeatureRenderer.hasTranslucency(submit)) continue;
            this.poseStack.pushPose();
            this.poseStack.last().set(submit.pose());
            ItemRenderer.renderItem(submit.displayContext(), this.poseStack, bufferSource, submit.lightCoords(), submit.overlayCoords(), submit.tintLayers(), submit.quads(), submit.foilType());
            if (submit.outlineColor() != 0) {
                outlineBufferSource.setColor(submit.outlineColor());
                ItemRenderer.renderItem(submit.displayContext(), this.poseStack, outlineBufferSource, submit.lightCoords(), submit.overlayCoords(), submit.tintLayers(), submit.quads(), ItemStackRenderState.FoilType.NONE);
            }
            this.poseStack.popPose();
        }
    }

    public void renderTranslucent(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, OutlineBufferSource outlineBufferSource) {
        for (SubmitNodeStorage.ItemSubmit submit : nodeCollection.getItemSubmits()) {
            if (!ItemFeatureRenderer.hasTranslucency(submit)) continue;
            this.poseStack.pushPose();
            this.poseStack.last().set(submit.pose());
            ItemRenderer.renderItem(submit.displayContext(), this.poseStack, bufferSource, submit.lightCoords(), submit.overlayCoords(), submit.tintLayers(), submit.quads(), submit.foilType());
            if (submit.outlineColor() != 0) {
                outlineBufferSource.setColor(submit.outlineColor());
                ItemRenderer.renderItem(submit.displayContext(), this.poseStack, outlineBufferSource, submit.lightCoords(), submit.overlayCoords(), submit.tintLayers(), submit.quads(), ItemStackRenderState.FoilType.NONE);
            }
            this.poseStack.popPose();
        }
    }

    private static boolean hasTranslucency(SubmitNodeStorage.ItemSubmit submit) {
        for (BakedQuad quad : submit.quads()) {
            if (!quad.spriteInfo().itemRenderType().hasBlending()) continue;
            return true;
        }
        return false;
    }
}

