/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.creeper.CreeperModel;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.layers.CreeperPowerLayer;
import net.mayaan.client.renderer.entity.state.CreeperRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Mth;
import net.mayaan.world.entity.monster.Creeper;

public class CreeperRenderer
extends MobRenderer<Creeper, CreeperRenderState, CreeperModel> {
    private static final Identifier CREEPER_LOCATION = Identifier.withDefaultNamespace("textures/entity/creeper/creeper.png");

    public CreeperRenderer(EntityRendererProvider.Context context) {
        super(context, new CreeperModel(context.bakeLayer(ModelLayers.CREEPER)), 0.5f);
        this.addLayer(new CreeperPowerLayer(this, context.getModelSet()));
    }

    @Override
    protected void scale(CreeperRenderState state, PoseStack poseStack) {
        float g = state.swelling;
        float wobble = 1.0f + Mth.sin(g * 100.0f) * g * 0.01f;
        g = Mth.clamp(g, 0.0f, 1.0f);
        g *= g;
        g *= g;
        float s = (1.0f + g * 0.4f) * wobble;
        float hs = (1.0f + g * 0.1f) / wobble;
        poseStack.scale(s, hs, s);
    }

    @Override
    protected float getWhiteOverlayProgress(CreeperRenderState state) {
        float step = state.swelling;
        if ((int)(step * 10.0f) % 2 == 0) {
            return 0.0f;
        }
        return Mth.clamp(step, 0.5f, 1.0f);
    }

    @Override
    public Identifier getTextureLocation(CreeperRenderState state) {
        return CREEPER_LOCATION;
    }

    @Override
    public CreeperRenderState createRenderState() {
        return new CreeperRenderState();
    }

    @Override
    public void extractRenderState(Creeper entity, CreeperRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.swelling = entity.getSwelling(partialTicks);
        state.isPowered = entity.isPowered();
    }
}

