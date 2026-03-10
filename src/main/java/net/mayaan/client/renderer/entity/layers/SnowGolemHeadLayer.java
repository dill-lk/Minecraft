/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import net.mayaan.client.model.animal.golem.SnowGolemModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.LivingEntityRenderer;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.SnowGolemRenderState;
import org.joml.Quaternionfc;

public class SnowGolemHeadLayer
extends RenderLayer<SnowGolemRenderState, SnowGolemModel> {
    public SnowGolemHeadLayer(RenderLayerParent<SnowGolemRenderState, SnowGolemModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, SnowGolemRenderState state, float yRot, float xRot) {
        if (state.headBlock.isEmpty()) {
            return;
        }
        if (state.isInvisible && !state.appearsGlowing()) {
            return;
        }
        poseStack.pushPose();
        ((SnowGolemModel)this.getParentModel()).getHead().translateAndRotate(poseStack);
        float s = 0.625f;
        poseStack.translate(0.0f, -0.34375f, 0.0f);
        poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0f));
        poseStack.scale(0.625f, -0.625f, -0.625f);
        int overlayCoords = LivingEntityRenderer.getOverlayCoords(state, 0.0f);
        poseStack.translate(-0.5f, -0.5f, -0.5f);
        if (state.appearsGlowing() && state.isInvisible) {
            state.headBlock.submitOnlyOutline(poseStack, submitNodeCollector, lightCoords, overlayCoords, state.outlineColor);
        } else {
            state.headBlock.submit(poseStack, submitNodeCollector, lightCoords, overlayCoords, state.outlineColor);
        }
        poseStack.popPose();
    }
}

