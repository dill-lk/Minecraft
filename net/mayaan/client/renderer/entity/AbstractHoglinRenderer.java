/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.geom.ModelLayerLocation;
import net.mayaan.client.model.monster.hoglin.BabyHoglinModel;
import net.mayaan.client.model.monster.hoglin.HoglinModel;
import net.mayaan.client.renderer.entity.AgeableMobRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.HoglinRenderState;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.monster.hoglin.HoglinBase;

public abstract class AbstractHoglinRenderer<T extends Mob>
extends AgeableMobRenderer<T, HoglinRenderState, HoglinModel> {
    public AbstractHoglinRenderer(EntityRendererProvider.Context context, ModelLayerLocation adultLayer, ModelLayerLocation babyLayer, float shadow) {
        super(context, new HoglinModel(context.bakeLayer(adultLayer)), new BabyHoglinModel(context.bakeLayer(babyLayer)), shadow);
    }

    @Override
    public HoglinRenderState createRenderState() {
        return new HoglinRenderState();
    }

    @Override
    public void extractRenderState(T entity, HoglinRenderState state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        state.attackAnimationRemainingTicks = ((HoglinBase)entity).getAttackAnimationRemainingTicks();
    }
}

