/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.model.monster.blaze.BlazeModel;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.core.BlockPos;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.monster.Blaze;

public class BlazeRenderer
extends MobRenderer<Blaze, LivingEntityRenderState, BlazeModel> {
    private static final Identifier BLAZE_LOCATION = Identifier.withDefaultNamespace("textures/entity/blaze/blaze.png");

    public BlazeRenderer(EntityRendererProvider.Context context) {
        super(context, new BlazeModel(context.bakeLayer(ModelLayers.BLAZE)), 0.5f);
    }

    @Override
    protected int getBlockLightLevel(Blaze entity, BlockPos blockPos) {
        return 15;
    }

    @Override
    public Identifier getTextureLocation(LivingEntityRenderState state) {
        return BLAZE_LOCATION;
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }
}

