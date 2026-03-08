/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.EntityRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.FireworkRocketRenderState;
import net.mayaan.client.renderer.item.ItemModelResolver;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.world.entity.projectile.FireworkRocketEntity;
import net.mayaan.world.item.ItemDisplayContext;
import org.joml.Quaternionfc;

public class FireworkEntityRenderer
extends EntityRenderer<FireworkRocketEntity, FireworkRocketRenderState> {
    private final ItemModelResolver itemModelResolver;

    public FireworkEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.itemModelResolver = context.getItemModelResolver();
    }

    @Override
    public void submit(FireworkRocketRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose((Quaternionfc)camera.orientation);
        if (state.isShotAtAngle) {
            poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(180.0f));
            poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(180.0f));
            poseStack.mulPose((Quaternionfc)Axis.XP.rotationDegrees(90.0f));
        }
        state.item.submit(poseStack, submitNodeCollector, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor);
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public FireworkRocketRenderState createRenderState() {
        return new FireworkRocketRenderState();
    }

    @Override
    public void extractRenderState(FireworkRocketEntity entity, FireworkRocketRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isShotAtAngle = entity.isShotAtAngle();
        this.itemModelResolver.updateForNonLiving(state.item, entity.getItem(), ItemDisplayContext.GROUND, entity);
    }
}

