/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.feature;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.renderer.MultiBufferSource;
import net.mayaan.client.renderer.OutlineBufferSource;
import net.mayaan.client.renderer.SubmitNodeCollection;
import net.mayaan.client.renderer.SubmitNodeStorage;
import net.mayaan.client.renderer.entity.ItemRenderer;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.client.resources.model.geometry.BakedQuad;

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

