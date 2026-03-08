/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.illager.IllagerModel;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.IllagerRenderer;
import net.mayaan.client.renderer.entity.layers.ItemInHandLayer;
import net.mayaan.client.renderer.entity.state.IllagerRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.monster.illager.Pillager;

public class PillagerRenderer
extends IllagerRenderer<Pillager, IllagerRenderState> {
    private static final Identifier PILLAGER = Identifier.withDefaultNamespace("textures/entity/illager/pillager.png");

    public PillagerRenderer(EntityRendererProvider.Context context) {
        super(context, new IllagerModel(context.bakeLayer(ModelLayers.PILLAGER)), 0.5f);
        this.addLayer(new ItemInHandLayer<IllagerRenderState, IllagerModel<IllagerRenderState>>(this));
    }

    @Override
    public Identifier getTextureLocation(IllagerRenderState state) {
        return PILLAGER;
    }

    @Override
    public IllagerRenderState createRenderState() {
        return new IllagerRenderState();
    }
}

