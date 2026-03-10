/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Matrix4fStack
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.gui.render.pip;

import com.maayanlabs.blaze3d.platform.Lighting;
import com.maayanlabs.blaze3d.systems.RenderSystem;
import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.math.Axis;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.render.pip.PictureInPictureRenderer;
import net.mayaan.client.renderer.MultiBufferSource;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.state.gui.pip.GuiSkinRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import org.joml.Matrix4fStack;
import org.joml.Quaternionfc;

public class GuiSkinRenderer
extends PictureInPictureRenderer<GuiSkinRenderState> {
    public GuiSkinRenderer(MultiBufferSource.BufferSource bufferSource) {
        super(bufferSource);
    }

    @Override
    public Class<GuiSkinRenderState> getRenderStateClass() {
        return GuiSkinRenderState.class;
    }

    @Override
    protected void renderToTexture(GuiSkinRenderState skinState, PoseStack modelStack) {
        Mayaan.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.PLAYER_SKIN);
        int guiScale = Mayaan.getInstance().gameRenderer.getGameRenderState().windowRenderState.guiScale;
        Matrix4fStack viewStack = RenderSystem.getModelViewStack();
        viewStack.pushMatrix();
        float scale = skinState.scale() * (float)guiScale;
        viewStack.rotateAround((Quaternionfc)Axis.XP.rotationDegrees(skinState.rotationX()), 0.0f, scale * -skinState.pivotY(), 0.0f);
        modelStack.mulPose((Quaternionfc)Axis.YP.rotationDegrees(-skinState.rotationY()));
        modelStack.translate(0.0f, -1.6010001f, 0.0f);
        RenderType skinRenderType = skinState.playerModel().renderType(skinState.texture());
        skinState.playerModel().renderToBuffer(modelStack, this.bufferSource.getBuffer(skinRenderType), 0xF000F0, OverlayTexture.NO_OVERLAY);
        this.bufferSource.endBatch();
        viewStack.popMatrix();
    }

    @Override
    protected String getTextureLabel() {
        return "player skin";
    }
}

