/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import net.mayaan.client.model.animal.llama.LlamaSpitModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.EntityRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.LlamaSpitRenderState;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.projectile.LlamaSpit;
import org.joml.Quaternionfc;

public class LlamaSpitRenderer
extends EntityRenderer<LlamaSpit, LlamaSpitRenderState> {
    private static final Identifier LLAMA_SPIT_LOCATION = Identifier.withDefaultNamespace("textures/entity/llama/llama_spit.png");
    private final LlamaSpitModel model;

    public LlamaSpitRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new LlamaSpitModel(context.bakeLayer(ModelLayers.LLAMA_SPIT));
    }

    @Override
    public void submit(LlamaSpitRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.15f, 0.0f);
        poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(state.yRot - 90.0f));
        poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(state.xRot));
        submitNodeCollector.submitModel(this.model, state, poseStack, this.model.renderType(LLAMA_SPIT_LOCATION), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public LlamaSpitRenderState createRenderState() {
        return new LlamaSpitRenderState();
    }

    @Override
    public void extractRenderState(LlamaSpit entity, LlamaSpitRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.yRot = entity.getYRot(partialTicks);
    }
}

