/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.silverfish.SilverfishModel;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.monster.Silverfish;

public class SilverfishRenderer
extends MobRenderer<Silverfish, LivingEntityRenderState, SilverfishModel> {
    private static final Identifier SILVERFISH_LOCATION = Identifier.withDefaultNamespace("textures/entity/silverfish/silverfish.png");

    public SilverfishRenderer(EntityRendererProvider.Context context) {
        super(context, new SilverfishModel(context.bakeLayer(ModelLayers.SILVERFISH)), 0.3f);
    }

    @Override
    protected float getFlipDegrees() {
        return 180.0f;
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return SILVERFISH_LOCATION;
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }
}

