/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import net.mayaan.client.model.geom.EntityModelSet;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.wither.WitherBossModel;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.EnergySwirlLayer;
import net.mayaan.client.renderer.entity.state.WitherRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.util.Mth;

public class WitherArmorLayer
extends EnergySwirlLayer<WitherRenderState, WitherBossModel> {
    private static final Identifier WITHER_ARMOR_LOCATION = Identifier.withDefaultNamespace("textures/entity/wither/wither_armor.png");
    private final WitherBossModel model;

    public WitherArmorLayer(RenderLayerParent<WitherRenderState, WitherBossModel> renderer, EntityModelSet modelSet) {
        super(renderer);
        this.model = new WitherBossModel(modelSet.bakeLayer(ModelLayers.WITHER_ARMOR));
    }

    @Override
    protected boolean isPowered(WitherRenderState state) {
        return state.isPowered;
    }

    @Override
    protected float xOffset(float t) {
        return Mth.cos(t * 0.02f) * 3.0f;
    }

    @Override
    protected Identifier getTextureLocation() {
        return WITHER_ARMOR_LOCATION;
    }

    @Override
    protected WitherBossModel model() {
        return this.model;
    }
}

