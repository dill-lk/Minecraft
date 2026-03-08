/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.animal.allay.AllayModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.layers.ItemInHandLayer;
import net.mayaan.client.renderer.entity.state.AllayRenderState;
import net.mayaan.client.renderer.entity.state.ArmedEntityRenderState;
import net.mayaan.core.BlockPos;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.animal.allay.Allay;

public class AllayRenderer
extends MobRenderer<Allay, AllayRenderState, AllayModel> {
    private static final Identifier ALLAY_TEXTURE = Identifier.withDefaultNamespace("textures/entity/allay/allay.png");

    public AllayRenderer(EntityRendererProvider.Context context) {
        super(context, new AllayModel(context.bakeLayer(ModelLayers.ALLAY)), 0.4f);
        this.addLayer(new ItemInHandLayer<AllayRenderState, AllayModel>(this));
    }

    @Override
    public Identifier getTextureLocation(AllayRenderState state) {
        return ALLAY_TEXTURE;
    }

    @Override
    public AllayRenderState createRenderState() {
        return new AllayRenderState();
    }

    @Override
    public void extractRenderState(Allay entity, AllayRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, this.itemModelResolver, partialTicks);
        state.isDancing = entity.isDancing();
        state.isSpinning = entity.isSpinning();
        state.spinningProgress = entity.getSpinningProgress(partialTicks);
        state.holdingAnimationProgress = entity.getHoldingItemAnimationProgress(partialTicks);
    }

    @Override
    protected int getBlockLightLevel(Allay entity, BlockPos blockPos) {
        return 15;
    }
}

