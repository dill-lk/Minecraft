/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import net.mayaan.client.model.ArmedModel;
import net.mayaan.client.model.EntityModel;
import net.mayaan.client.model.effects.SpearAnimations;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.ArmedEntityRenderState;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.world.entity.EntityType;
import net.mayaan.world.entity.HumanoidArm;
import net.mayaan.world.item.ItemStack;
import net.mayaan.world.item.SwingAnimationType;
import org.joml.Quaternionfc;

public class ItemInHandLayer<S extends ArmedEntityRenderState, M extends EntityModel<S>>
extends RenderLayer<S, M> {
    public ItemInHandLayer(RenderLayerParent<S, M> renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
        this.submitArmWithItem(state, ((ArmedEntityRenderState)state).rightHandItemState, ((ArmedEntityRenderState)state).rightHandItemStack, HumanoidArm.RIGHT, poseStack, submitNodeCollector, lightCoords);
        this.submitArmWithItem(state, ((ArmedEntityRenderState)state).leftHandItemState, ((ArmedEntityRenderState)state).leftHandItemStack, HumanoidArm.LEFT, poseStack, submitNodeCollector, lightCoords);
    }

    protected void submitArmWithItem(S state, ItemStackRenderState item, ItemStack itemStack, HumanoidArm arm, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords) {
        float ticksUsingItem;
        if (item.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        ((ArmedModel)this.getParentModel()).translateToHand(state, arm, poseStack);
        poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(-90.0f));
        poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0f));
        boolean isLeftHand = arm == HumanoidArm.LEFT;
        float offsetX = this.useBabyOffset(state) ? 0.0f : 1.0f;
        float offsetY = this.useBabyOffset(state) ? 1.0f : 2.0f;
        float offsetZ = this.useBabyOffset(state) ? -4.5f : -10.0f;
        poseStack.translate((float)(isLeftHand ? -1 : 1) * offsetX / 16.0f, offsetY / 16.0f, offsetZ / 16.0f);
        if (((ArmedEntityRenderState)state).attackTime > 0.0f && ((ArmedEntityRenderState)state).attackArm == arm && ((ArmedEntityRenderState)state).swingAnimationType == SwingAnimationType.STAB) {
            SpearAnimations.thirdPersonAttackItem(state, poseStack);
        }
        if ((ticksUsingItem = ((ArmedEntityRenderState)state).ticksUsingItem(arm)) != 0.0f) {
            (arm == HumanoidArm.RIGHT ? ((ArmedEntityRenderState)state).rightArmPose : ((ArmedEntityRenderState)state).leftArmPose).animateUseItem(state, poseStack, ticksUsingItem, arm, itemStack);
        }
        item.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, ((ArmedEntityRenderState)state).outlineColor);
        poseStack.popPose();
    }

    private boolean useBabyOffset(S state) {
        return ((ArmedEntityRenderState)state).isBaby && ((ArmedEntityRenderState)state).entityType != EntityType.ARMOR_STAND;
    }
}

