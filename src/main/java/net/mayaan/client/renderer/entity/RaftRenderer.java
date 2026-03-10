/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.EntityModel;
import net.mayaan.client.model.geom.ModelLayerLocation;
import net.mayaan.client.model.object.boat.RaftModel;
import net.mayaan.client.renderer.entity.AbstractBoatRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.BoatRenderState;
import net.mayaan.client.renderer.rendertype.RenderType;
import net.mayaan.resources.Identifier;

public class RaftRenderer
extends AbstractBoatRenderer {
    private final EntityModel<BoatRenderState> model;
    private final Identifier texture;

    public RaftRenderer(EntityRendererProvider.Context context, ModelLayerLocation modelId) {
        super(context);
        this.texture = modelId.model().withPath(p -> "textures/entity/" + p + ".png");
        this.model = new RaftModel(context.bakeLayer(modelId));
    }

    @Override
    protected EntityModel<BoatRenderState> model() {
        return this.model;
    }

    @Override
    protected RenderType renderType() {
        return this.model.renderType(this.texture);
    }
}

