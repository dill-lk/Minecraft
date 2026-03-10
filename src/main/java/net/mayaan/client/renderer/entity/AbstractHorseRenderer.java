/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.EntityModel;
import net.mayaan.client.renderer.entity.AgeableMobRenderer;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.state.EquineRenderState;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.EquipmentSlot;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.animal.equine.AbstractHorse;

public abstract class AbstractHorseRenderer<T extends AbstractHorse, S extends EquineRenderState, M extends EntityModel<? super S>>
extends AgeableMobRenderer<T, S, M> {
    public AbstractHorseRenderer(EntityRendererProvider.Context context, M model, M babyModel) {
        super(context, model, babyModel, 0.75f);
    }

    @Override
    public void extractRenderState(T entity, S state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        ((EquineRenderState)state).saddle = ((LivingEntity)entity).getItemBySlot(EquipmentSlot.SADDLE).copy();
        ((EquineRenderState)state).bodyArmorItem = ((Mob)entity).getBodyArmorItem().copy();
        ((EquineRenderState)state).isRidden = ((Entity)entity).isVehicle();
        ((EquineRenderState)state).eatAnimation = ((AbstractHorse)entity).getEatAnim(partialTicks);
        ((EquineRenderState)state).standAnimation = ((AbstractHorse)entity).getStandAnim(partialTicks);
        ((EquineRenderState)state).feedingAnimation = ((AbstractHorse)entity).getMouthAnim(partialTicks);
        ((EquineRenderState)state).animateTail = ((AbstractHorse)entity).tailCounter > 0;
    }
}

