/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.animal.dolphin.DolphinModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.DolphinRenderState;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.util.Mth;

public class DolphinCarryingItemLayer
extends RenderLayer<DolphinRenderState, DolphinModel> {
    public DolphinCarryingItemLayer(RenderLayerParent<DolphinRenderState, DolphinModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, DolphinRenderState state, float yRot, float xRot) {
        ItemStackRenderState item = state.heldItem;
        if (item.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        float y = 1.0f;
        float z = -1.0f;
        float angleXPercent = Mth.abs(state.xRot) / 60.0f;
        if (state.xRot < 0.0f) {
            poseStack.translate(0.0f, 1.0f - angleXPercent * 0.5f, -1.0f + angleXPercent * 0.5f);
        } else {
            poseStack.translate(0.0f, 1.0f + angleXPercent * 0.8f, -1.0f + angleXPercent * 0.2f);
        }
        item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
    }
}

