/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.animal.panda.PandaModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.PandaRenderState;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.util.Mth;

public class PandaHoldsItemLayer
extends RenderLayer<PandaRenderState, PandaModel> {
    public PandaHoldsItemLayer(RenderLayerParent<PandaRenderState, PandaModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, PandaRenderState state, float yRot, float xRot) {
        ItemStackRenderState item = state.heldItem;
        if (item.isEmpty() || !state.isSitting || state.isScared) {
            return;
        }
        float z = -0.6f;
        float y = 1.4f;
        if (state.isEating) {
            z -= 0.2f * Mth.sin(state.ageInTicks * 0.6f) + 0.2f;
            y -= 0.09f * Mth.sin(state.ageInTicks * 0.6f);
        }
        poseStack.pushPose();
        poseStack.translate(0.1f, y, z);
        item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
    }
}

