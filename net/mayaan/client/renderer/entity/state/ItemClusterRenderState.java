/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.entity.state.EntityRenderState;
import net.mayaan.client.renderer.item.ItemModelResolver;
import net.mayaan.client.renderer.item.ItemStackRenderState;
import net.mayaan.world.entity.Entity;
import net.mayaan.world.item.Item;
import net.mayaan.world.item.ItemDisplayContext;
import net.mayaan.world.item.ItemStack;

public class ItemClusterRenderState
extends EntityRenderState {
    public final ItemStackRenderState item = new ItemStackRenderState();
    public int count;
    public int seed;

    public void extractItemGroupRenderState(Entity entity, ItemStack stack, ItemModelResolver itemModelResolver) {
        itemModelResolver.updateForNonLiving(this.item, stack, ItemDisplayContext.GROUND, entity);
        this.count = ItemClusterRenderState.getRenderedAmount(stack.getCount());
        this.seed = ItemClusterRenderState.getSeedForItemStack(stack);
    }

    public static int getSeedForItemStack(ItemStack itemStack) {
        return itemStack.isEmpty() ? 187 : Item.getId(itemStack.getItem()) + itemStack.getDamageValue();
    }

    public static int getRenderedAmount(int stackCount) {
        if (stackCount <= 1) {
            return 1;
        }
        if (stackCount <= 16) {
            return 2;
        }
        if (stackCount <= 32) {
            return 3;
        }
        if (stackCount <= 48) {
            return 4;
        }
        return 5;
    }
}

