/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 *  org.joml.Vector3f
 */
package net.mayaan.client.gui.render.pip;

import com.maayanlabs.blaze3d.platform.Lighting;
import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.render.pip.PictureInPictureRenderer;
import net.mayaan.client.renderer.MultiBufferSource;
import net.mayaan.client.renderer.entity.EntityRenderDispatcher;
import net.mayaan.client.renderer.feature.FeatureRenderDispatcher;
import net.mayaan.client.renderer.state.gui.pip.GuiEntityRenderState;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.joml.Vector3f;

public class GuiEntityRenderer
extends PictureInPictureRenderer<GuiEntityRenderState> {
    private final EntityRenderDispatcher entityRenderDispatcher;

    public GuiEntityRenderer(MultiBufferSource.BufferSource bufferSource, EntityRenderDispatcher entityRenderDispatcher) {
        super(bufferSource);
        this.entityRenderDispatcher = entityRenderDispatcher;
    }

    @Override
    public Class<GuiEntityRenderState> getRenderStateClass() {
        return GuiEntityRenderState.class;
    }

    @Override
    protected void renderToTexture(GuiEntityRenderState entityState, PoseStack poseStack) {
        Mayaan.getInstance().gameRenderer.getLighting().setupFor(Lighting.Entry.ENTITY_IN_UI);
        Vector3f translation = entityState.translation();
        poseStack.translate(translation.x, translation.y, translation.z);
        poseStack.mulPose((Quaternionfc)entityState.rotation());
        Quaternionf overriddenCameraAngle = entityState.overrideCameraAngle();
        FeatureRenderDispatcher featureRenderDispatcher = Mayaan.getInstance().gameRenderer.getFeatureRenderDispatcher();
        CameraRenderState cameraRenderState = new CameraRenderState();
        if (overriddenCameraAngle != null) {
            cameraRenderState.orientation = overriddenCameraAngle.conjugate(new Quaternionf()).rotateY((float)Math.PI);
        }
        this.entityRenderDispatcher.submit(entityState.renderState(), cameraRenderState, 0.0, 0.0, 0.0, poseStack, featureRenderDispatcher.getSubmitNodeStorage());
        featureRenderDispatcher.renderAllFeatures();
    }

    @Override
    protected float getTranslateY(int height, int guiScale) {
        return (float)height / 2.0f;
    }

    @Override
    protected String getTextureLabel() {
        return "entity";
    }
}

