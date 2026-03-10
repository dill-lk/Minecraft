/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import java.util.function.Consumer;
import java.util.function.Function;
import net.mayaan.client.model.EntityModel;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.block.BlockModelRenderState;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.RenderLayer;
import net.mayaan.client.renderer.entity.state.EntityRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;

public class BlockDecorationLayer<S extends EntityRenderState, M extends EntityModel<S>>
extends RenderLayer<S, M> {
    private static final Matrix4fc UNIT_CUBE_BOTTOM_CENTER_TO_ANTENNA_CENTER = new Matrix4f().translation(-0.5f, 0.0f, -0.5f).rotateAround((Quaternionfc)Axis.ZP.rotationDegrees(180.0f), 0.5f, 0.5f, 0.5f);
    private final Function<S, BlockModelRenderState> blockModel;
    private final Consumer<PoseStack> transform;

    public BlockDecorationLayer(RenderLayerParent<S, M> renderer, Function<S, BlockModelRenderState> blockModel, Consumer<PoseStack> transform) {
        super(renderer);
        this.blockModel = blockModel;
        this.transform = transform;
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, float yRot, float xRot) {
        BlockModelRenderState blockModel = this.blockModel.apply(state);
        if (blockModel.isEmpty()) {
            return;
        }
        poseStack.pushPose();
        this.transform.accept(poseStack);
        poseStack.mulPose(UNIT_CUBE_BOTTOM_CENTER_TO_ANTENNA_CENTER);
        blockModel.submit(poseStack, submitNodeCollector, lightCoords, OverlayTexture.NO_OVERLAY, ((EntityRenderState)state).outlineColor);
        poseStack.popPose();
    }
}

