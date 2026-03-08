/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.block.BlockModelRenderState;
import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.world.entity.Crackiness;

public class IronGolemRenderState
extends LivingEntityRenderState {
    public float attackTicksRemaining;
    public int offerFlowerTick;
    public final BlockModelRenderState flowerBlock = new BlockModelRenderState();
    public Crackiness.Level crackiness = Crackiness.Level.NONE;
}

