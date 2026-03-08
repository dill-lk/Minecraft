/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.creeper.CreeperModel;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.EnergySwirlLayer;
import net.mayaan.client.renderer.entity.state.CreeperRenderState;
import net.mayaan.resources.Identifier;

public class CreeperPowerLayer
extends EnergySwirlLayer<CreeperRenderState, CreeperModel> {
    private static final Identifier POWER_LOCATION = Identifier.withDefaultNamespace("textures/entity/creeper/creeper_armor.png");
    private final CreeperModel model;

    public CreeperPowerLayer(RenderLayerParent<CreeperRenderState, CreeperModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new CreeperModel(modelSet.bakeLayer(ModelLayers.CREEPER_ARMOR));
    }

    @Override
    protected boolean isPowered(CreeperRenderState state) {
        return state.isPowered;
    }

    @Override
    protected float xOffset(float t) {
        return t * 0.01f;
    }

    @Override
    protected Identifier getTextureLocation() {
        return POWER_LOCATION;
    }

    @Override
    protected CreeperModel model() {
        return this.model;
    }
}

