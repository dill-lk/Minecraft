/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.object.crystal.EndCrystalModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.culling.Frustum;
import net.mayaan.client.renderer.entity.EnderDragonRenderer;
import net.mayaan.client.renderer.entity.EntityRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.EndCrystalRenderState;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.core.BlockPos;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Mth;
import net.mayaan.world.entity.boss.enderdragon.EndCrystal;
import net.mayaan.world.phys.Vec3;

public class EndCrystalRenderer
extends EntityRenderer<EndCrystal, EndCrystalRenderState> {
    private static final Identifier END_CRYSTAL_LOCATION = Identifier.withDefaultNamespace("textures/entity/end_crystal/end_crystal.png");
    private static final RenderType RENDER_TYPE = RenderTypes.entityCutout(END_CRYSTAL_LOCATION);
    private final EndCrystalModel model;

    public EndCrystalRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5f;
        this.model = new EndCrystalModel(context.bakeLayer(ModelLayers.END_CRYSTAL));
    }

    @Override
    public void submit(EndCrystalRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.scale(2.0f, 2.0f, 2.0f);
        poseStack.translate(0.0f, -0.5f, 0.0f);
        submitNodeCollector.submitModel(this.model, state, poseStack, RENDER_TYPE, state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
        Vec3 beamOffset = state.beamOffset;
        if (beamOffset != null) {
            float crystalY = EndCrystalRenderer.getY(state.ageInTicks);
            float deltaX = (float)beamOffset.x;
            float deltaY = (float)beamOffset.y;
            float deltaZ = (float)beamOffset.z;
            poseStack.translate(beamOffset);
            EnderDragonRenderer.submitCrystalBeams(-deltaX, -deltaY + crystalY, -deltaZ, state.ageInTicks, poseStack, submitNodeCollector, state.lightCoords);
        }
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    public static float getY(float timeInTicks) {
        float hh = Mth.sin(timeInTicks * 0.2f) / 2.0f + 0.5f;
        hh = (hh * hh + hh) * 0.4f;
        return hh - 1.4f;
    }

    @Override
    public EndCrystalRenderState createRenderState() {
        return new EndCrystalRenderState();
    }

    @Override
    public void extractRenderState(EndCrystal entity, EndCrystalRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.ageInTicks = (float)entity.time + partialTicks;
        state.showsBottom = entity.showsBottom();
        BlockPos beamTarget = entity.getBeamTarget();
        state.beamOffset = beamTarget != null ? Vec3.atCenterOf(beamTarget).subtract(entity.getPosition(partialTicks)) : null;
    }

    @Override
    public boolean shouldRender(EndCrystal entity, Frustum culler, double camX, double camY, double camZ) {
        return super.shouldRender(entity, culler, camX, camY, camZ) || entity.getBeamTarget() != null;
    }
}

