/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.world.entity.AnimationState;

public class BatRenderState
extends LivingEntityRenderState {
    public boolean isResting;
    public final AnimationState flyAnimationState = new AnimationState();
    public final AnimationState restAnimationState = new AnimationState();
}

