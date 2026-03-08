/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import net.mayaan.client.model.Model;
import net.mayaan.client.model.geom.ModelPart;
import net.mayaan.client.model.player.PlayerModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.LivingEntityRenderer;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.AvatarRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Mth;
import net.mayaan.util.RandomSource;
import org.joml.Quaternionfc;

public abstract class StuckInBodyLayer<M extends PlayerModel, S>
extends RenderLayer<AvatarRenderState, M> {
    private final Model<S> model;
    private final S modelState;
    private final Identifier texture;
    private final PlacementStyle placementStyle;

    public StuckInBodyLayer(LivingEntityRenderer<?, AvatarRenderState, M> renderer, Model<S> model, S modelState, Identifier texture, PlacementStyle placementStyle) {
        super(renderer);
        this.model = model;
        this.modelState = modelState;
        this.texture = texture;
        this.placementStyle = placementStyle;
    }

    protected abstract int numStuck(AvatarRenderState var1);

    private void submitStuckItem(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, float directionX, float directionY, float directionZ, int outlineColor) {
        float directionXZ = Mth.sqrt(directionX * directionX + directionZ * directionZ);
        float yRot = (float)(Math.atan2(directionX, directionZ) * 57.2957763671875);
        float xRot = (float)(Math.atan2(directionY, directionXZ) * 57.2957763671875);
        poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(yRot - 90.0f));
        poseStack.mulPose((Quaternionfc)Axis.ZP.rotationDegrees(xRot));
        submitNodeCollector.submitModel(this.model, this.modelState, poseStack, this.model.renderType(this.texture), lightCoords, OverlayTexture.NO_OVERLAY, outlineColor, null);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, AvatarRenderState state, float yRot, float xRot) {
        int count = this.numStuck(state);
        if (count <= 0) {
            return;
        }
        RandomSource random = RandomSource.createThreadLocalInstance(state.id);
        for (int i = 0; i < count; ++i) {
            poseStack.pushPose();
            ModelPart modelPart = ((PlayerModel)this.getParentModel()).getRandomBodyPart(random);
            ModelPart.Cube cube = modelPart.getRandomCube(random);
            modelPart.translateAndRotate(poseStack);
            float midX = random.nextFloat();
            float midY = random.nextFloat();
            float midZ = random.nextFloat();
            if (this.placementStyle == PlacementStyle.ON_SURFACE) {
                int plane = random.nextInt(3);
                switch (plane) {
                    case 0: {
                        midX = StuckInBodyLayer.snapToFace(midX);
                        break;
                    }
                    case 1: {
                        midY = StuckInBodyLayer.snapToFace(midY);
                        break;
                    }
                    default: {
                        midZ = StuckInBodyLayer.snapToFace(midZ);
                    }
                }
            }
            poseStack.translate(Mth.lerp(midX, cube.minX, cube.maxX) / 16.0f, Mth.lerp(midY, cube.minY, cube.maxY) / 16.0f, Mth.lerp(midZ, cube.minZ, cube.maxZ) / 16.0f);
            this.submitStuckItem(poseStack, submitNodeCollector, lightCoords, -(midX * 2.0f - 1.0f), -(midY * 2.0f - 1.0f), -(midZ * 2.0f - 1.0f), state.outlineColor);
            poseStack.popPose();
        }
    }

    private static float snapToFace(float value) {
        return value > 0.5f ? 1.0f : 0.5f;
    }

    public static enum PlacementStyle {
        IN_CUBE,
        ON_SURFACE;

    }
}

