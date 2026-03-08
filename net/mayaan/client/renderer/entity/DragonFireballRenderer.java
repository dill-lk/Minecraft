/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.blaze3d.vertex.VertexConsumer;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.EntityRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.EntityRenderState;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.core.BlockPos;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.projectile.hurtingprojectile.DragonFireball;
import org.joml.Quaternionfc;

public class DragonFireballRenderer
extends EntityRenderer<DragonFireball, EntityRenderState> {
    private static final Identifier TEXTURE_LOCATION = Identifier.withDefaultNamespace("textures/entity/enderdragon/dragon_fireball.png");
    private static final RenderType RENDER_TYPE = RenderTypes.entityCutout(TEXTURE_LOCATION);

    public DragonFireballRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected int getBlockLightLevel(DragonFireball entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.scale(2.0f, 2.0f, 2.0f);
        poseStack.mulPose((Quaternionfc)camera.orientation);
        submitNodeCollector.submitCustomGeometry(poseStack, RENDER_TYPE, (pose, buffer) -> {
            DragonFireballRenderer.vertex(buffer, pose, state.lightCoords, 0.0f, 0, 0, 1);
            DragonFireballRenderer.vertex(buffer, pose, state.lightCoords, 1.0f, 0, 1, 1);
            DragonFireballRenderer.vertex(buffer, pose, state.lightCoords, 1.0f, 1, 1, 0);
            DragonFireballRenderer.vertex(buffer, pose, state.lightCoords, 0.0f, 1, 0, 0);
        });
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    private static void vertex(VertexConsumer builder, PoseStack.Pose pose, int lightCoords, float x, int y, int u, int v) {
        builder.addVertex(pose, x - 0.5f, (float)y - 0.25f, 0.0f).setColor(-1).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY).setLight(lightCoords).setNormal(pose, 0.0f, 1.0f, 0.0f);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }
}

