/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.renderer.entity.state.EntityRenderState;

public interface VillagerLikeModel<T extends EntityRenderState> {
    public void translateToArms(T var1, PoseStack var2);
}

