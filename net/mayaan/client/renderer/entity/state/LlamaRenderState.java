/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.world.entity.animal.equine.Llama;
import net.mayaan.world.item.ItemStack;

public class LlamaRenderState
extends LivingEntityRenderState {
    public Llama.Variant variant = Llama.Variant.DEFAULT;
    public boolean hasChest;
    public ItemStack bodyItem = ItemStack.EMPTY;
    public boolean isTraderLlama;
}

