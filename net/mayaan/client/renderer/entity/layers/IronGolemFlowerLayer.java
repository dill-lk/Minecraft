/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import net.mayaan.client.model.animal.golem.IronGolemModel;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.IronGolemRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import org.joml.Quaternionfc;

public class IronGolemFlowerLayer
extends RenderLayer<IronGolemRenderState, IronGolemModel> {
    public IronGolemFlowerLayer(RenderLayerParent<IronGolemRenderState, IronGolemModel> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, IronGolemRenderState state, float yRot, float xRot) {
        if (state.flowerBlock.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        ModelPart arm = ((IronGolemModel)this.getParentModel()).getFlowerHoldingArm();
        arm.translateAndRotate(poseStack);
        poseStack.translate(-1.1875f, 1.0625f, -0.9375f);
        poseStack.translate(0.5f, 0.5f, 0.5f);
        float s = 0.5f;
        poseStack.scale(0.5f, 0.5f, 0.5f);
        poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-90.0f));
        poseStack.translate(-0.5f, -0.5f, -0.5f);
        state.flowerBlock.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
    }
}

