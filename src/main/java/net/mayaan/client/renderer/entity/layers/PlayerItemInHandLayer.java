/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.EntityModel;
import net.mayaan.client.model.HeadedModel;
import net.mayaan.client.model.Model;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.CustomHeadLayer;
import net.mayaan.client.renderer.entity.layers.ItemInHandLayer;
import net.mayaan.client.renderer.entity.state.AvatarRenderState;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.util.Mth;
import net.mayaan.world.InteractionHand;
import net.mayaan.world.entity.HumanoidArm;
import net.mayaan.world.item.ItemStack;

public class PlayerItemInHandLayer<S extends AvatarRenderState, M extends EntityModel<S> & HeadedModel>
extends ItemInHandLayer<S, M> {
    private static final float X_ROT_MIN = -0.5235988f;
    private static final float X_ROT_MAX = 1.5707964f;

    public PlayerItemInHandLayer(RenderLayerParent<S, M> renderer) {
        super(renderer);
    }

    @Override
    protected void submitArmWithItem(S state, ItemStackRenderState item, ItemStack itemStack, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
        InteractionHand currentHand;
        if (item.isEmpty()) {
            return;
        }
        InteractionHand interactionHand = currentHand = arm == ((AvatarRenderState)state).mainArm ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
        if (((AvatarRenderState)state).isUsingItem && ((AvatarRenderState)state).useItemHand == currentHand && ((AvatarRenderState)state).attackTime < 1.0E-5f && !((AvatarRenderState)state).heldOnHead.isEmpty()) {
            this.renderItemHeldToEye(state, arm, poseStack, submitNodeCollector, lightCoords);
        } else {
            super.submitArmWithItem(state, item, itemStack, arm, poseStack, submitNodeCollector, lightCoords);
        }
    }

    private void renderItemHeldToEye(S state, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
        poseStack.pushPose();
        ((Model)this.getParentModel()).root().translateAndRotate(poseStack);
        ModelPart head = ((HeadedModel)this.getParentModel()).getHead();
        float previousXRot = head.xRot;
        head.xRot = Mth.clamp(head.xRot, -0.5235988f, 1.5707964f);
        head.translateAndRotate(poseStack);
        head.xRot = previousXRot;
        CustomHeadLayer.translateToHead(poseStack, CustomHeadLayer.Transforms.DEFAULT);
        boolean isLeftHand = arm == HumanoidArm.LEFT;
        poseStack.translate((isLeftHand ? -2.5f : 2.5f) / 16.0f, -0.0625f, 0.0f);
        ((AvatarRenderState)state).heldOnHead.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, ((AvatarRenderState)state).outlineColor);
        poseStack.popPose();
    }
}

