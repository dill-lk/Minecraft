/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import java.util.List;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.object.projectile.TridentModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.EntityRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.ItemRenderer;
import net.mayaan.client.renderer.entity.state.ThrownTridentRenderState;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Unit;
import net.mayaan.world.entity.projectile.arrow.ThrownTrident;
import org.joml.Quaternionfc;

public class ThrownTridentRenderer
extends EntityRenderer<ThrownTrident, ThrownTridentRenderState> {
    public static final Identifier TRIDENT_LOCATION = Identifier.withDefaultNamespace("textures/entity/trident/trident.png");
    private final TridentModel model;

    public ThrownTridentRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new TridentModel(context.bakeLayer(ModelLayers.TRIDENT));
    }

    @Override
    public void submit(ThrownTridentRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(state.yRot - 90.0f));
        poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(state.xRot + 90.0f));
        List<RenderType> renderTypes = ItemRenderer.getFoilRenderTypes(this.model.renderType(TRIDENT_LOCATION), false, state.isFoil);
        for (int i = 0; i < renderTypes.size(); ++i) {
            submitNodeCollector.order(i).submitModel(this.model, Unit.INSTANCE, poseStack, renderTypes.get(i), state.lightCoords, OverlayTexture.NO_OVERLAY, -1, null, state.outlineColor, null);
        }
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public ThrownTridentRenderState createRenderState() {
        return new ThrownTridentRenderState();
    }

    @Override
    public void extractRenderState(ThrownTrident entity, ThrownTridentRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.yRot = entity.getYRot(partialTicks);
        state.xRot = entity.getXRot(partialTicks);
        state.isFoil = entity.isFoil();
    }
}

