/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.object.projectile.ArrowModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.EntityRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.ArrowRenderState;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.projectile.arrow.AbstractArrow;
import org.joml.Quaternionfc;

public abstract class ArrowRenderer<T extends AbstractArrow, S extends ArrowRenderState>
extends EntityRenderer<T, S> {
    private final ArrowModel model;

    public ArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new ArrowModel(context.bakeLayer(ModelLayers.ARROW));
    }

    @Override
    public void submit(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(((ArrowRenderState)state).yRot - 90.0f));
        poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(((ArrowRenderState)state).xRot));
        submitNodeCollector.submitModel(this.model, state, poseStack, RenderTypes.entityCutoutCull(this.getTextureLocation(state)), ((ArrowRenderState)state).lightCoords, OverlayTexture.NO_OVERLAY, ((ArrowRenderState)state).outlineColor, null);
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    protected abstract Identifier getTextureLocation(S var1);

    @Override
    public void extractRenderState(T entity, S state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        ((ArrowRenderState)state).xRot = ((Entity)entity).getXRot(partialTicks);
        ((ArrowRenderState)state).yRot = ((Entity)entity).getYRot(partialTicks);
        ((ArrowRenderState)state).shake = (float)((AbstractArrow)entity).shakeTime - partialTicks;
    }
}

