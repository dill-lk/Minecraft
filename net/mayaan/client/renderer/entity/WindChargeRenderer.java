/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.object.projectile.WindChargeModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.EntityRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.EntityRenderState;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.projectile.hurtingprojectile.windcharge.AbstractWindCharge;

public class WindChargeRenderer
extends EntityRenderer<AbstractWindCharge, EntityRenderState> {
    private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/projectiles/wind_charge.png");
    private final WindChargeModel model;

    public WindChargeRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new WindChargeModel(context.bakeLayer(ModelLayers.WIND_CHARGE));
    }

    @Override
    public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        submitNodeCollector.submitModel(this.model, state, poseStack, RenderTypes.breezeWind(TEXTURE_LOCATION, this.xOffset(state.ageInTicks) % 1.0f, 0.0f), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    protected float xOffset(float t) {
        return t * 0.03f;
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}

