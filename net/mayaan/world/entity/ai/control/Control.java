/*
 * Decompiled with CFR 0.152.
 */
package net.mayaan.world.entity.ai.control;

import net.mayaan.util.Mth;

public interface Control {
    default public float rotateTowards(float fromAngle, float toAngle, float maxRot) {
        float diff = Mth.degreesDifference(fromAngle, toAngle);
        float diffClamped = Mth.clamp(diff, -maxRot, maxRot);
        return fromAngle + diffClamped;
    }
}

