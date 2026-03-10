/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import net.mayaan.client.model.effects.EvokerFangsModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.EntityRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.EvokerFangsRenderState;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.projectile.EvokerFangs;
import org.joml.Quaternionfc;

public class EvokerFangsRenderer
extends EntityRenderer<EvokerFangs, EvokerFangsRenderState> {
    private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/illager/evoker_fangs.png");
    private final EvokerFangsModel model;

    public EvokerFangsRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new EvokerFangsModel(context.bakeLayer(ModelLayers.EVOKER_FANGS));
    }

    @Override
    public void submit(EvokerFangsRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        float biteProgress = state.biteProgress;
        if (biteProgress == 0.0f) {
            return;
        }
        poseStack.pushPose();
        poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(90.0f - state.yRot));
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        poseStack.translate(0.0f, -1.501f, 0.0f);
        submitNodeCollector.submitModel(this.model, state, poseStack, this.model.renderType(TEXTURE_LOCATION), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public EvokerFangsRenderState createRenderState() {
        return new EvokerFangsRenderState();
    }

    @Override
    public void extractRenderState(EvokerFangs entity, EvokerFangsRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.yRot = entity.getYRot();
        state.biteProgress = entity.getAnimationProgress(partialTicks);
    }
}

