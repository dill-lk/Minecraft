/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.world.entity.AnimationState;

public class BreezeRenderState
extends LivingEntityRenderState {
    public final AnimationState idle = new AnimationState();
    public final AnimationState shoot = new AnimationState();
    public final AnimationState slide = new AnimationState();
    public final AnimationState slideBack = new AnimationState();
    public final AnimationState inhale = new AnimationState();
    public final AnimationState longJump = new AnimationState();
}

