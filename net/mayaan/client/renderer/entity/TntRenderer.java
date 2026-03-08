/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.block.BlockModelResolver;
import net.mayaan.client.renderer.block.model.BlockDisplayContext;
import net.mayaan.client.renderer.entity.EntityRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.TntMinecartRenderer;
import net.mayaan.client.renderer.entity.state.TntRenderState;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.util.Mth;
import net.mayaan.world.entity.item.PrimedTnt;
import org.joml.Quaternionfc;

public class TntRenderer
extends EntityRenderer<PrimedTnt, TntRenderState> {
    public static final BlockDisplayContext BLOCK_DISPLAY_CONTEXT = BlockDisplayContext.create();
    private final BlockModelResolver blockModelResolver;

    public TntRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.shadowRadius = 0.5f;
        this.blockModelResolver = context.getBlockModelResolver();
    }

    @Override
    public void submit(TntRenderState state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera) {
        poseStack.pushPose();
        poseStack.translate(0.0f, 0.5f, 0.0f);
        float fuse = state.fuseRemainingInTicks;
        if (state.fuseRemainingInTicks < 10.0f) {
            float g = 1.0f - state.fuseRemainingInTicks / 10.0f;
            g = Mth.clamp(g, 0.0f, 1.0f);
            g *= g;
            g *= g;
            float s = 1.0f + g * 0.3f;
            poseStack.scale(s, s, s);
        }
        poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-90.0f));
        poseStack.translate(-0.5f, -0.5f, 0.5f);
        poseStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(90.0f));
        if (!state.blockState.isEmpty()) {
            TntMinecartRenderer.submitWhiteSolidBlock(state.blockState, poseStack, submitNodeCollector, state.lightCoords, (int)fuse / 5 % 2 == 0, state.outlineColor);
        }
        poseStack.popPose();
        super.submit(state, poseStack, submitNodeCollector, camera);
    }

    @Override
    public TntRenderState createRenderState() {
        return new TntRenderState();
    }

    @Override
    public void extractRenderState(PrimedTnt entity, TntRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.fuseRemainingInTicks = (float)entity.getFuse() - partialTicks + 1.0f;
        this.blockModelResolver.update(state.blockState, entity.getBlockState(), BLOCK_DISPLAY_CONTEXT);
    }
}

