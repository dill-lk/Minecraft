/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.EntityModel;
import net.mayaan.client.model.Model;
import net.mayaan.client.renderer.SubmitNodeCollector;
import net.mayaan.client.renderer.entity.LivingEntityRenderer;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.state.EntityRenderState;
import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.resources.Identifier;

public abstract class RenderLayer<S extends EntityRenderState, M extends EntityModel<? super S>> {
    private final RenderLayerParent<S, M> renderer;

    public RenderLayer(RenderLayerParent<S, M> renderer) {
        this.renderer = renderer;
    }

    protected static <S extends LivingEntityRenderState> void coloredCutoutModelCopyLayerRender(Model<? super S> model, Identifier texture, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, int color, int order) {
        if (!state.isInvisible) {
            RenderLayer.renderColoredCutoutModel(model, texture, poseStack, submitNodeCollector, lightCoords, state, color, order);
        }
    }

    protected static <S extends LivingEntityRenderState> void renderColoredCutoutModel(Model<? super S> model, Identifier texture, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int lightCoords, S state, int color, int order) {
        submitNodeCollector.order(order).submitModel(model, state, poseStack, RenderTypes.entityCutout(texture), lightCoords, LivingEntityRenderer.getOverlayCoords(state, 0.0f), color, null, state.outlineColor, null);
    }

    public M getParentModel() {
        return this.renderer.getModel();
    }

    public abstract void submit(PoseStack var1, SubmitNodeCollector var2, int var3, S var4, float var5, float var6);
}

