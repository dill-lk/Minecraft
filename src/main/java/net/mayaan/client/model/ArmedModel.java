/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.renderer.entity.state.EntityRenderState;
import net.mayaan.world.entity.HumanoidArm;

public interface ArmedModel<T extends EntityRenderState> {
    public void translateToHand(T var1, HumanoidArm var2, PoseStack var3);
}

