/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.gui.render.pip;

import com.maayanlabs.blaze3d.platform.Lighting;
import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.blaze3d.vertex.VertexConsumer;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.render.pip.PictureInPictureRenderer;
import net.mayaan.client.model.Model;
import net.mayaan.client.renderer.MultiBufferSource;
import net.mayaan.client.renderer.Sheets;
import net.mayaan.client.renderer.state.gui.pip.GuiSignRenderState;
import net.mayaan.client.renderer.texture.OverlayTexture;
import net.mayaan.client.resources.model.sprite.SpriteGetter;
import net.mayaan.client.resources.model.sprite.SpriteId;

public class GuiSignRenderer
extends PictureInPictureRenderer<GuiSignRenderState> {
    private final SpriteGetter sprites;

    public GuiSignRenderer(MultiBufferSource.BufferSource bufferSource, SpriteGetter sprites) {
        super(bufferSource);
        this.sprites = sprites;
    }

    @Override
    public Class<GuiSignRenderState> getRenderStateClass() {
        return GuiSignRenderState.class;
    }

    @Override
    protected void renderToTexture(GuiSignRenderState renderState, PoseStack poseStack) {
        Mayaan.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ITEMS_FLAT);
        poseStack.translate(0.0f, -0.75f, 0.0f);
        SpriteId sprite = Sheets.getSignSprite(renderState.woodType());
        Model.Simple model = renderState.signModel();
        VertexConsumer buffer = sprite.buffer(this.sprites, this.bufferSource, model::renderType);
        model.renderToBuffer(poseStack, buffer, 0xF000F0, OverlayTexture.NO_OVERLAY);
    }

    @Override
    protected String getTextureLabel() {
        return "sign";
    }
}

