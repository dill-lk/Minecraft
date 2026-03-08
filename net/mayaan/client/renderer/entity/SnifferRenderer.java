/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.animal.sniffer.SnifferModel;
import net.mayaan.client.model.geom.ModelLayers;
import net.mayaan.client.renderer.entity.AgeableMobRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.SnifferRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.animal.sniffer.Sniffer;
import net.mayaan.world.phys.AABB;

public class SnifferRenderer
extends AgeableMobRenderer<Sniffer, SnifferRenderState, SnifferModel> {
    private static final Identifier SNIFFER_LOCATION = Identifier.withDefaultNamespace("textures/entity/sniffer/sniffer.png");
    private static final Identifier SNIFFLET_LOCATION = Identifier.withDefaultNamespace("textures/entity/sniffer/snifflet.png");

    public SnifferRenderer(EntityRendererProvider.Context context) {
        super(context, new SnifferModel(context.bakeLayer(ModelLayers.SNIFFER)), new SnifferModel(context.bakeLayer(ModelLayers.SNIFFER_BABY)), 1.1f);
    }

    @Override
    public Identifier getTextureLocation(SnifferRenderState state) {
        return state.isBaby ? SNIFFLET_LOCATION : SNIFFER_LOCATION;
    }

    @Override
    public SnifferRenderState createRenderState() {
        return new SnifferRenderState();
    }

    @Override
    public void extractRenderState(Sniffer entity, SnifferRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.isSearching = entity.isSearching();
        state.diggingAnimationState.copyFrom(entity.diggingAnimationState);
        state.sniffingAnimationState.copyFrom(entity.sniffingAnimationState);
        state.risingAnimationState.copyFrom(entity.risingAnimationState);
        state.feelingHappyAnimationState.copyFrom(entity.feelingHappyAnimationState);
        state.scentingAnimationState.copyFrom(entity.scentingAnimationState);
    }

    @Override
    protected AABB getBoundingBoxForCulling(Sniffer entity) {
        return super.getBoundingBoxForCulling(entity).inflate(0.6f);
    }
}

