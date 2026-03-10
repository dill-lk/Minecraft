/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.resources.Identifier;
import net.mayaan.world.entity.AnimationState;

public class FrogRenderState
extends LivingEntityRenderState {
    private static final Identifier DEFAULT_TEXTURE = Identifier.withDefaultNamespace("textures/entity/frog/frog_temperate.png");
    public boolean isSwimming;
    public final AnimationState jumpAnimationState = new AnimationState();
    public final AnimationState croakAnimationState = new AnimationState();
    public final AnimationState tongueAnimationState = new AnimationState();
    public final AnimationState swimIdleAnimationState = new AnimationState();
    public Identifier texture = DEFAULT_TEXTURE;
}

