/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.renderer.entity.state;

import net.mayaan.client.model.animal.parrot.ParrotModel;
import net.mayaan.client.renderer.entity.state.LivingEntityRenderState;
import net.mayaan.world.entity.animal.parrot.Parrot;

public class ParrotRenderState
extends LivingEntityRenderState {
    public Parrot.Variant variant = Parrot.Variant.RED_BLUE;
    public float flapAngle;
    public ParrotModel.Pose pose = ParrotModel.Pose.FLYING;
}

