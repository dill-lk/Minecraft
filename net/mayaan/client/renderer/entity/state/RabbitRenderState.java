/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.world.entity.AnimationState;
import net.mayaan.world.entity.animal.rabbit.Rabbit;

public class RabbitRenderState
extends LivingEntityRenderState {
    public float jumpCompletion;
    public boolean isToast;
    public Rabbit.Variant variant = Rabbit.Variant.DEFAULT;
    public final AnimationState hopAnimationState = new AnimationState();
    public final AnimationState idleHeadTiltAnimationState = new AnimationState();
}

