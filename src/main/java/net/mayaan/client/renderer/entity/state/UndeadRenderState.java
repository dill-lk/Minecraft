/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.entity.state.HumanoidRenderState;
import net.mayaan.world.entity.HumanoidArm;
import net.mayaan.world.item.ItemStack;

public class UndeadRenderState
extends HumanoidRenderState {
    @Override
    public ItemStack getUseItemStackForArm(HumanoidArm arm) {
        return this.getMainHandItemStack();
    }
}

