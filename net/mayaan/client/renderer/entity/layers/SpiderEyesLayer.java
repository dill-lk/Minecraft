/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.layers;

import net.mayaan.client.model.monster.spider.SpiderModel;
import net.mayaan.client.renderer.entity.RenderLayerParent;
import net.mayaan.client.renderer.entity.layers.EyesLayer;
import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.client.renderer.rendertype.RenderTypes;
import net.mayaan.resources.Identifier;

public class SpiderEyesLayer<M extends SpiderModel>
extends EyesLayer<LivingEntityRenderState, M> {
    private static final RenderType SPIDER_EYES = RenderTypes.eyes(Identifier.withDefaultNamespace("textures/entity/spider/spider_eyes.png"));

    public SpiderEyesLayer(RenderLayerParent<LivingEntityRenderState, M> renderer) {
        super(renderer);
    }

    @Override
    public RenderType renderType() {
        return SPIDER_EYES;
    }
}

