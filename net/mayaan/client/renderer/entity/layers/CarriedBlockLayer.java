/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import net.mayaan.client.model.monster.enderman.EndermanModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.block.BlockModelRenderState;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.EndermanRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import org.joml.Quaternionfc;

public class CarriedBlockLayer
extends RenderLayer<EndermanRenderState, EndermanModel<EndermanRenderState>> {
    public CarriedBlockLayer(RenderLayerParent<EndermanRenderState, EndermanModel<EndermanRenderState>> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, EndermanRenderState state, float yRot, float xRot) {
        BlockModelRenderState carriedBlock = state.carriedBlock;
        if (carriedBlock.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.6875f, -0.75f);
        poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(20.0f));
        poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(45.0f));
        poseStack.translate(0.25f, 0.1875f, 0.25f);
        float s = 0.5f;
        poseStack.scale(-0.5f, -0.5f, 0.5f);
        poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(90.0f));
        carriedBlock.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
    }
}

