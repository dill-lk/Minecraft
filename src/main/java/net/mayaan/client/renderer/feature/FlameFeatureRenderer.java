/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.joml.Quaternionf
 *  org.joml.Quaternionfc
 */
package net.mayaan.client.renderer.feature;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import com.maayanlabs.blaze3d.vertex.VertexConsumer;
import net.mayaan.client.renderer.MultiBufferSource;
import net.mayaan.client.renderer.Sheets;
import net.mayaan.client.renderer.SubmitNodeCollection;
import net.mayaan.client.renderer.SubmitNodeStorage;
import net.mayaan.client.renderer.entity.state.EntityRenderState;
import net.mayaan.client.renderer.texture.TextureAtlasSprite;
import net.mayaan.client.resources.model.ModelBakery;
import net.mayaan.client.resources.model.sprite.AtlasManager;
import net.mayaan.util.LightCoordsUtil;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;

public class FlameFeatureRenderer {
    public void renderSolid(SubmitNodeCollection nodeCollection, MultiBufferSource.BufferSource bufferSource, AtlasManager atlasManager) {
        for (SubmitNodeStorage.FlameSubmit flameSubmit : nodeCollection.getFlameSubmits()) {
            this.renderFlame(flameSubmit.pose(), bufferSource, flameSubmit.entityRenderState(), flameSubmit.rotation(), atlasManager);
        }
    }

    private void renderFlame(PoseStack.Pose pose, MultiBufferSource bufferSource, EntityRenderState state, Quaternionf rotation, AtlasManager atlasManager) {
        TextureAtlasSprite fire1 = atlasManager.get(ModelBakery.FIRE_0);
        TextureAtlasSprite fire2 = atlasManager.get(ModelBakery.FIRE_1);
        float s = state.boundingBoxWidth * 1.4f;
        pose.scale(s, s, s);
        float r = 0.5f;
        float xo = 0.0f;
        float h = state.boundingBoxHeight / s;
        float yo = 0.0f;
        pose.rotate((Quaternionfc)rotation);
        pose.translate(0.0f, 0.0f, 0.3f - (float)((int)h) * 0.02f);
        float zo = 0.0f;
        int ss = 0;
        VertexConsumer buffer = bufferSource.getBuffer(Sheets.cutoutBlockSheet());
        int lightCoords = LightCoordsUtil.withBlock(state.lightCoords, 15);
        while (h > 0.0f) {
            TextureAtlasSprite tex = ss % 2 == 0 ? fire1 : fire2;
            float u0 = tex.getU0();
            float v0 = tex.getV0();
            float u1 = tex.getU1();
            float v1 = tex.getV1();
            if (ss / 2 % 2 == 0) {
                float tmp = u1;
                u1 = u0;
                u0 = tmp;
            }
            FlameFeatureRenderer.fireVertex(pose, buffer, -r - 0.0f, 0.0f - yo, zo, u1, v1, lightCoords);
            FlameFeatureRenderer.fireVertex(pose, buffer, r - 0.0f, 0.0f - yo, zo, u0, v1, lightCoords);
            FlameFeatureRenderer.fireVertex(pose, buffer, r - 0.0f, 1.4f - yo, zo, u0, v0, lightCoords);
            FlameFeatureRenderer.fireVertex(pose, buffer, -r - 0.0f, 1.4f - yo, zo, u1, v0, lightCoords);
            h -= 0.45f;
            yo -= 0.45f;
            r *= 0.9f;
            zo -= 0.03f;
            ++ss;
        }
    }

    private static void fireVertex(PoseStack.Pose pose, VertexConsumer buffer, float x, float y, float z, float u, float v, int lightCoords) {
        buffer.addVertex(pose, x, y, z).setColor(-1).setUv(u, v).setUv1(0, 10).setLight(lightCoords).setNormal(pose, 0.0f, 1.0f, 0.0f);
    }
}

