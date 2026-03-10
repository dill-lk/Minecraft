/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.animal.frog.TadpoleModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.animal.frog.Tadpole;

public class TadpoleRenderer
extends MobRenderer<Tadpole, LivingEntityRenderState, TadpoleModel> {
    private static final Identifier TADPOLE_TEXTURE = Identifier.withDefaultNamespace("textures/entity/tadpole/tadpole.png");

    public TadpoleRenderer(EntityRendererProvider.Context context) {
        super(context, new TadpoleModel(context.bakeLayer(ModelLayers.TADPOLE)), 0.14f);
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return TADPOLE_TEXTURE;
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }
}

