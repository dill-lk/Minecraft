/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.geom.PartPose;
import net.mayaan.client.model.geom.builders.CubeListBuilder;
import net.mayaan.client.model.geom.builders.LayerDefinition;
import net.mayaan.client.model.geom.builders.MeshDefinition;
import net.mayaan.client.model.geom.builders.PartDefinition;
import net.mayaan.client.model.object.skull.SkullModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.EntityRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.WitherSkullRenderState;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.core.BlockPos;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.projectile.hurtingprojectile.WitherSkull;

public class WitherSkullRenderer
extends EntityRenderer<WitherSkull, WitherSkullRenderState> {
    private static final Identifier WITHER_INVULNERABLE_LOCATION = Identifier.withDefaultNamespace("textures/entity/wither/wither_invulnerable.png");
    private static final Identifier WITHER_LOCATION = Identifier.withDefaultNamespace("textures/entity/wither/wither.png");
    private final SkullModel model;

    public WitherSkullRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new SkullModel(context.bakeLayer(ModelLayers.WITHER_SKULL));
    }

    public static LayerDefinition createSkullLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 35).addBox(-4.0f, -8.0f, -4.0f, 8.0f, 8.0f, 8.0f), PartPose.ZERO);
        return LayerDefinition.create(mesh, 64, 64);
    }

    @Override
    protected int getBlockLightLevel(WitherSkull entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void submit(WitherSkullRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.scale(-1.0f, -1.0f, 1.0f);
        submitNodeCollector.submitModel(this.model, state.modelState, poseStack, this.model.renderType(this.getTextureLocation(state)), state.lightCoords, OverlayTexture.NO_OVERLAY, state.outlineColor, null);
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    private Identifier getTextureLocation(WitherSkullRenderState state) {
        return state.isDangerous ? WITHER_INVULNERABLE_LOCATION : WITHER_LOCATION;
    }

    @Override
    public WitherSkullRenderState createRenderState() {
        return new WitherSkullRenderState();
    }

    @Override
    public void extractRenderState(WitherSkull entity, WitherSkullRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isDangerous = entity.isDangerous();
        state.modelState.animationPos = 0.0f;
        state.modelState.yRot = entity.getYRot(partialTicks);
        state.modelState.xRot = entity.getXRot(partialTicks);
    }
}

