/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.client.model;

import com.maayanlabs.blaze3d.vertex.PoseStack;
import net.mayaan.client.model.geom.ModelPart;

public interface HeadedModel {
    public ModelPart getHead();

    default public void translateToHead(PoseStack poseStack) {
        this.getHead().translateAndRotate(poseStack);
    }
}

