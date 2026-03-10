/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.client.renderer.item.ItemModelResolver;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.world.entity.LivingEntity;
import net.mayaan.world.item.ItemDisplayContext;

public class HoldingEntityRenderState
extends LivingEntityRenderState {
    public final ItemStackRenderState heldItem = new ItemStackRenderState();

    public static void extractHoldingEntityRenderState(LivingEntity entity, HoldingEntityRenderState state, ItemModelResolver itemModelResolver) {
        itemModelResolver.updateForLiving(state.heldItem, entity.getMainHandItem(), ItemDisplayContext.GROUND, entity);
    }
}

