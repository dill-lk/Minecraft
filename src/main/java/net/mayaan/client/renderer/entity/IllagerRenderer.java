/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity;

import net.mayaan.client.model.monster.illager.IllagerModel;
import net.mayaan.client.renderer.entity.EntityRendererProvider;
import net.mayaan.client.renderer.entity.MobRenderer;
import net.mayaan.client.renderer.entity.layers.CustomHeadLayer;
import net.mayaan.client.renderer.entity.state.ArmedEntityRenderState;
import net.mayaan.client.renderer.entity.state.IllagerRenderState;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.entity.Mob;
import net.mayaan.world.entity.monster.illager.AbstractIllager;
import net.mayaan.world.item.CrossbowItem;

public abstract class IllagerRenderer<T extends AbstractIllager, S extends IllagerRenderState>
extends MobRenderer<T, S, IllagerModel<S>> {
    protected IllagerRenderer(EntityRendererProvider.Context context, IllagerModel<S> model, float shadow) {
        super(context, model, shadow);
        this.addLayer(new CustomHeadLayer(this, context.getModelSet(), context.getPlayerSkinRenderCache()));
    }

    @Override
    public void extractRenderState(T entity, S state, float partialTicks) {
        super.extractRenderState(entity, state, partialTicks);
        ArmedEntityRenderState.extractArmedEntityRenderState(entity, state, this.itemModelResolver, partialTicks);
        ((IllagerRenderState)state).isRiding = ((Entity)entity).isPassenger();
        ((IllagerRenderState)state).mainArm = ((Mob)entity).getMainArm();
        ((IllagerRenderState)state).armPose = ((AbstractIllager)entity).getArmPose();
        ((IllagerRenderState)state).maxCrossbowChargeDuration = ((IllagerRenderState)state).armPose == AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE ? CrossbowItem.getChargeDuration(((LivingEntity)entity).getUseItem(), entity) : 0;
        ((IllagerRenderState)state).ticksUsingItem = ((LivingEntity)entity).getTicksUsingItem(partialTicks);
        ((IllagerRenderState)state).attackAnim = ((LivingEntity)entity).getAttackAnim(partialTicks);
        ((IllagerRenderState)state).isAggressive = ((Mob)entity).isAggressive();
    }
}

