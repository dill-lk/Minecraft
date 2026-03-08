/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.geom.ModelLayerLocation;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.spider.SpiderModel;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.layers.SpiderEyesLayer;
import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.monster.spider.Spider;

public class SpiderRenderer<T extends Spider>
extends MobRenderer<T, LivingEntityRenderState, SpiderModel> {
    private static final Identifier SPIDER_LOCATION = Identifier.withDefaultNamespace("textures/entity/spider/spider.png");

    public SpiderRenderer(EntityRendererProvider.Context context) {
        this(context, ModelLayers.SPIDER);
    }

    public SpiderRenderer(EntityRendererProvider.Context context, ModelLayerLocation model) {
        super(context, new SpiderModel(context.bakeLayer(model)), 0.8f);
        this.addLayer(new SpiderEyesLayer<SpiderModel>(this));
    }

    @Override
    protected float getFlipDegrees() {
        return 180.0f;
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return SPIDER_LOCATION;
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }

    @Override
    public void extractRenderState(T entity, LivingEntityRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
    }
}

