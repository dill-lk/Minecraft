/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.world.entity.AnimationState;

public class CreakingRenderState
extends LivingEntityRenderState {
    public final AnimationState invulnerabilityAnimationState = new AnimationState();
    public final AnimationState attackAnimationState = new AnimationState();
    public final AnimationState deathAnimationState = new AnimationState();
    public boolean eyesGlowing;
    public boolean canMove;
}

