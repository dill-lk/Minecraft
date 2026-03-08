/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Matrix4f
 *  org.joml.Matrix4fc
 *  org.joml.Quaternionfc
 *  org.jspecify.annotations.Nullable
 */
package net.mayaan.client.renderer.feature;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.mayaan.client.Mayaan;
import net.mayaan.client.gui.Font;
import net.mayaan.client.renderer.MultiBufferSource;
import net.mayaan.client.renderer.SubmitNodeCollection;
import net.mayaan.client.renderer.SubmitNodeStorage;
import net.mayaan.client.renderer.state.level.CameraRenderState;
import net.mayaan.network.chat.Component;
import net.mayaan.util.LightCoordsUtil;
import net.mayaan.world.phys.Vec3;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;
import org.joml.Quaternionfc;
import org.jspecify.annotations.Nullable;

public class NameTagFeatureRenderer {
    public void renderTranslucent(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, Font font) {
        Storage storage = nodeCollection.getNameTagSubmits();
        storage.nameTagSubmitsSeethrough.sort(Comparator.comparing(SubmitNodeStorage.NameTagSubmit::distanceToCameraSq).reversed());
        for (SubmitNodeStorage.NameTagSubmit nameTag : storage.nameTagSubmitsSeethrough) {
            font.drawInBatch(nameTag.text(), nameTag.x(), nameTag.y(), nameTag.color(), false, nameTag.pose(), (MultiBufferSource)bufferSource, Font.DisplayMode.SEE_THROUGH, nameTag.backgroundColor(), nameTag.lightCoords());
        }
        for (SubmitNodeStorage.NameTagSubmit nameTag : storage.nameTagSubmitsNormal) {
            font.drawInBatch(nameTag.text(), nameTag.x(), nameTag.y(), nameTag.color(), false, nameTag.pose(), (MultiBufferSource)bufferSource, Font.DisplayMode.NORMAL, nameTag.backgroundColor(), nameTag.lightCoords());
        }
    }

    public static class Storage {
        private final List<SubmitNodeStorage.NameTagSubmit> nameTagSubmitsSeethrough = new ArrayList<SubmitNodeStorage.NameTagSubmit>();
        private final List<SubmitNodeStorage.NameTagSubmit> nameTagSubmitsNormal = new ArrayList<SubmitNodeStorage.NameTagSubmit>();

        public void add(PoseStack poseStack, @Nullable Vec3 nameTagAttachment, int offset, Component name, boolean seeThrough, int lightCoords, double distanceToCameraSq, CameraRenderState camera) {
            if (nameTagAttachment == null) {
                return;
            }
            Mayaan minecraft = Mayaan.getInstance();
            poseStack.pushPose();
            poseStack.translate(nameTagAttachment.x, nameTagAttachment.y + 0.5, nameTagAttachment.z);
            poseStack.mulPose((Quaternionfc)camera.orientation);
            poseStack.scale(0.025f, -0.025f, 0.025f);
            Matrix4f pose = new Matrix4f((Matrix4fc)poseStack.last().pose());
            float x = (float)(-minecraft.font.width(name)) / 2.0f;
            int backgroundColor = (int)(minecraft.gameRenderer.getGameRenderState().optionsRenderState.getBackgroundOpacity(0.25f) * 255.0f) << 24;
            if (seeThrough) {
                this.nameTagSubmitsNormal.add(new SubmitNodeStorage.NameTagSubmit(pose, x, offset, name, LightCoordsUtil.lightCoordsWithEmission(lightCoords, 2), -1, 0, distanceToCameraSq));
                this.nameTagSubmitsSeethrough.add(new SubmitNodeStorage.NameTagSubmit(pose, x, offset, name, lightCoords, -2130706433, backgroundColor, distanceToCameraSq));
            } else {
                this.nameTagSubmitsNormal.add(new SubmitNodeStorage.NameTagSubmit(pose, x, offset, name, lightCoords, -2130706433, backgroundColor, distanceToCameraSq));
            }
            poseStack.popPose();
        }

        public void clear() {
            this.nameTagSubmitsNormal.clear();
            this.nameTagSubmitsSeethrough.clear();
        }
    }
}

