/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.block.BlockModelRenderState;
import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.world.entity.animal.cow.MushroomCow;

public class MushroomCowRenderState
extends LivingEntityRenderState {
    public MushroomCow.Variant variant = MushroomCow.Variant.RED;
    public final BlockModelRenderState mushroomModel = new BlockModelRenderState();
}

